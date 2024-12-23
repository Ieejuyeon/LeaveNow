package com.mobileprogramming.leavenow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanDetailsActivity extends AppCompatActivity {

    private int plan_id; // 클래스 필드로 선언
    private String plan_title;
    private String plan_content;
    private String plan_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);


        Button btn_create = findViewById(R.id.btn_create_plandetail);
        plan_id = getIntent().getIntExtra("plan_id", 0);
        plan_title = getIntent().getStringExtra("plan_title");
        plan_content = getIntent().getStringExtra("plan_content");
        plan_date = getIntent().getStringExtra("plan_date");
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PlanDetailsCreateActivity.class);
                i.putExtra("plan_id",plan_id);
                i.putExtra("plan_content", plan_content);
                i.putExtra("plan_date", plan_date);
                i.putExtra("plan_title", plan_title);
                startActivity(i);
                }
        });
        // TabHost 설정
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        // 기본 탭 추가
        for (int i = 0; i < 5; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec("Day " + (i + 1));
            tabSpec.setIndicator("Day " + (i + 1));
            tabSpec.setContent(getResources().getIdentifier("day" + (i + 1), "id", getPackageName()));
            tabHost.addTab(tabSpec);
        }

        // 기본 탭 설정
        tabHost.setCurrentTab(0);

        // 제목, 내용, 날짜 설정
        TextView title_plan_detail = findViewById(R.id.title_plan_detail);
        TextView date_plan_detail = findViewById(R.id.date_plan_detail);
        TextView content_plan_detail = findViewById(R.id.content_plan_detail);

        title_plan_detail.setText(plan_title);
        content_plan_detail.setText(plan_content);
        date_plan_detail.setText(plan_date);

        // 데이터 로드 및 탭 업데이트
        loadDataAndSetupTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menuback){
            startActivity(new Intent(getApplicationContext(), PlanViewActivity.class));
        };
        return super.onOptionsItemSelected(item);
    }

    private void loadDataAndSetupTabs() {
        DB db = new DB(this);
        String query = "SELECT visit_date, place_name, start_time, end_time FROM plan_details WHERE plan_id = " + plan_id + " ORDER BY visit_date ASC";

        db.executeQuery(query, new DB.QueryResponseListener() {
            @Override
            public void onQuerySuccess(Object data) {
                try {
                    JSONArray ja = (JSONArray) data;

                    // 날짜별 PlanDetail 저장
                    Map<String, List<PlanDetail>> dateDetails = new HashMap<>();

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        String visitDate = jo.getString("visit_date");
                        String placeName = jo.getString("place_name");
                        String startTime = jo.getString("start_time");
                        String endTime = jo.getString("end_time");

                        PlanDetail detail = new PlanDetail(visitDate, placeName, startTime, endTime);
                        if (!dateDetails.containsKey(visitDate)) {
                            dateDetails.put(visitDate, new ArrayList<>());
                        }
                        dateDetails.get(visitDate).add(detail);
                    }

                    // UI 업데이트
                    runOnUiThread(() -> {
                        List<String> sortedDates = new ArrayList<>(dateDetails.keySet());
                        Collections.sort(sortedDates); // 날짜 순 정렬

                        TabHost tabHost = findViewById(android.R.id.tabhost);
                        TabWidget tabWidget =findViewById(android.R.id.tabs);
                        int tabIndex = 0;

                        for(int i = 0; i< 5; i++){
                            tabWidget.getChildAt(i).setVisibility(View.GONE);
                        }
                        for (String date : sortedDates) {
                            if (tabIndex >= 5) break; // 최대 5일 표시
                            tabWidget.getChildAt(tabIndex).setVisibility(View.VISIBLE);
                            int listViewId = getResources().getIdentifier("day" + (tabIndex + 1), "id", getPackageName());
                            setupListView(listViewId, dateDetails.get(date));

                            tabIndex++;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onQueryError(String errorMessage) {
                // 오류 처리
            }
        });
    }

    private void setupListView(int listViewId, List<PlanDetail> details) {
        ListView listView = findViewById(listViewId);

        if (details != null && !details.isEmpty()) {
            PlanDetailAdapter adapter = new PlanDetailAdapter(details);
            listView.setAdapter(adapter);
        } else {
            listView.setAdapter(null); // 데이터가 없으면 빈 상태로 설정
        }
    }

    // 내부 클래스: PlanDetailAdapter
    private class PlanDetailAdapter extends BaseAdapter {
        private List<PlanDetail> details;

        public PlanDetailAdapter(List<PlanDetail> details) {
            this.details = details;
        }

        @Override
        public int getCount() {
            return details.size();
        }

        @Override
        public PlanDetail getItem(int position) {
            return details.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(PlanDetailsActivity.this).inflate(R.layout.plan_details, parent, false);
            }

            TextView title = convertView.findViewById(R.id.title_plan_details);
            TextView st = convertView.findViewById(R.id.start_time);
            TextView et = convertView.findViewById(R.id.end_time);

            PlanDetail detail = getItem(position);
            title.setText(detail.placeName);
            st.setText(detail.startTime);
            et.setText(detail.startTime);

            return convertView;
        }
    }
}
