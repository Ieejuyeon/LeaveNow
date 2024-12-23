package com.mobileprogramming.leavenow;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PostCreateActivity extends AppCompatActivity {
    private EditText etTitle, etContent;
    private Button btnAttachment, btnSave;
    private String attachmentPath = null;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);
        id = MainActivity.ID;

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnAttachment = findViewById(R.id.btnAttachment);
        btnSave = findViewById(R.id.btnSave);


        // 첨부파일 버튼 클릭
        btnAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 파일 선택을 위한 Intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });

        // 저장 버튼 클릭
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();

                if (title.isEmpty() || content.isEmpty() || attachmentPath.isEmpty()) {
                    Toast.makeText(PostCreateActivity.this, "모든 항목을 채워주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // DB에 데이터 저장
                savePostToDatabase(title, content, attachmentPath);
            }
        });
    }

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
    // 파일 선택 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                // 내부 저장소에 파일 저장
                InputStream inputStream = getContentResolver().openInputStream(uri);
                String fileName = "attachment_" + System.currentTimeMillis() + ".jpg";
                File file = new File(getFilesDir(), fileName);
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                // 파일 경로 저장
                attachmentPath = file.getAbsolutePath();
                Toast.makeText(this, "파일 저장 완료: " + attachmentPath, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "파일 저장 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 게시물 데이터 저장 로직
    private void savePostToDatabase(String title, String content, String attachment) {
        String query = "INSERT INTO community (post_id, title, content, attachment, create_at, user_id) " +
                "VALUES (NULL, '" + title + "', '" + content + "', '" + attachment + "', NOW(), " + id + ");";

        DB db = new DB(this);
        db.executeQuery(query, new DB.QueryResponseListener() {
            @Override
            public void onQuerySuccess(Object data) {
                Toast.makeText(PostCreateActivity.this, "작성 완료!", Toast.LENGTH_SHORT).show();
                finish(); // 화면 종료
            }

            @Override
            public void onQueryError(String errorMessage) {
                Toast.makeText(PostCreateActivity.this, "작성 실패: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

        });
    }
}
