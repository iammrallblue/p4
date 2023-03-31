package edu.uga.cs.p4;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This is a SQLiteOpenHelper class, for CURD operations in this project.
 * Following singleton design pattern...
 * <p>
 * /data/data/edu.uga.cs.p4/databases/geoquiz.db, to check database and tables
 */
public class GeoQuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "GeoQuizDBHelper";
    private static final String DB_NAME = "geoquiz.db"; // the name of database
    private static final int DB_VERSION = 1;

    /* The continent table */
    private static final String TABLE_CONTINENTS = "continents";
    private static final String KEY_CONTINENT_ID = "id";
    private static final String KEY_CONTINENT_NAME = "name";

    /* The country table */
    private static final String TABLE_COUNTRIES = "countries";
    private static final String KEY_COUNTRY_ID = "id";
    private static final String KEY_COUNTRY_NAME = "name";
    private static final String KEY_COUNTRY_CONTINENT = "continent";
    private static final String KEY_COUNTRY_CONTINENT_ID = "continent_id";


    /* The neighbor table */
    private static final String TABLE_NEIGHBOR = "neighbor";
    private static final String KEY_NEIGHBOR_ID = "id";
    private static final String KEY_NEIGHBOR_COUNTRY_ID = "country_id";
    private static final String KEY_NEIGHBOR_NAME = "neighbor_name";
    private static final String KEY_NEIGHBOR_NEIGHBORING_COUNTRY_ID = "neighboring_country_id";

    /* The Quiz bank table */
    private static final String TABLE_QUIZ_BANK = "quiz_bank";
    private static final String KEY_QUIZ_BANK_ID = "id";
    private static final String KEY_QUIZ_BANK_QUESTION = "question";
    private static final String KEY_QUIZ_BANK_CORRECT_ANSWER = "correct_answer";
    private static final String KEY_QUIZ_BANK_WRONG_ANSWER1 = "wrong_answer1";
    private static final String KEY_QUIZ_BANK_WRONG_ANSWER2 = "wrong_answer2";


    /* The Completed Quiz table */
    private static final String TABLE_COMPLETED_QUIZZES = "completed_quizzes";
    private static final String KEY_COMPLETED_QUIZ_ID = "quiz_id";
    private static final String KEY_COMPLETED_QUIZ_DATE = "quiz_date";
    private static final String KEY_COMPLETED_QUIZ_RESULT = "quiz_result";


    // This is a reference to the only instance for the helper.
    private static GeoQuizDBHelper helperInstance;
    private final Context context;

    // private constructor of GeoQuizDBHelper
    public GeoQuizDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
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

        // Create a continent table
        String CREATE_CONTINENTS_TABLE = "CREATE TABLE " + TABLE_CONTINENTS + "(" + KEY_CONTINENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CONTINENT_NAME + " TEXT UNIQUE" + ")";

        // Create a country table
        String CREATE_COUNTRIES_TABLE = "CREATE TABLE " + TABLE_COUNTRIES + "(" + KEY_COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_COUNTRY_NAME + " TEXT, " + KEY_COUNTRY_CONTINENT + " TEXT, " + KEY_COUNTRY_CONTINENT_ID + " INTEGER," + "FOREIGN KEY (" + KEY_COUNTRY_CONTINENT_ID + ") REFERENCES " + TABLE_CONTINENTS + "(" + KEY_CONTINENT_ID + ")" + ")";

        // Create a neighbor table
        String CREATE_NEIGHBORS_TABLE = "CREATE TABLE " + TABLE_NEIGHBOR + "(" + KEY_NEIGHBOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NEIGHBOR_COUNTRY_ID + " INTEGER NOT NULL," + KEY_NEIGHBOR_NAME + " TEXT," + KEY_NEIGHBOR_NEIGHBORING_COUNTRY_ID + " INTEGER," + "FOREIGN KEY (" + KEY_NEIGHBOR_COUNTRY_ID + ") REFERENCES " + TABLE_COUNTRIES + "(" + KEY_COUNTRY_ID + ")" + "FOREIGN KEY (" + KEY_NEIGHBOR_NEIGHBORING_COUNTRY_ID + ") REFERENCES " + TABLE_COUNTRIES + "(" + KEY_COUNTRY_ID + ")" + ")";

        // Create a quiz bank table
        String CREATE_QUIZ_BANK_TABLE = "CREATE TABLE " + TABLE_QUIZ_BANK + "(" + KEY_QUIZ_BANK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_QUIZ_BANK_QUESTION + " TEXT, " + KEY_QUIZ_BANK_CORRECT_ANSWER + " TEXT," + KEY_QUIZ_BANK_WRONG_ANSWER1 + " TEXT," + KEY_QUIZ_BANK_WRONG_ANSWER2 + " TEXT" + ")";


        // Create a completed quizzes table
        String CREATE_COMPLETED_QUIZZES_TABLE = "CREATE TABLE " + TABLE_COMPLETED_QUIZZES + "(" + KEY_COMPLETED_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_COMPLETED_QUIZ_DATE + " INTEGER," + KEY_COMPLETED_QUIZ_RESULT + " INTEGER" + ")";

        // execute sql statements
        db.execSQL(CREATE_CONTINENTS_TABLE);
        db.execSQL(CREATE_COUNTRIES_TABLE);
        db.execSQL(CREATE_NEIGHBORS_TABLE);
        db.execSQL(CREATE_QUIZ_BANK_TABLE);
        db.execSQL(CREATE_COMPLETED_QUIZZES_TABLE);

        // import countries data from CSV file
        try {
            InputStream inputStream = context.getAssets().open("country_continent.csv");
            importCountriesFromCSV(db, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // onCreate()

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTINENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEIGHBOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_BANK);
        db.execSQL("DROP TABle IF EXISTS " + TABLE_COMPLETED_QUIZZES);
        onCreate(db); // recreate all tables after dropped all tables in database
    } // onUpgrade()

    /**
     * read the CSV file and insert the data into the countries table.
     */
    public void importCountriesFromCSV(SQLiteDatabase db, InputStream inputStream) throws IOException {

        // get database to write
//        SQLiteDatabase db = this.getWritableDatabase();

        // read CSV data from the InputStream
        InputStreamReader isr = new InputStreamReader(inputStream);

        // read the CSV file line by line
        BufferedReader br = new BufferedReader(isr);
        String line; // read the first line of the CSV file

        while ((line = br.readLine()) != null) {
            String[] columns = line.split(","); // get data split by comma

            // ** need to check order of columns in table
            if (columns.length > 2) {
                // extract the values from the columns
                String country = columns[0].trim();
                String continent = columns[1].trim();

                // insert the values into the database
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_COUNTRY_NAME, country);
                contentValues.put(KEY_COUNTRY_CONTINENT, continent);

                // insert the row into the table
                db.insert(TABLE_COUNTRIES, null, contentValues);
            }
        } // while

    } // importCountriesFromCSV

} // CountryQuizDBHelper
