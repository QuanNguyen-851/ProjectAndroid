package com.example.projectandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.CategoryEntity;
import model.HistoryEntity;
import model.ProfileEntity;

public class CreateHistoryActivity extends AppCompatActivity {
    Integer categoryId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_history);
        TextView currentAccount = findViewById(R.id.create_history_account_current);
        currentAccount.setText(getAccount().getAccount().toString());
        EditText payment = findViewById(R.id.create_history_account);
        TextView paymentErr = findViewById(R.id.create_history_account_err);
        EditText description = findViewById(R.id.create_history_description);
        TextView descriptionErr = findViewById(R.id.create_history_description_err);
        Integer curNumber = Integer.valueOf(currentAccount.getText().toString());
        Spinner spinner = findViewById(R.id.create_history_category);
        List<String> list = getAllCategoryName();
        ArrayAdapter adapter = new ArrayAdapter(CreateHistoryActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryEntity entity = getCategoryByName(list.get(i));
                categoryId= entity.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                } else if (pay > curNumber) {
                    paymentErr.setText("Bạn đã sử dụng vượt quá số dư,\n số tiền nợ sẽ được tính vào lần cập nhật tiếp theo");
                    paymentErr.setVisibility(View.VISIBLE);
                    payment.requestFocus();
                } else {
                    paymentErr.setVisibility(View.GONE);
                }

                currentAccount.setText("" + (curNumber - pay) + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        findViewById(R.id.create_history_btnSave).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Boolean paymentIsValid = false;
                Boolean descriptionIsValid = false;
                Long pay = Long.valueOf(payment.getText().toString().isEmpty() ? "0" : payment.getText().toString());
                Long result = curNumber - pay;
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
                    update(result, viewPreferences("profile_phone"));
                    HistoryEntity historyEntity = new HistoryEntity();
                    historyEntity.setCategoryId(categoryId);
                    historyEntity.setPayment(pay);
                    historyEntity.setProfileId(getAccount().getId());
                    historyEntity.setDescription(description.getText().toString());
                    create(historyEntity);
                    startActivity(new Intent(CreateHistoryActivity.this, DrawerActivity.class));
                }
            }
        });
        findViewById(R.id.create_history_account_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateHistoryActivity.this, DrawerActivity.class));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Void create(HistoryEntity entity) {
        LocalDate today = LocalDate.now();
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        ContentValues value = new ContentValues();
        value.put("categoryId", entity.getCategoryId());
        value.put("payment", entity.getPayment());
        value.put("description", entity.getDescription());
        value.put("date", today.toString());
        value.put("profileId", entity.getProfileId());
        db.insert("history", null, value);
        return null;
    }

    private String viewPreferences(String key) {
        SharedPreferences viewPreferences = getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        return viewPreferences.getString(key, null);
    }

    private void update(Long account, String phone) {
        System.out.println(account);
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        ContentValues value = new ContentValues();
        value.put("account", account);
        db.update("profile", value, "phone = '" + phone + "'", null);
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

    private CategoryEntity getCategoryByName(String name) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);

        String sql = "select * from category where name ='" + name + "'";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();

        return new CategoryEntity(rs.getInt(0), rs.getString(1));
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