package com.mobileprogramming.leavenow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "diaryDB";
    public static final int DATABASE_VERSION = 2; // 버전 2로 업데이트
    public static final String TABLE_DIARIES = "diaries";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_MOOD = "mood"; // mood 컬럼 추가

    // 테이블 생성 SQL (mood 컬럼 추가)
    private static final String CREATE_TABLE_DIARIES =
            "CREATE TABLE " + TABLE_DIARIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_TIMESTAMP + " TEXT, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_MOOD + " INTEGER);"; // mood 컬럼을 INTEGER로 정의 (0-5로 설정)

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DIARIES); // 테이블 생성
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블을 삭제하고 다시 생성하여 schema 변경을 적용
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARIES);
        onCreate(db);
    }
}
