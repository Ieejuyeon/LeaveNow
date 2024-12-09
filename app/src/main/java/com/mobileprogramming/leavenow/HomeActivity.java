package com.mobileprogramming.leavenow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    }
}