package com.mobileprogramming.leavenow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        tv_name.setText(MainActivity.NICKNAME + "님.");
        Button b = findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PlanViewActivity.class);
                startActivity(i);
            }
        });
    }
}