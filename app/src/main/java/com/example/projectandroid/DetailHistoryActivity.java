package com.example.projectandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.CategoryEntity;
import model.HistoryEntity;
import model.ProfileEntity;

public class DetailHistoryActivity extends AppCompatActivity {
    Integer categoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        TextView currentAccount = findViewById(R.id.detail_history_account_current);
        EditText payment = findViewById(R.id.detail_history_account);
        TextView paymentErr = findViewById(R.id.detail_history_account_err);
        Spinner categoryName = findViewById(R.id.detail_history_category);
        EditText description = findViewById(R.id.detail_history_description);
        TextView descriptionErr = findViewById(R.id.detail_history_description_err);
        ProfileEntity myProfile = getAccount();
        currentAccount.setText(myProfile.getAccount().toString());
        Bundle historyBun = getIntent().getExtras();
        List<String> list = getAllCategoryName();
        Long originAccount = myProfile.getAccount();
        Long curNumber = 0L;
        Long curPayment = 0L;
        Integer historyId = 0;
        try {

            HistoryEntity historyEntity = (HistoryEntity) historyBun.getSerializable("history");
            payment.setText(historyEntity.getPayment().toString());
            curPayment = Long.valueOf(payment.getText().toString());
            historyId = historyEntity.getId();
            description.setText(historyEntity.getDescription());
            curNumber = originAccount + historyEntity.getPayment();
            ArrayAdapter adapter = new ArrayAdapter(DetailHistoryActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoryName.setAdapter(adapter);
            if (!historyEntity.getCategoryName().isEmpty()) {
                int spinnerPosition = adapter.getPosition(historyEntity.getCategoryName());
                categoryName.setSelection(spinnerPosition);
            }

        } catch (Exception e) {
            throw e;
        }
        categoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryEntity entity = getCategoryByName(list.get(i));
                categoryId = entity.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Long finalCurNumber = curNumber;
        payment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Long pay = Long.valueOf(payment.getText().toString().isEmpty() ? "0" : payment.getText().toString());
                if (pay < 1000L) {
                    paymentErr.setText("vui lòng nhập số tiền lớn hơn 1000vnd");
                    paymentErr.setVisibility(View.VISIBLE);
                    payment.requestFocus();
                } else if (pay > finalCurNumber) {
                    paymentErr.setText("Bạn đã sử dụng vượt quá số dư,\n số tiền nợ sẽ được tính vào lần cập nhật tiếp theo");
                    paymentErr.setVisibility(View.VISIBLE);
                    payment.requestFocus();
                } else {
                    paymentErr.setVisibility(View.GONE);
                }

                currentAccount.setText("" + (finalCurNumber - pay) + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Integer finalHistoryId = historyId;
        findViewById(R.id.detail_history_btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean paymentIsValid = false;
                Boolean descriptionIsValid = false;
                Long pay = Long.valueOf(payment.getText().toString().isEmpty() ? "0" : payment.getText().toString());
                Long result = finalCurNumber - pay;
                if (pay < 1000L) {
                    paymentErr.setText("vui lòng nhập số tiền lớn hơn 1000vnd");
                    paymentErr.setVisibility(View.VISIBLE);
                    payment.requestFocus();
                    paymentIsValid = false;
                } else {
                    paymentErr.setVisibility(View.GONE);
                    paymentIsValid = true;
                }

                if (description.getText().toString().isEmpty()) {
                    descriptionErr.setText("Vui lòng nhập mô tả!");
                    descriptionErr.setVisibility(View.VISIBLE);
                    descriptionIsValid = false;
                } else {
                    descriptionIsValid = true;
                    descriptionErr.setVisibility(View.GONE);
                }
                if (descriptionIsValid && paymentIsValid) {
                    updateProfile(result, viewPreferences("profile_phone"));
                    HistoryEntity historyEntity = new HistoryEntity();
                    historyEntity.setCategoryId(categoryId);
                    historyEntity.setPayment(pay);
                    historyEntity.setProfileId(getAccount().getId());
                    historyEntity.setDescription(description.getText().toString());
                    updateHistory(finalHistoryId, historyEntity);
                    startActivity(new Intent(DetailHistoryActivity.this, DrawerActivity.class));
                }
            }
        });

        findViewById(R.id.detail_history_account_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailHistoryActivity.this, DrawerActivity.class));
            }
        });
        Long finalCurPayment = curPayment;
        findViewById(R.id.detail_history_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailHistoryActivity.this);
                dialog.setTitle("Bạn có chắc muốn xóa không ? ");
                dialog.setMessage("Sau khi xóa số tiền đã chi tiêu sẽ được cộng vào số dư ");
                dialog.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Long refundAccount = originAccount + finalCurPayment;
                        updateProfile(refundAccount, viewPreferences("profile_phone"));
                        delete(finalHistoryId);
                        startActivity(new Intent(DetailHistoryActivity.this, DrawerActivity.class));

                    }
                });
                dialog.show();
            }
        });


    }

    private Void delete(Integer id) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        String sql = " DELETE FROM history where id = " + id;
        db.execSQL(sql);
        return null;
    }

    private Void updateHistory(Integer historyId, HistoryEntity entity) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        ContentValues value = new ContentValues();
        value.put("categoryId", entity.getCategoryId());
        value.put("payment", entity.getPayment());
        value.put("description", entity.getDescription());
        db.update("history", value, "id = " + historyId, null);
        return null;
    }

    private String viewPreferences(String key) {
        SharedPreferences viewPreferences = getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        return viewPreferences.getString(key, null);
    }

    private void updateProfile(Long account, String phone) {
        System.out.println(account);
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        ContentValues value = new ContentValues();
        value.put("account", account);
        Toast.makeText(this, "Số dư tài khoản đã được cập nhật: " + account + ".vnđ !", Toast.LENGTH_SHORT).show();
        db.update("profile", value, "phone = '" + phone + "'", null);
    }

    private CategoryEntity getCategoryByName(String name) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);

        String sql = "select * from category where name ='" + name + "'";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();

        return new CategoryEntity(rs.getInt(0), rs.getString(1));
    }

    private List<String> getAllCategoryName() {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);

        List<String> list = new ArrayList<>();
        String sql = "select * from category";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        list.clear();
        while (!rs.isAfterLast()) {
            list.add(rs.getString(1));
            rs.moveToNext();
        }
        return list;
    }

    private ProfileEntity getAccount() {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        SharedPreferences viewPreferences = getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        String phone = viewPreferences.getString("profile_phone", null);
        String sql = "select * from profile where " +
                " phone = '" + phone + "'" +
                " LIMIT 1";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setId(rs.getInt(0));
        profileEntity.setAccount(rs.getLong(8));
        return profileEntity;
    }
}