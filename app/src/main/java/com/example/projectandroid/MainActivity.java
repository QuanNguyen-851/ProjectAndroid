package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import model.ProfileEntity;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        if (viewPreferences("profile_phone") == null) {
            startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
        } else {
            System.out.println(viewPreferences("profile_phone"));
            String sql = "select * from profile where " +
                    " phone = '" + viewPreferences("profile_phone") + "'" +
                    " LIMIT 1";
            Cursor rs = db.rawQuery(sql, null);
            rs.moveToFirst();
            System.out.println(rs.getLong(8));
            if (rs.getInt(8) < 10000) {
                final EditText input = new EditText(this);
                input.setText("10000");
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Thông báo !");
                alert.setMessage("Tài khoản của bạn còn dưới 10.000 vnđ, vui lòng cập nhật số dư");
                alert.setView(input);
                alert.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        update(Integer.valueOf(input.getText().toString()), viewPreferences("profile_phone"));
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                });
                alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.show();
            } else {
                startActivity(new Intent(MainActivity.this, DrawerActivity.class));
            }
        }
    }

    private String viewPreferences(String key) {
        SharedPreferences viewPreferences = getSharedPreferences("ProjectAndroidSetting", MODE_PRIVATE);
        return viewPreferences.getString(key, null);
    }

    private void update(Integer account, String phone) {
        System.out.println(account);
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        ContentValues value = new ContentValues();
        value.put("account", account);
        db.update("profile", value, "phone = '" + phone +"'" , null);
    }
}