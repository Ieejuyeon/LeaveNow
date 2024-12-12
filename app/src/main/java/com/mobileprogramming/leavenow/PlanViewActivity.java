package com.mobileprogramming.leavenow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PlanViewActivity extends AppCompatActivity {

    String title[] = {"강릉", "제주"};
    String date[] = {"10.08~10.09","01.02~01.04"};
    String contents[] = {"친구들과", "새해여행"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_view);

        ListView lv = (findViewById(R.id.lv_plan));
        planAdapter pa = new planAdapter(this, title, date, contents);
        lv.setAdapter(pa);

        ImageButton btn_newplan = (findViewById(R.id.btn_newplan));
        btn_newplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PlanCreateActivity.class));
            }
        });
    }

    class planAdapter extends ArrayAdapter<String>{
        Context context;
        String title[];
        String date[];
        String contents[];

        planAdapter(Context c, String title[], String date[], String contents[]){
            super(c, R.layout.plan,R.id.title_plan,title);
            this.context=c;
            this.title = title;
            this.date = date;
            this.contents = contents;
        }
    @Override
        public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent){
        LayoutInflater lif = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View plan = lif.inflate(R.layout.plan,parent,false);

        TextView tp = plan.findViewById(R.id.title_plan);
        TextView dp = plan.findViewById(R.id.date_plan);
        TextView cp = plan.findViewById(R.id.content_plan);
        LinearLayout ll = plan.findViewById(R.id.ll_plan);

        if(position%2==1){ll.setBackgroundResource(R.drawable.plan_item2);}

        tp.setText(title[position]);
        dp.setText(date[position]);
        cp.setText(contents[position]);

        return plan;
    }
    }
}