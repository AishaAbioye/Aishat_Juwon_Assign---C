package com.example.aishat_juwon_assign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ComboDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "comboAttempts.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_COMBO_ATTEMPTS = "combo_attempts";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COMBO_NAME = "combo_name";
    public static final String COLUMN_ATTEMPTED = "attempted";
    public static final String COLUMN_RESULT = "result";

    // Create table SQL query
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_COMBO_ATTEMPTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COMBO_NAME + " TEXT, " +
                    COLUMN_ATTEMPTED + " INTEGER, " +
                    COLUMN_RESULT + " INTEGER" + ");";

    public ComboDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMBO_ATTEMPTS);
        onCreate(db);
    }

    // Insert combo attempt
    public void insertComboAttempt(String comboName, boolean attempted, boolean result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMBO_NAME, comboName);
        values.put(COLUMN_ATTEMPTED, attempted ? 1 : 0);
        values.put(COLUMN_RESULT, result ? 1 : 0);
        db.insert(TABLE_COMBO_ATTEMPTS, null, values);
        db.close();
    }

    // Update combo attempt
    public void updateComboAttempt(String comboName, boolean attempted, boolean result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ATTEMPTED, attempted ? 1 : 0);
        values.put(COLUMN_RESULT, result ? 1 : 0);
        db.update(TABLE_COMBO_ATTEMPTS, values, COLUMN_COMBO_NAME + "=?", new String[]{comboName});
        db.close();
    }

    // Update combo result only
    public void updateComboResult(String comboName, boolean result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT, result ? 1 : 0);
        db.update(TABLE_COMBO_ATTEMPTS, values, COLUMN_COMBO_NAME + "=?", new String[]{comboName});
        db.close();
    }

    // Get combo attempt
    public Cursor getComboAttempt(String comboName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_COMBO_ATTEMPTS, null, COLUMN_COMBO_NAME + "=?", new String[]{comboName}, null, null, null);
    }

    // Check if record exists
    public boolean recordExists(String comboName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMBO_ATTEMPTS, new String[]{COLUMN_COMBO_NAME}, COLUMN_COMBO_NAME + "=?", new String[]{comboName}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
