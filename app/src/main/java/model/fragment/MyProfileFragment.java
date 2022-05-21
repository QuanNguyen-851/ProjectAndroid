package model.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectandroid.AuthenticationActivity;
import com.example.projectandroid.MainActivity;
import com.example.projectandroid.ProfileActivity;
import com.example.projectandroid.R;
import com.example.projectandroid.SignInProfileActivity;

import java.util.Calendar;

import model.ProfileEntity;

public class MyProfileFragment extends Fragment {
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View viewActivity = lf.inflate(R.layout.activity_profile, container, false);
        EditText name = viewActivity.findViewById(R.id.my_profile_name);
        TextView nameErr = viewActivity.findViewById(R.id.my_profile_name_err);
        RadioGroup gender = viewActivity.findViewById(R.id.my_profile_gender);
        RadioButton genderNam = viewActivity.findViewById(R.id.my_profile_gender_nam);
        RadioButton genderNu = viewActivity.findViewById(R.id.my_profile_gender_nu);
        EditText dateOfBirth = viewActivity.findViewById(R.id.my_profile_birthDate);
        EditText address = viewActivity.findViewById(R.id.my_profile_address);
        EditText job = viewActivity.findViewById(R.id.my_profile_job);
        EditText account = viewActivity.findViewById(R.id.my_profile_account);
        TextView accountErr = viewActivity.findViewById(R.id.my_profile_account_err);
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                dateOfBirth.setText(day + "/" + month + "/" + year);
            }
        };









        SharedPreferences viewPreferences = getActivity().getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        String phone = viewPreferences.getString("profile_phone", null);
        SQLiteDatabase db = getActivity().openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        String sql = "select * from profile where " +
                " phone = '" + phone + "'" +
                " LIMIT 1";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        name.setText(rs.getString(1));
        if(rs.getString(4).equals("Nam")){
            genderNam.setChecked(true);
        }else if(rs.getString(4).equals("Nữ")){
            genderNu.setChecked(true);
        }
        dateOfBirth.setText(rs.getString(6));
        address.setText(rs.getString(5));
        job.setText(rs.getString(7));
        account.setText(String.valueOf(rs.getLong(8)));
        Button button = viewActivity.findViewById(R.id.my_profile_saveBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton checked = viewActivity.findViewById(gender.getCheckedRadioButtonId());
                Boolean isValidName = true;
                Boolean isValidAcc = true;
                if (name.getText().toString().isEmpty() || name.getText().toString().length() < 5) {
                    nameErr.setText("* Vui lòng nhập tên trên 5 ký tự ");
                    nameErr.setVisibility(View.VISIBLE);
                    isValidName = false;
                } else {
                    nameErr.setVisibility(View.GONE);
                    isValidName = true;
                }
                if (account.getText().toString().isEmpty()) {
                    accountErr.setText("* Vui lòng nhập số dư");
                    accountErr.setVisibility(View.VISIBLE);
                    isValidAcc = false;
                }else if(Integer.parseInt(account.getText().toString())<10000){
                    accountErr.setText("* Số dư tối thiểu là 10.000vnđ");
                    accountErr.setVisibility(View.VISIBLE);
                    isValidAcc = false;                }
                ProfileEntity profileEntity = new ProfileEntity();
                profileEntity.setId(rs.getInt(0));
                profileEntity.setName(name.getText().toString());
                profileEntity.setGender(checked.getText().toString());
                profileEntity.setAddress(address.getText().toString());
                profileEntity.setBirthDate(dateOfBirth.getText().toString());
                profileEntity.setJob(job.getText().toString());
                profileEntity.setAccount(Long.valueOf(account.getText().toString()));
                if(isValidName&& isValidAcc){
                    update(profileEntity);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });

        return viewActivity;
    }

    private void update(ProfileEntity profileEntity) {
        SQLiteDatabase db = getActivity().openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        ContentValues value = new ContentValues();
        value.put("name", profileEntity.getName());
        value.put("gender", profileEntity.getGender());
        value.put("dateOfBirth", profileEntity.getBirthDate());
        value.put("address", profileEntity.getAddress());
        value.put("job", profileEntity.getJob());
        value.put("account", profileEntity.getAccount());
        db.update("profile", value, "id = " + profileEntity.getId() , null);
    }
}
