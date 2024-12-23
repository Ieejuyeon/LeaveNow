package com.mobileprogramming.leavenow;

import org.json.JSONArray;
import org.json.JSONException;

public class CommunityPost {
    private int post_id;

    private String title;
    private String content;
    private String attachment;
    private String create_at;
    private String user_id;
    private String nickname;

    // 생성자
    public CommunityPost(int post_id, String title, String content, String attachment, String create_at, String user_id, String nickname) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.attachment = attachment;
        this.create_at = create_at;
        this.user_id = user_id;
        this.nickname = nickname;
    }

    // Getter
    public int getPostId() { return post_id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAttachment() { return attachment; }
    public String getCreateAt() { return create_at; }
    public String getUserId() { return user_id; }
    public String getNickname() {return nickname; }

}
