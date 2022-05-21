package com.example.projectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import model.HistoryEntity;
import model.adapter.HistoryAdapter;

public class HonePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hone_page);
//        List<HistoryEntity> listHistory = new ArrayList<>();
//        listHistory.add(new HistoryEntity("tesst", 1003515122, "2020/05/08"));
//        HistoryAdapter historyAdapter =new HistoryAdapter(this, listHistory);
//        ListView listView = findViewById(R.id.home_page_list);
//        listView.setAdapter(historyAdapter);
    }
}