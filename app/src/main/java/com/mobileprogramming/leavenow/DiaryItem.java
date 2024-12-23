package com.mobileprogramming.leavenow;

public class DiaryItem {
    private long id;
    private String title;
    private String content;
    private String timestamp;
    private String imageUrl;
    private String mood;

    // 생성자
    public DiaryItem(long id, String title, String content, String timestamp, String imageUrl, String mood) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.mood = mood;
    }

    // Getter 및 Setter 메서드
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMood() {
        return mood;
    }
    public void setMood() {
        this.mood = mood; }
}
