package com.mobileprogramming.leavenow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText et_id, et_pw;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        btn_submit = findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 쿼리 실행
                String userId = et_id.getText().toString().trim();
                String userPw = et_pw.getText().toString().trim();

                if (!userId.isEmpty() && !userPw.isEmpty()) {
                    String loginQuery = "SELECT name FROM user WHERE id = '" + userId + "' AND pw = '" + userPw + "'";

                    DatabaseQueryHelper dbHelper = new DatabaseQueryHelper(MainActivity.this);
                    dbHelper.executeQuery(loginQuery, new DatabaseQueryHelper.QueryCallback() {
                        @Override
                        public void onQuerySuccess(Object result) {
                            if (result instanceof JSONArray) {
                                // SELECT 쿼리 결과가 JSONArray인 경우
                                JSONArray data = (JSONArray) result;
                                try {
                                    if (data.length() > 0) {
                                        JSONObject user = data.getJSONObject(0);  // 첫 번째 사용자 정보
                                        String name = user.getString("name");
                                        Toast.makeText(MainActivity.this, "로그인 성공! 이름: " + name, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "로그인 실패: 잘못된 아이디 또는 비밀번호", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "응답 처리 중 오류 발생", Toast.LENGTH_SHORT).show();
                                }
                            } else if (result instanceof String) {
                                // INSERT, UPDATE, DELETE 등의 쿼리 결과가 String 메시지인 경우
                                String message = (String) result;
                                Toast.makeText(MainActivity.this, "결과: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onQueryError(String errorMessage) {
                            Toast.makeText(MainActivity.this, "에러 발생: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
