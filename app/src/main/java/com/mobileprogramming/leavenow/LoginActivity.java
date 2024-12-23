package com.mobileprogramming.leavenow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_pw;
    private Button btn_submit;
    private Button btn_back;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_result = findViewById(R.id.tv_result);
        btn_submit = findViewById(R.id.btn_submit);
        btn_back = findViewById(R.id.btn_back);
        et_pw = findViewById(R.id.et_pw);
        et_email = findViewById(R.id.et_id);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = et_email.getText().toString().trim();
                String userPw = et_pw.getText().toString().trim();

                if (!userEmail.isEmpty() && !userPw.isEmpty()) {

                    // DB 사용 부분 !!!!

                    // 쿼리 지정, DB객체생성 (DB(context))
                    String loginQuery = "SELECT nickname, user_id FROM user WHERE email = '" + userEmail + "' AND password = '" + userPw + "'";
                    DB db = new DB(LoginActivity.this);

                    // executeQuery : 쿼리 보내는 함수, 파라미터는 ("쿼리문", QueryResponseListener 인터페이스 객체 )
                    db.executeQuery(loginQuery, new DB.QueryResponseListener() {

                        // QueryResponseListener을 익명객체로 사용하였고, 밑에서 필요한 추상메서드(onQuerySuccess와 onQueryError)를 구현하는것
                        @Override
                        //성공했을 때 실행될 함수
                        public void onQuerySuccess(Object data) {
                            // 응답받은 데이터를 처리 (위의 data는 쿼리가 SELECT문일때는 JSONArray객체, 그 외 INSERT 등은 문자열("success")형태임)
                            // SELECT 결과 (JsonArray) 처리
                            JSONArray jsonArray = (JSONArray) data;
                            try {
                                // 로그인 성공 시 static 변수에 아이디 및 닉네임 저장
                                MainActivity.ID = jsonArray.getJSONObject(0).getString("user_id");  // 첫 번째 행에서 이름 가져오기
                                MainActivity.NICKNAME = jsonArray.getJSONObject(0).getString("nickname");  // 첫 번째 행에서 이름 가져오기
                                tv_result.setText("로그인 성공" + "\n아이디: " + MainActivity.ID);  // 화면에 출력
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                tv_result.setText(e.toString());
                            }
                        }

                        //실패했을 때 실행될 함수, errorMessage를 갖고있음.
                        @Override
                        public void onQueryError(String errorMessage) {
                            tv_result.setText("오류: " + errorMessage);
                        }

                    });
                    //여기까지 DB부분


                } else {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}