package edu.uga.cs.p4;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeoQuizDBHelperTest {
    private GeoQuizDBHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp(){
        dbHelper = GeoQuizDBHelper.getInstance(ApplicationProvider.getApplicationContext());
        db = dbHelper.getWritableDatabase();
    }

    @After
    public void tearDown(){
        dbHelper.close();
        dbHelper = null;
    }

    @Test
    public void testDBIsOpen(){
        assertTrue(db.isOpen());
    }

    @SuppressLint("Range")
    @Test
    public void testInsertAndRetrieveQuiz(){
        // Insert a new quiz record
        ContentValues contentValues = new ContentValues();
        contentValues.put("quiz_date", "2023-03-30");
        contentValues.put("quiz_result", 5);
        long quizId = db.insert("completed_quizzes", null, contentValues);

        // retrieve the quiz record
        Cursor cursor = db.query("completed_quizzes", null, "quiz_id=?", new String[]{String.valueOf(quizId)}, null,null,null);

        // check if the record was found
        assertTrue(cursor.moveToFirst());

        // check if the data matches
        assertEquals("2023-03-30", cursor.getString(cursor.getColumnIndex("quiz_date")));
        assertEquals(5, cursor.getInt(cursor.getColumnIndex("quiz_result")));

        // close the cursor
        cursor.close();
    }
}
