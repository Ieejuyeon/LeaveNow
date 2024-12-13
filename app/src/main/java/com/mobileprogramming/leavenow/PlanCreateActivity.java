package com.mobileprogramming.leavenow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class PlanCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_create);

        EditText title = findViewById(R.id.title_plancreate);
        ImageButton calstart = findViewById(R.id.calstart_plancreate);
        ImageButton calend = findViewById(R.id.calend_plancreate);
        TextView datestart = findViewById(R.id.datestart_plancreate);
        TextView dateend = findViewById(R.id.dateend_plancreate);
        CheckBox cb_endnull = findViewById(R.id.cb_endnull);
        EditText content = findViewById(R.id.content_plancreate);
        Button btn_create = findViewById(R.id.btn_create_plancreate);
        Button btn_cancel = findViewById(R.id.btn_cancel_plancreate);

        calstart.setOnClickListener(view -> showDatePickerDialog((year, month, day) -> {
            datestart.setText(editDate(year, month, day));
        }));
        calend.setOnClickListener(view -> showDatePickerDialog((year, month, day) -> {
            dateend.setText(editDate(year, month, day));
        }));

        cb_endnull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_endnull.isChecked()) {
                    dateend.setVisibility(View.INVISIBLE);
                    calend.setVisibility(View.INVISIBLE);
                } else {
                    dateend.setVisibility(View.VISIBLE);
                    calend.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String ds = datestart.getText().toString();
                String de;
                if(cb_endnull.isChecked()){
                    de = "";
                }
                else de = dateend.getText().toString();
                String ct = content.getText().toString();

                if(!dateValidate(ds,de)){
                    Toast.makeText(getApplicationContext(), "날짜가 잘못 작성되었습니다." , Toast.LENGTH_SHORT);
                }
                else if(!t.equals("") && !ds.equals("") && !ct.equals("")) {
                    String query = "insert into plan (plan_name, contents, start_date, end_date, user_id) values ('" + t + "', '" + ct + "', '" + ds + "', '" + de + "', '" + MainActivity.ID + "' )";
                    DB db = new DB(PlanCreateActivity.this);
                    db.executeQuery(query, new DB.QueryResponseListener() {
                        @Override
                        public void onQuerySuccess(Object data) {
                            Toast.makeText(getApplicationContext(), "plan updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), PlanViewActivity.class));
                        }

                        @Override
                        public void onQueryError(String errorMessage) {
                            Toast.makeText(getApplicationContext(), "오류발생 : " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else Toast.makeText(getApplicationContext(), "공백란이 있습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean dateValidate (String datestart, String dateend){
        String[] a = datestart.split("/");
        String[] b = dateend.split("/");
        for(int i = 0; i < 3; i++){
            if(Integer.parseInt(a[i]) > Integer.parseInt(b[i])) {return false;}
        }
        return true;
    }
    private String editDate(int year, int month, int day) {
        String y = year % 100 + "";
        String m = (month + 1) + "";
        String d = day + "";
        if (year % 100 == year % 10) {
            y = "0" + y;
        }
        if (month % 10 == month) {
            m = "0" + m;
        }
        if (day % 10 == day) {
            d = "0" + d;
        }
        return y + "/" + m + "/" + d;
    }

    private void showDatePickerDialog(OnDateSelectedListener onDateSelected) {
        // 현재 날짜 가져오기
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog 생성
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    if (onDateSelected != null) {
                        onDateSelected.onDateSelected(selectedYear, selectedMonth, selectedDay);
                    }
                },
                year, month, day
        );

        // DatePickerDialog 표시
        datePickerDialog.show();
    }

    interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day);
    }
}