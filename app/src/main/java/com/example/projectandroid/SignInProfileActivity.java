package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.ProfileEntity;

public class SignInProfileActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_profile);
//        choseBirthDate event
        EditText birthDate = findViewById(R.id.signIn_profile_birthDate);
        TextView birthDateErr = findViewById(R.id.signIn_profile_birthDate_err);
        EditText name = findViewById(R.id.signIn_profile_name);
        TextView nameErr = findViewById(R.id.signIn_profile_name_err);
        RadioGroup gender = findViewById(R.id.signIn_profile_gender);
        EditText address = findViewById(R.id.signIn_profile_address);
        TextView addressErr = findViewById(R.id.signIn_profile_address_err);
        EditText job = findViewById(R.id.signIn_profile_job);
        TextView jobErr = findViewById(R.id.signIn_profile_job_err);

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SignInProfileActivity.this,
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
                birthDate.setText(day + "/" + month + "/" + year);
            }
        };
//      SignIn flow
        Button signInButton = findViewById(R.id.signIn_profile_saveBtn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isValidName = true;
                Boolean isValidDob = true;
                RadioButton genderchecked = findViewById(gender.getCheckedRadioButtonId());
                if (name.getText().toString().isEmpty() || name.getText().toString().length() < 5) {
                    nameErr.setText("* Vui lòng nhập tên trên 5 ký tự ");
                    nameErr.setVisibility(View.VISIBLE);
                    isValidName = false;
                } else {
                    nameErr.setVisibility(View.GONE);
                    isValidName = true;
                }
                if (birthDate.getText().toString().isEmpty()) {
                    birthDateErr.setText("* Vui lòng nhập ngày sinh ");
                    birthDateErr.setVisibility(View.VISIBLE);
                    isValidDob = false;
                } else {
                    isValidDob = true;
                    birthDateErr.setVisibility(View.GONE);
                }
                if (isValidDob && isValidName) {

                    Bundle signInBun = getIntent().getExtras();
                    try {

                        ProfileEntity profileEntity = (ProfileEntity) signInBun.getSerializable("profile");
                        profileEntity.setName(name.getText().toString());
                        profileEntity.setGender(genderchecked.getText().toString());
                        profileEntity.setBirthDate(birthDate.getText().toString());
                        profileEntity.setAddress(address.getText().toString());
                        profileEntity.setJob(job.getText().toString());
                        if (!checkPhoneIsValid(profileEntity.getPhone())) {
                            create(profileEntity);
                        }
                        startActivity(new Intent(SignInProfileActivity.this, LoginActivity.class));

                    } catch (Exception e) {
                        throw e;
                    }
                }
            }
        });


    }

    public Boolean checkPhoneIsValid(String phone) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        String sql = "select count(id) from profile where phone = '" + phone + "' LIMIT 1";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        if (rs.getInt(0) >= 1) {
            return true;
        }
        return false;
    }

    private void create(ProfileEntity profileEntity) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        ContentValues value = new ContentValues();
        value.put("name", profileEntity.getName());
        value.put("gender", profileEntity.getGender());
        value.put("phone", profileEntity.getPhone());
        value.put("dateOfBirth", profileEntity.getBirthDate());
        value.put("password", profileEntity.getPassword());
        value.put("address", profileEntity.getAddress());
        value.put("job", profileEntity.getJob());
        db.insert("profile", null, value);
    }
}