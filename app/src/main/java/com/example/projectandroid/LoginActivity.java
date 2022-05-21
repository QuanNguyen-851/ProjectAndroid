package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Base64;

public class LoginActivity extends AppCompatActivity {

    private EditText phone;
    private EditText password;
    private TextView phoneErr;
    private TextView passwordErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(viewPreferences("profile_phone")!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        Button login = findViewById(R.id.login_btn);
        TextView back = findViewById(R.id.login_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, AuthenticationActivity.class));
            }
        });
        phone = findViewById(R.id.login_phone);
        phoneErr = findViewById(R.id.login_phone_err);
        password = findViewById(R.id.login_password);
        passwordErr = findViewById(R.id.login_password_err);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isValidPhone = true;
                Boolean isValidPass = true;
                if (phone.getText().toString().isEmpty() || phone.getText().toString().length() < 10) {
                    phoneErr.setText("* Vui lòng nhập đúng định dạng");
                    phoneErr.setVisibility(View.VISIBLE);
                    isValidPhone = false;
                } else {
                    phoneErr.setVisibility(View.GONE);
                    isValidPhone = true;
                }

                if (password.getText().toString().isEmpty()) {
                    passwordErr.setText("* Vui lòng nhập mật khẩu");
                    passwordErr.setVisibility(View.VISIBLE);
                    isValidPass = false;
                } else {
                    isValidPass = true;
                    passwordErr.setVisibility(View.GONE);
                }
                if (isValidPass && isValidPhone) {
                    if (!checkPhoneAndPass(phone.getText().toString(), password.getText().toString())) {
                        passwordErr.setText("* Mật khẩu hoặc số điện thoại của bạn không đúng!");
                        passwordErr.setVisibility(View.VISIBLE);
                    } else {
                        login(phone.getText().toString(), password.getText().toString());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }
            }
        });

    }
    private String viewPreferences(String key) {
        SharedPreferences viewPreferences = getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        return viewPreferences.getString(key, null);
    }

    private Void login(String phone, String pass){
        SharedPreferences preferences = getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profile_phone", phone);
        editor.putString("profile_pass", pass);
        editor.commit();
        return null;
    }

    private Boolean checkPhoneAndPass(String phone, String pass) {
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        String sql = "select count(id) from profile where " +
                " phone = '" + phone + "'" +
                " and password = '" + pass + "'" +
                " LIMIT 1";
        Cursor rs = db.rawQuery(sql, null);
        rs.moveToFirst();
        if (rs.getInt(0) >= 1) {
            return true;
        }
        return false;
    }
}