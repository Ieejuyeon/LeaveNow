package com.mobileprogramming.leavenow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private GridView gridView;
    private CommunityAdapter adapter;
    private List<CommunityPost> postList;
    private DB db;
    private FloatingActionButton fab;
    private TextView emptyView;
    private ImageView searchIcon;
    private EditText searchInput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        gridView = findViewById(R.id.gridview);
        emptyView = findViewById(R.id.emptyView);
        postList = new ArrayList<>();
        adapter = new CommunityAdapter(this, postList);
        gridView.setAdapter((ListAdapter) adapter);

        db = new DB(this);

        searchIcon = findViewById(R.id.searchIcon);
        searchInput = findViewById(R.id.searchInput);

        searchIcon.setOnClickListener(v -> {
            String keyword = searchInput.getText().toString().trim();
            if (!keyword.isEmpty()) {
                filterPostsByTitle(keyword);
            } else {
                postList.clear();
                loadCommunityPosts();
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(CommunityActivity.this, PostCreateActivity.class);
            startActivity(intent);
        });

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            CommunityPost post = postList.get(position);
            Intent intent = new Intent(CommunityActivity.this, PostDetailActivity.class);
            intent.putExtra("title", post.getTitle());
            intent.putExtra("content", post.getContent());
            intent.putExtra("author", post.getNickname());
            intent.putExtra("date", post.getCreateAt());
            intent.putExtra("attachment", post.getAttachment());
            startActivity(intent);
        });

        loadCommunityPosts();

        //네비게이션 바 기능
        LinearLayout nav_home, nav_trip, nav_diary, nav_community;

        nav_home = findViewById(R.id.nav_home);
        nav_trip = findViewById(R.id.nav_trip);
        nav_diary = findViewById(R.id.nav_diary);
        nav_community = findViewById(R.id.nav_community);

        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        nav_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, PlanViewActivity.class);
                startActivity(intent);
            }
        });

        nav_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, diary.class);
                startActivity(intent);
            }
        });

        nav_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 데이터를 다시 로드
        postList.clear();
        loadCommunityPosts();
    }


    private void loadCommunityPosts() {
        String query = "SELECT c.post_id, c.title, c.content, c.attachment, c.create_at, c.user_id, u.nickname " +
                "FROM community c " +
                "LEFT JOIN user u ON c.user_id = u.user_id";

        db.executeQuery(query, new DB.QueryResponseListener() {
            @Override
            public void onQuerySuccess(Object data) {
                try {
                    JSONArray jsonArray = (JSONArray) data;

                    if (jsonArray.length() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("아직 게시물이 없습니다.");
                        gridView.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int postId = jsonObject.getInt("post_id");
                            String title = jsonObject.getString("title");
                            String content = jsonObject.getString("content");
                            String attachment = jsonObject.optString("attachment", "");
                            String createAt = jsonObject.getString("create_at");
                            String userId = jsonObject.getString("user_id");
                            String nickname = jsonObject.optString("nickname", "Unknown"); // 닉네임 추가

                            postList.add(new CommunityPost(postId, title, content, attachment, createAt, userId, nickname));
                        }
                        runOnUiThread(() -> {
                            emptyView.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CommunityActivity.this, "데이터 처리 중 오류 발생", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onQueryError(String errorMessage) {
                Toast.makeText(CommunityActivity.this, "오류: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void filterPostsByTitle(String keyword) {
        String query = "SELECT c.post_id, c.title, c.content, c.attachment, c.create_at, c.user_id, u.nickname " +
                "FROM community c " +
                "LEFT JOIN user u ON c.user_id = u.user_id " +
                "WHERE c.title LIKE '%" + keyword + "%'";

        db.executeQuery(query, new DB.QueryResponseListener() {
            @Override
            public void onQuerySuccess(Object data) {
                try {
                    JSONArray jsonArray = (JSONArray) data;
                    postList.clear(); // 기존 데이터 초기화

                    if (jsonArray.length() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("해당 게시물이 없습니다.");
                        gridView.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int postId = jsonObject.getInt("post_id");
                            String title = jsonObject.getString("title");
                            String content = jsonObject.getString("content");
                            String attachment = jsonObject.optString("attachment", "");
                            String createAt = jsonObject.getString("create_at");
                            String userId = jsonObject.getString("user_id");
                            String nickname = jsonObject.optString("nickname", "Unknown");

                            postList.add(new CommunityPost(postId, title, content, attachment, createAt, userId, nickname));
                        }
                        runOnUiThread(() -> {
                            emptyView.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CommunityActivity.this, "데이터 처리 중 오류 발생", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onQueryError(String errorMessage) {
                Toast.makeText(CommunityActivity.this, "오류: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

        });
    }


}
