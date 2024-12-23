package com.mobileprogramming.leavenow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DiaryDatabaseManager {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DiaryDatabaseManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    // 데이터베이스 열기
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // 데이터베이스 닫기
    public void close() {
        dbHelper.close();
    }

    // 컬럼 데이터 가져오기 메서드
    private String getColumnValue(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1) {
            return cursor.getString(index);
        } else {
            Log.e("Database Error", columnName + " not found");
            return null;
        }
    }

    // 일기 추가
    public long addDiary(String title, String content, String timestamp, String imageUrl, float mood) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_CONTENT, content);
        values.put(DBHelper.COLUMN_TIMESTAMP, timestamp);
        values.put(DBHelper.COLUMN_IMAGE_URL, imageUrl);
        values.put(DBHelper.COLUMN_MOOD, mood);

        return database.insert(DBHelper.TABLE_DIARIES, null, values);
    }

    // 일기 중복 확인
    public boolean isDiaryExist(String title) {
        Cursor cursor = database.query(DBHelper.TABLE_DIARIES, null,
                DBHelper.COLUMN_TITLE + " = ?", new String[]{title},
                null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        return exists;
    }

    // 모든 일기 조회
    public List<DiaryItem> getAllDiaries() {
        List<DiaryItem> diaryList = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_DIARIES, null, null, null, null, null, DBHelper.COLUMN_TIMESTAMP + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                String title = getColumnValue(cursor, DBHelper.COLUMN_TITLE);
                String content = getColumnValue(cursor, DBHelper.COLUMN_CONTENT);
                String timestamp = getColumnValue(cursor, DBHelper.COLUMN_TIMESTAMP);
                String imageUrl = getColumnValue(cursor, DBHelper.COLUMN_IMAGE_URL);
                String mood = getColumnValue(cursor, DBHelper.COLUMN_MOOD);

                diaryList.add(new DiaryItem(id, title, content, timestamp, imageUrl, mood));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.e("Database Error", "No data found");
        }
        return diaryList;
    }

    // 일기 상세 조회
    public DiaryItem getDiaryById(long id) {
        DiaryItem diaryItem = null;
        Cursor cursor = database.query(DBHelper.TABLE_DIARIES, null,
                DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            long diaryId = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String title = getColumnValue(cursor, DBHelper.COLUMN_TITLE);
            String content = getColumnValue(cursor, DBHelper.COLUMN_CONTENT);
            String timestamp = getColumnValue(cursor, DBHelper.COLUMN_TIMESTAMP);
            String imageUrl = getColumnValue(cursor, DBHelper.COLUMN_IMAGE_URL);
            String mood = getColumnValue(cursor, DBHelper.COLUMN_MOOD);

            diaryItem = new DiaryItem(diaryId, title, content, timestamp, imageUrl, mood);
            cursor.close();
        }
        return diaryItem;
    }

    // 일기 수정
    public void updateDiary(long id, String content, int mood) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CONTENT, content);
        values.put(DBHelper.COLUMN_MOOD, mood);

        database.update(DBHelper.TABLE_DIARIES, values, DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // 일기 삭제
    public void deleteDiary(long id) {
        database.delete(DBHelper.TABLE_DIARIES, DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // 테이블 컬럼 목록 가져오기
    public List<String> getTableColumns(String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = database.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                columns.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return columns;
    }
}
