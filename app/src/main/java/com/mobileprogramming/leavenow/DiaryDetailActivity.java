package com.mobileprogramming.leavenow;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.InputStream;

public class DiaryDetailActivity extends AppCompatActivity {

    private TextView tvDate;
    private EditText tvTitle, etContent;
    private RatingBar rbMood;
    private ImageView ivImage;
    private Button btnSave;

    private long diaryId;
    private String diaryImageUrl;

    private DiaryDatabaseManager dbManager;

    private LinearLayout llAttachmentsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_diary);

        // 뷰 초기화
        tvTitle = findViewById(R.id.et_diary_title1);
        tvDate = findViewById(R.id.tv_diary_date1);
        etContent = findViewById(R.id.et_diary_content1);
        rbMood = findViewById(R.id.rb_mood1);
        btnSave = findViewById(R.id.btn_save_diary1);
        llAttachmentsContainer = findViewById(R.id.ll_attachments_container);

        // 데이터베이스 관리 객체 초기화
        dbManager = new DiaryDatabaseManager(this);

        // 인텐트에서 데이터 받기
        Intent intent = getIntent();
        diaryId = intent.getLongExtra("diary_id", -1);

        // 일기 데이터 로드
        loadDiaryDetails();

        // 수정 버튼 클릭 시 일기 내용 수정
        btnSave.setOnClickListener(v -> {
            String title = tvTitle.getText().toString();
            String updatedContent = etContent.getText().toString();
            int moodRating = (int) rbMood.getRating();

            // 수정된 내용 저장
            updateDiaryContent(title, updatedContent, moodRating);

            // 수정 후 액티비티 종료
            finish();
        });
    }

    // 일기 내용 로드
    private void loadDiaryDetails() {
        dbManager.open();

        DiaryItem diaryItem = dbManager.getDiaryById(diaryId);

        if (diaryItem != null) {
            tvTitle.setText(diaryItem.getTitle());
            tvDate.setText(diaryItem.getTimestamp());
            etContent.setText(diaryItem.getContent());
            rbMood.setRating(Float.parseFloat(diaryItem.getMood()));

            String imagePaths = diaryItem.getImageUrl();

            if (imagePaths != null && !imagePaths.isEmpty()) {
                String[] paths = imagePaths.split(";");
                for (String path : paths) {
                    Log.d("ImagePath", "Loading path: " + path);
                    try {
                        Uri fileUri = Uri.parse(path);
                        // "file://"로 시작하는 경로만 처리
                        if (fileUri.getScheme() != null && fileUri.getScheme().equals("file")) {
                            addFileToLayout(fileUri);
                        }
                    } catch (Exception e) {
                        Log.e("ImagePath", "Failed to load Uri: " + path, e);
                    }
                }
            }
        }

        dbManager.close();
    }


    private void addFileToLayout(Uri fileUri) {
        View fileItem = LayoutInflater.from(this).inflate(R.layout.item_file, llAttachmentsContainer, false);
        TextView tvFileName = fileItem.findViewById(R.id.tv_file_name);
        ImageView ivFileIcon = fileItem.findViewById(R.id.iv_file_icon);

        String fileName = getFileName(fileUri);
        if (fileName != null) {
            tvFileName.setText(fileName);
        } else {
            tvFileName.setText("알 수 없는 파일");
        }

        try {
            if ("content".equals(fileUri.getScheme())) {
                // content:// URI 처리
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ivFileIcon.setImageBitmap(bitmap);
                    inputStream.close();
                }
            } else if ("file".equals(fileUri.getScheme())) {
                // file:// URI 처리
                ivFileIcon.setImageBitmap(BitmapFactory.decodeFile(new File(fileUri.getPath()).getAbsolutePath()));
            } else {
                ivFileIcon.setImageResource(R.drawable.ic_launcher_background); // 에러 시 기본 이미지
            }
        } catch (Exception e) {
            e.printStackTrace();
            ivFileIcon.setImageResource(R.drawable.ic_launcher_background); // 에러 시 기본 이미지
        }

        llAttachmentsContainer.addView(fileItem);
    }



    // 이미지 파일인지 검사하는 메서드
    private boolean isImageFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("image/");
    }

    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    // 일기 내용 수정
    private void updateDiaryContent(String title, String content, int moodRating) {
        dbManager.open();

        // 수정된 내용과 이미지 경로 저장
        dbManager.updateDiary(diaryId, title, content, moodRating);

        dbManager.close();
    }
}
