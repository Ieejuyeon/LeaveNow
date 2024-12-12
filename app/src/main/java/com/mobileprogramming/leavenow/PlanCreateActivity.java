package com.mobileprogramming.leavenow;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlanCreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_create);

        EditText title = findViewById(R.id.title_plancreate);
        ImageButton calstart = findViewById(R.id.calstart_plancreate);
        ImageButton calEnd = findViewById(R.id.calend_plancreate);
        TextView datestart = findViewById(R.id.datestart_plancreate);
        TextView dateend = findViewById(R.id.dateend_plancreate);
        CheckBox cb_endnull = findViewById(R.id.cb_endnull);
        EditText content = findViewById(R.id.content_plancreate);
        Button btn_create = findViewById(R.id.btn_create_plancreate);
        Button btn_cancel = findViewById(R.id.btn_cancel_plancreate);

    }
}