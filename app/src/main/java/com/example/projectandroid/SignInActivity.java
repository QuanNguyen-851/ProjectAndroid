package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import model.ProfileEntity;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        EditText phone = findViewById(R.id.signIn_phone);
        EditText password = findViewById(R.id.signIn_password);
        EditText rePassword = findViewById(R.id.signIn_rePassword);
        TextView phoneErr = findViewById(R.id.signIn_phone_err);
        TextView passwordErr = findViewById(R.id.signIn_password_err);
        TextView rePasswordErr = findViewById(R.id.signIn_rePassword_err);
        findViewById(R.id.signIn_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isValidPhone = true;
                Boolean isValidPass = true;
                Boolean isValidRePass = true;
                if (phone.getText().toString().isEmpty()) {
                    phoneErr.setText("* Vui lòng nhập số điện thoại");
                    phoneErr.setVisibility(View.VISIBLE);
                    isValidPhone = false;
                } else if (phone.getText().toString().length() < 10) {
                    phoneErr.setText("* Vui lòng nhập đúng định dạng");
                    phoneErr.setVisibility(View.VISIBLE);
                    isValidPhone = false;

                } else if (checkPhoneIsValid(phone.getText().toString())) {
                    phoneErr.setText("* Số điện thoại này đã được đăng ký");
                    phoneErr.setVisibility(View.VISIBLE);
                    isValidPhone = false;

                } else {
                    phoneErr.setVisibility(View.GONE);
                }
                if (password.getText().toString().isEmpty() || password.getText().toString().length() < 5) {
                    passwordErr.setText("* Vui lòng nhập mật trên 5 kí tự");
                    passwordErr.setVisibility(View.VISIBLE);
                    isValidPass = false;

                } else {
                    passwordErr.setVisibility(View.GONE);
                }
                if (rePassword.getText().toString().isEmpty()) {
                    rePasswordErr.setText("* Vui lòng nhập lại mật khẩu");
                    rePasswordErr.setVisibility(View.VISIBLE);
                    isValidRePass = false;

                } else if (!checkRePassword(password.getText().toString(), rePassword.getText().toString())) {
                    rePasswordErr.setText("* Không đúng mật khẩu");
                    rePasswordErr.setVisibility(View.VISIBLE);
                    isValidRePass = false;

                } else {
                    rePasswordErr.setVisibility(View.GONE);
                }

                if (isValidPhone && isValidPass && isValidRePass) {
                    Intent signInProfile = new Intent(SignInActivity.this, SignInProfileActivity.class);
                    Bundle signInBun = new Bundle();
                    ProfileEntity profileEntity = new ProfileEntity();
                    profileEntity.setPhone(phone.getText().toString());
                    profileEntity.setPassword(password.getText().toString());
                    signInBun.putSerializable("profile", profileEntity);
                    signInProfile.putExtras(signInBun);
                    signInBun.clear();
                    startActivity(signInProfile);
                }
            }
        });
    }

    private Boolean checkRePassword(String newPass, String oldPass) {
        if (newPass.equals(oldPass)) {
            return true;
        }
        return false;
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

}