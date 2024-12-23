package com.mobileprogramming.leavenow;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class PlanDetailsCreateActivity extends AppCompatActivity {

    private TextView timeStartTextView;
    private TextView timeEndTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details_create);

        Intent i = getIntent();
        int plan_id = i.getIntExtra("plan_id",0);
        String plan_title = i.getStringExtra("plan_title");
        String plan_date = i.getStringExtra("plan_date");
        String plan_content = i.getStringExtra("plan_content");
        TextView title = findViewById(R.id.title_plandetail_create);
        Button btn_create = findViewById(R.id.btn_create_plandetail_create);
        Button btn_cancel = findViewById(R.id.btn_cancel_plandetail_create);
        TextView visit_date = findViewById(R.id.date_plandetail_create);
        timeStartTextView = findViewById(R.id.timestart_plandetail_create);
        timeEndTextView = findViewById(R.id.timeend_plandetail_create);
        ImageButton calButton = findViewById(R.id.cal_plandetail_create);
        ImageButton calStartButton = findViewById(R.id.calstart_plandetail_create);
        ImageButton calEndButton = findViewById(R.id.calend_plandetail_create);



        calButton.setOnClickListener(view -> showDatePickerDialog((year, month, day) -> {
            visit_date.setText(editDate(year, month, day));
        }));
        // 시작 시간 설정 버튼 클릭 리스너
        calStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(timeStartTextView);
            }
        });

        // 종료 시간 설정 버튼 클릭 리스너
        calEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(timeEndTextView);
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String vd = visit_date.getText().toString();
                String st = timeStartTextView.getText().toString();
                String et = timeEndTextView.getText().toString();
                String query = "insert into plan_details (plan_id, place_name, visit_date, start_time, end_time) values ('" + plan_id + "' , '" + t + "', '" + vd + "', '" + st + "', '" + et + "')";
                DB db = new DB(PlanDetailsCreateActivity.this);
                db.executeQuery(query, new DB.QueryResponseListener() {
                    @Override
                    public void onQuerySuccess(Object data) {
                        Toast.makeText(getApplicationContext(), "plan detail updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), PlanDetailsActivity.class);
                        i.putExtra("plan_id",plan_id);
                        i.putExtra("plan_title", plan_title);
                        i.putExtra("plan_date", plan_date);
                        i.putExtra("plan_content", plan_content);
                        startActivity(i);
                    }

                    @Override
                    public void onQueryError(String errorMessage) {
                        Toast.makeText(getApplicationContext(), "오류발생 : " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PlanDetailsActivity.class);
                i.putExtra("plan_id",plan_id);
                i.putExtra("plan_title", plan_title);
                i.putExtra("plan_date", plan_date);
                i.putExtra("plan_content", plan_content);
                startActivity(i);
            }
        });

    }

    /**
     * TimePickerDialog를 표시하고 선택된 시간을 TextView에 설정합니다.
     *
     * @param targetTextView 시간을 표시할 TextView
     */
    private void showTimePickerDialog(TextView targetTextView) {
        // 현재 시간 가져오기
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // TimePickerDialog 생성
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    // 선택된 시간을 HH:mm 형식으로 설정
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    targetTextView.setText(time);
                },
                hour,
                minute,
                true // 24시간 형식 사용
        );

        timePickerDialog.show();
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
    private void showDatePickerDialog(PlanCreateActivity.OnDateSelectedListener onDateSelected) {
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
