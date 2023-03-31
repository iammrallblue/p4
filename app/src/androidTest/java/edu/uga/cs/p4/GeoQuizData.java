package edu.uga.cs.p4;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class GeoQuizData {

    private SQLiteDatabase db;
    private final SQLiteOpenHelper geoQuizDBHelper;

    public GeoQuizData(Context context) {
        this.geoQuizDBHelper = GeoQuizDBHelper.getInstance(context);
    }

    public void open() {
        db = geoQuizDBHelper.getWritableDatabase();
    } // open database

    public void close() {
        if (geoQuizDBHelper != null) {
            geoQuizDBHelper.close();
        }
    } // close database

    // insert quiz result
    public void insertQuizResult(int correctAnswers) {
        ContentValues values = new ContentValues();
//        values.put(GeoQuizDBHelper.COMPLETED_QUIZZES_COLUMN_QUIZ_DATA, System.currentTimeMillis());
//        values.put();
    }
}

