package com.mobileprogramming.leavenow;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatabaseQueryHelper {

    private Context context;

    // 생성자
    public DatabaseQueryHelper(Context context) {
        this.context = context;
    }

    public void executeQuery(String query, final QueryResponseListener listener) {
        String url = "http://104.154.220.216/execute_query1.php";  // 서버 URL

        // Volley 요청
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // JSON 응답 처리
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                if (jsonObject.has("data")) {
                                    // SELECT 쿼리 결과 처리
                                    JSONArray data = jsonObject.getJSONArray("data");
                                    listener.onQuerySuccess(data);  // 성공 시 콜백
                                } else {
                                    // INSERT, UPDATE, DELETE 성공 메시지 처리
                                    String message = jsonObject.getString("message");
                                    listener.onQuerySuccess(message);  // 성공 시 콜백
                                }
                            } else {
                                String message = jsonObject.getString("message");
                                listener.onQueryError(message);  // 실패 시 콜백
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onQueryError("응답 처리 중 오류 발생.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onQueryError("서버와의 통신 중 오류 발생.");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // POST 요청에 전달할 데이터 (쿼리문)
                Map<String, String> params = new HashMap<>();
                params.put("query", query);  // 쿼리문
                return params;
            }
        };

        // 요청 큘에 추가
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // 쿼리 실행 결과를 처리할 콜백 인터페이스
    public interface QueryResponseListener {
        void onQuerySuccess(Object data);  // 성공 시 호출
        void onQueryError(String errorMessage);  // 오류 발생 시 호출
    }
}
