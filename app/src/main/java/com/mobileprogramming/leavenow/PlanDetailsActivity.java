package com.mobileprogramming.leavenow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class PlanDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);

        TextView title_plan_detail = findViewById(R.id.title_plan_detail);
        TextView date_plan_detail = findViewById(R.id.date_plan_detail);
        TextView content_plan_detail = findViewById(R.id.content_plan_detail);
        int plan_id = getIntent().getIntExtra("plan_id", 0);
        title_plan_detail.setText(getIntent().getStringExtra("plan_title"));
        content_plan_detail.setText(getIntent().getStringExtra("plan_content"));
        date_plan_detail.setText(getIntent().getStringExtra("plan_date"));


        // TabHost 설정
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        // 탭 추가
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Day 1");
        tabSpec1.setIndicator("Day 1");
        tabSpec1.setContent(R.id.day1);
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Day 2");
        tabSpec2.setIndicator("Day 2");
        tabSpec2.setContent(R.id.day2);
        tabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("Day 3");
        tabSpec3.setIndicator("Day 3");
        tabSpec3.setContent(R.id.day3);
        tabHost.addTab(tabSpec3);

        TabHost.TabSpec tabSpec4 = tabHost.newTabSpec("Day 4");
        tabSpec4.setIndicator("Day 4");
        tabSpec4.setContent(R.id.day4);
        tabHost.addTab(tabSpec4);

        TabHost.TabSpec tabSpec5 = tabHost.newTabSpec("Day 5");
        tabSpec5.setIndicator("Day 5");
        tabSpec5.setContent(R.id.day5);
        tabHost.addTab(tabSpec5);


        // 각 ListView에 대한 설정
        setupListView(R.id.day1);
        setupListView(R.id.day2);
        setupListView(R.id.day3);
        setupListView(R.id.day4);
        setupListView(R.id.day5);

        // TabHost 기본 탭 설정
        tabHost.setCurrentTab(0);

     }

    private void setupListView(int listViewId) {
        ListView listView = findViewById(listViewId);
        // ListView에 Adapter 설정, 필요 시 Adapter를 구현해 넣으세요.
        // 예시: listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, yourDataList));
    }

}