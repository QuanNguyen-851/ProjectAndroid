package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ExecuteSQLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_sqlactivity);
        Integer test = R.drawable.ic_baseline_account_circle_24;
        EditText sql = findViewById(R.id.querySql);
        TextView resultTxt = findViewById(R.id.queryResult);
        Button button = findViewById(R.id.querySqlBtn);
        SQLiteDatabase db = openOrCreateDatabase("projectAndroid", MODE_PRIVATE, null);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor rs = db.rawQuery(sql.getText().toString(), null);
                rs.moveToFirst();
                StringBuilder result = new StringBuilder();
                while (!rs.isAfterLast()) {
                    result.append(rs.getString(0)+" | "+ rs.getString(1) + "\n");
                    rs.moveToNext();
                }
                resultTxt.setText(result);
            }
        });

        Button profile = findViewById(R.id.tb_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String drop = "drop table IF EXISTS profile";
                db.execSQL(drop);
                String executeQuery = "create table IF NOT EXISTS profile (id integer primary key AUTOINCREMENT," +
                        " name varchar(255)," +
                        " phone varchar(12)," +
                        " password varchar(50)," +
                        " gender varchar(10)," +
                        " address text, " +
                        " dateOfBirth varchar(50), " +
                        " job text, " +
                        " account bigint"+
                        " )";

                db.execSQL(executeQuery);
                Toast.makeText(ExecuteSQLActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tb_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String executeQuery = "drop table IF EXISTS  category";
                db.execSQL(executeQuery);
                 executeQuery = "create table IF NOT EXISTS category (id integer primary key AUTOINCREMENT," +
                        " name varchar(255)" +
                        " )";
                db.execSQL(executeQuery);
                executeQuery = "INSERT INTO category (name)  \n" +
                        "VALUES ('Ăn uống')," +
                        "('Trả nợ')," +
                        "('Đi chợ')," +
                        "('Mua sắm quần áo')," +
                        "('Đóng học')";
                db.execSQL(executeQuery);
                Toast.makeText(ExecuteSQLActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tb_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String executeQuery = "drop table IF EXISTS history";
                db.execSQL(executeQuery);
                executeQuery = "create table IF NOT EXISTS history (id integer primary key AUTOINCREMENT," +
                        " categoryId integer, " +
                        " profileId integer, " +
                        " payment bigint, " +
                        " description text, " +
                        " date varchar(255)" +
                        " )";
                db.execSQL(executeQuery);
                executeQuery = "INSERT INTO history (categoryId, profileId, payment, description, date)  \n" +
                        "VALUES (1 ,1, 11111156, 'tesst', '2022-05-12')," +
                        "(2 ,1, 11111156, 'tesst', '2022-05-12')," +
                        "(3 ,1, 11111444, 'tesst', '2022-05-12')";
                db.execSQL(executeQuery);
                Toast.makeText(ExecuteSQLActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
            }
        });



    }
}