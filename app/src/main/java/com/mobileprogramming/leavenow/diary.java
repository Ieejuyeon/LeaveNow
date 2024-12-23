package com.mobileprogramming.leavenow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class diary extends AppCompatActivity implements DiaryAdapter.OnDiaryInteractionListener {

    private RecyclerView recyclerView;
    private DiaryAdapter adapter;
    private DiaryDatabaseManager dbManager;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);

        dbManager = new DiaryDatabaseManager(this);
        dbManager.open();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadDiaries();

        addButton = findViewById(R.id.btn_add);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(diary.this, NewDiaryActivity.class);
            startActivityForResult(intent, 1);
        });

        //네비게이션 바 기능
        LinearLayout nav_home, nav_trip, nav_diary, nav_community;

        nav_home = findViewById(R.id.nav_home);
        nav_trip = findViewById(R.id.nav_trip);
        nav_diary = findViewById(R.id.nav_diary);
        nav_community = findViewById(R.id.nav_community);

        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(diary.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        nav_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(diary.this, PlanViewActivity.class);
                startActivity(intent);
            }
        });

        nav_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(diary.this, diary.class);
                startActivity(intent);
            }
        });

        nav_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(diary.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadDiaries() {
        List<DiaryItem> diaryList = dbManager.getAllDiaries();
        adapter = new DiaryAdapter(diaryList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDeleteClicked(DiaryItem diary) {
        dbManager.deleteDiary(diary.getId());
        loadDiaries();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadDiaries();
        }
    }
}
