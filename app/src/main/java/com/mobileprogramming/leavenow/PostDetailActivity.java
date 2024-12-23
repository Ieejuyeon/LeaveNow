package com.mobileprogramming.leavenow;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class PostDetailActivity extends AppCompatActivity {

    private TextView titleView, contentView, authorView, dateView;
    private ImageView attachmentView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menuback){
            finish();
        };
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        titleView = findViewById(R.id.detailTitle);
        contentView = findViewById(R.id.detailContent);
        authorView = findViewById(R.id.detailAuthor);
        dateView = findViewById(R.id.detailDate);
        attachmentView = findViewById(R.id.detailAttachment);


        // Intent로 전달된 데이터 가져오기
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String author = getIntent().getStringExtra("author");
        String date = getIntent().getStringExtra("date");
        String attachment = getIntent().getStringExtra("attachment");

        // 데이터를 View에 설정
        titleView.setText(title);
        contentView.setText(content);
        authorView.setText(author);
        dateView.setText(date);

        // 이미지 파일 로드
        if (attachment != null) {
            File imgFile = new File(attachment);
            if (imgFile.exists()) {
                attachmentView.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            }
        }
    }
}
