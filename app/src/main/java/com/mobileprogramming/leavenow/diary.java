package com.mobileprogramming.leavenow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
