package com.mobileprogramming.leavenow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class HomeActivity extends AppCompatActivity {

    TextView tv_name;
    String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv_name = findViewById(R.id.tv_name);
        id = MainActivity.ID;

        // MainActivity에 입력된 ID를 이용하여 user의 name을 가져오는 구문
        String loginQuery = "SELECT nickname FROM user WHERE user_id = '" + id + "'";
        DB db = new DB(HomeActivity.this);
        tv_name.setText(MainActivity.NICKNAME + "님.");


        //네비게이션 바 기능
        LinearLayout nav_home, nav_trip, nav_diary, nav_community;

        nav_home = findViewById(R.id.nav_home);
        nav_trip = findViewById(R.id.nav_trip);
        nav_diary = findViewById(R.id.nav_diary);
        nav_community = findViewById(R.id.nav_community);

        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        /*
        nav_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TripActivity.class);
                startActivity(intent);
            }
        });
         */
        nav_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, diary.class);
                startActivity(intent);
            }
        });

        nav_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

    }

}