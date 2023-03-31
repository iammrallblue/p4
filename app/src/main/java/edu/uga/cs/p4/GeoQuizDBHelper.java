package edu.uga.cs.p4;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * This is a SQLiteOpenHelper class, for CURD operations in this project.
 * Following singleton design pattern...
 */
public class GeoQuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "GeoQuizDBHelper";
    private static final String DB_NAME = "geo_app.db"; // the name of database
    private static final int DB_VERSION = 1;

    /* The country table */
    private static final String TABLE_COUNTRY = "country";
    private static final String KEY_COUNTRY_ID = "id";
    private static final String KEY_COUNTRY_NAME = "name";
    private static final String KEY_CONTINENT = "continent";
    private static final String KEY_COUNTRY_CONTINENT_ID = "continent_id";

    /* The continent table */
    private static final String TABLE_CONTINENT = "continent";
    private static final String KEY_CONTINENT_ID = "id";
    private static final String KEY_CONTINENT_NAME = "name";

    /* The neighbor table */
    private static final String TABLE_NEIGHBOR = "neighbor";
    private static final String KEY_NEIGHBOR_ID = "id";
    private static final String KEY_NEIGHBOR_COUNTRY_ID = "country_id";
    private static final String KEY_NEIGHBOR_NAME = "neighbor_name";

    /* The Quiz bank table */
    private static final String TABLE_QUIZ_BANK = "quiz_bank";
    private static final String KEY_QUIZ_BANK_ID = "id";
    private static final String KEY_QUIZ_BANK_QUESTION = "question";
    private static final String KEY_QUIZ_BANK_CORRECT_ANSWER = "correct_answer";
    private static final String KEY_QUIZ_BANK_WRONG_ANSWER1 = "wrong_answer1";
    private static final String KEY_QUIZ_BANK_WRONG_ANSWER2 = "wrong_answer2";


    /* The Completed Quiz table */
    private static final String TABLE_COMPLETED_QUIZ = "completed_quiz";
    private static final String KEY_COMPLETED_QUIZ_ID = "quiz_id";
    private static final String KEY_COMPLETED_QUIZ_DATE = "quiz_date";
    private static final String KEY_COMPLETED_QUIZ_RESULT = "quiz_result";


    private static GeoQuizDBHelper helperInstance;

    public GeoQuizDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Access method to the single instance of the class.
    public static synchronized GeoQuizDBHelper getInstance(Context context) {
        if (helperInstance == null) {
            helperInstance = new GeoQuizDBHelper(context.getApplicationContext());
        }
        return helperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a country table
        String CREATE_COUNTRY_TABLES = "CREATE TABLE " + TABLE_COUNTRY + "("
                + KEY_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_COUNTRY_NAME + " TEXT,"
                + KEY_CONTINENT + " TEXT" + ")";

        // Create a neighbor table
        String CREATE_NEIGHBORS_TABLE = "CREATE TABLE " + TABLE_NEIGHBOR + "("
                + KEY_NEIGHBOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NEIGHBOR_COUNTRY_ID + "INTEGER,"
                + KEY_NEIGHBOR_NAME + " TEXT,"
                + "FOREIGN KEY (" + KEY_NEIGHBOR_COUNTRY_ID + ") REFERENCES " + TABLE_COUNTRY
                + "(" + KEY_COUNTRY_ID + ")" + ")";

        // Create a quiz bank table
        String CREATE_QUIZ_BANK_TABLE = "CREATE TABLE " + TABLE_QUIZ_BANK + "("
                + KEY_QUIZ_BANK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEYQUI


        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ_BANK + "("
        + KEY_QUIZ_BANK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + KEY_QUIZ_BANK_DATE + " INTEGER,"
        + KEY_QUIZ_RESULT + " INTEGER" + ")";


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
} // CountryQuizDBHelper
