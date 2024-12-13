package com.mobileprogramming.leavenow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlanViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<Plan> plans = new ArrayList<>();

        planAdapter pa = new planAdapter(this, plans);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_view);

        DB db = new DB(PlanViewActivity.this);
        String query = "SELECT plan_id, plan_name, contents, start_date, end_date from leavenow.plan where user_id = " + MainActivity.ID;
        db.executeQuery(query, new DB.QueryResponseListener() {
            @Override
            public void onQuerySuccess(Object data) {
                JSONArray ja = (JSONArray) data;
                try {
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        String title = jo.getString("plan_name");
                        String date = jo.getString("start_date") + " ~ " + jo.getString("end_date");
                        String contents = jo.getString("contents");
                        int id = jo.getInt("plan_id");
                        plans.add(new Plan(id, title, contents, date));
                        Toast.makeText(getApplicationContext(), "query successed" , Toast.LENGTH_LONG).show();
                    }
                    runOnUiThread(() -> {
                        pa.notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onQueryError(String errorMessage) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        ListView lv = (findViewById(R.id.lv_plan));
        lv.setAdapter(pa);

        ImageButton btn_newplan = (findViewById(R.id.btn_newplan));
        btn_newplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PlanCreateActivity.class));
            }
        });
    }

    static class planAdapter extends ArrayAdapter<Plan> {

        public planAdapter(Context c, List<Plan> plans) {
            super(c, 0, plans);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.plan, parent, false);
            }
//            LayoutInflater lif = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View plan = lif.inflate(R.layout.plan, parent, false);

            TextView tp = convertView.findViewById(R.id.title_plan);
            TextView dp = convertView.findViewById(R.id.date_plan);
            TextView cp = convertView.findViewById(R.id.content_plan);
            LinearLayout ll = convertView.findViewById(R.id.ll_plan);

            if (position % 2 == 1) {
                ll.setBackgroundResource(R.drawable.plan_item2);
            }

            Plan plan = getItem(position);
            if(plan!=null) {
                tp.setText(plan.title);
                dp.setText(plan.date);
                cp.setText(plan.contents);

                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return convertView;
        }
    }
}