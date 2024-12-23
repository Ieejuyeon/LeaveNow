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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class NewDiaryActivity extends AppCompatActivity {

    private EditText etDiaryTitle, etDiaryContent;
    private TextView tvDiaryDate;
    private RatingBar rbMood;
    private DiaryDatabaseManager diaryDatabaseManager;
    private List<Uri> attachmentUris = new ArrayList<>();
    private LinearLayout llAttachmentsContainer;
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
        setContentView(R.layout.activity_new_diary);

        etDiaryTitle = findViewById(R.id.et_diary_title);
        etDiaryContent = findViewById(R.id.et_diary_content);
        tvDiaryDate = findViewById(R.id.tv_diary_date);
        rbMood = findViewById(R.id.rb_mood);
        llAttachmentsContainer = findViewById(R.id.ll_attachments_container);

        diaryDatabaseManager = new DiaryDatabaseManager(this);

        // 현재 날짜 자동 입력
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvDiaryDate.setText("작성 일자: " + currentDate);

        diaryDatabaseManager.open();

        // 파일 선택 버튼 동작
        Button btnAddAttachment = findViewById(R.id.btn_attach_file);
        btnAddAttachment.setOnClickListener(v -> openFilePicker());

        // 저장 버튼 클릭 시 처리
        findViewById(R.id.btn_save_diary).setOnClickListener(v -> saveDiary());
    }

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    if (result.getData().getClipData() != null) {
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri fileUri = result.getData().getClipData().getItemAt(i).getUri();
                            addFileToLayout(fileUri);
                            attachmentUris.add(fileUri);
                        }
                    } else if (result.getData().getData() != null) {
                        Uri fileUri = result.getData().getData();
                        addFileToLayout(fileUri);
                        attachmentUris.add(fileUri);
                    }
                }
            }
    );

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filePickerLauncher.launch(intent);
    }

    private void addFileToLayout(Uri fileUri) {
        // 이미 URI가 attachmentUris에 존재하면 추가하지 않음
        if (attachmentUris.contains(fileUri)) {
            Log.d("DuplicateUri", "이미 추가된 URI: " + fileUri.toString());
            return; // 중복 URI는 추가하지 않음
        }

        Log.d("FileAdded", "File URI: " + fileUri.toString()); // 로그로 경로 확인

        // 나머지 코드
        View fileItem = LayoutInflater.from(this).inflate(R.layout.item_file, llAttachmentsContainer, false);
        TextView tvFileName = fileItem.findViewById(R.id.tv_file_name);
        ImageView ivFileIcon = fileItem.findViewById(R.id.iv_file_icon);

        String fileName = getFileName(fileUri);
        if (fileName != null) {
            tvFileName.setText(fileName);
        } else {
            tvFileName.setText("알 수 없는 파일");
        }

        // 이미지 파일인 경우 내부 저장소에 저장
        if (isImageFile(fileUri)) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // 파일 이름 생성
                    String savedFileName = "attachment_" + System.currentTimeMillis() + ".jpg";
                    File file = new File(getFilesDir(), savedFileName);

                    // Bitmap을 파일로 저장
                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        Toast.makeText(this, "이미지 저장 완료: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                        // 파일 경로만 'file://'로 저장
                        Uri savedUri = Uri.fromFile(file);
                        if (!attachmentUris.contains(savedUri)) {
                            attachmentUris.add(savedUri); // 중복 체크 후 추가
                        }

                        // 이미지 뷰에 미리 보기 표시
                        ivFileIcon.setImageBitmap(bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                ivFileIcon.setImageResource(R.drawable.ic_launcher_background); // 에러 시 기본 이미지
                Toast.makeText(this, "이미지 저장 실패", Toast.LENGTH_SHORT).show();
            }
        } else {
            ivFileIcon.setVisibility(View.GONE); // 이미지가 아닌 경우 아이콘 숨기기
        }

        llAttachmentsContainer.addView(fileItem);
    }

    // 이미지 파일인지 검사하는 메서드 (확장자 검사)
    private boolean isImageFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("image/");
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    for (String column : cursor.getColumnNames()) {
                        Log.d("CursorColumn", column);
                    }
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void saveDiary() {
        String title = etDiaryTitle.getText().toString().trim();
        String content = etDiaryContent.getText().toString().trim();
        float mood = rbMood.getRating();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "모든 정보를 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        StringBuilder filePaths = new StringBuilder();

        for (Uri uri : attachmentUris) {
            // 경로 중복 확인 후 저장
            if (!filePaths.toString().contains(uri.toString())) {
                filePaths.append(uri.toString()).append(";");
            }
        }

        long rowId = diaryDatabaseManager.addDiary(title, content, timestamp, filePaths.toString(), mood);

        if (rowId != -1) {
            Toast.makeText(this, "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "일기 저장 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}
