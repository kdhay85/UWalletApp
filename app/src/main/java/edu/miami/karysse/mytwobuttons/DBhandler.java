package edu.miami.karysse.mytwobuttons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBhandler extends SQLiteOpenHelper {

    static final String DB_NAME = "uwallet";
    static final int DB_VERSION = 1;

    // Hardcoded file path to the database
    static final String DB_FILE_PATH = "/data/data/edu.miami.karysse.mytwobuttons/databases/uwallet_db";

    public DBhandler(Context context) {
        super(context, DB_FILE_PATH, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to create tables, as they already exist in the existing database file
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No need to upgrade tables, as they already exist in the existing database file
    }

    public boolean isValidCredentials(String email, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean isValid = true;

        try {
            db = SQLiteDatabase.openDatabase(DB_FILE_PATH, null, SQLiteDatabase.OPEN_READONLY);
            String[] columns = {"cane_id AS _id", "email", "first_name", "last_name", "password"};
            String selection = "email=? AND password=?";
            String[] selectionArgs = {email, password};
            cursor = db.query("person", columns, selection, selectionArgs, null, null, null);

            // Check if the cursor has any rows
            if (cursor != null && cursor.moveToFirst()) {
                isValid = true; // Set the isValid flag to true
                // Log the contents of the cursor
                do {
                    Log.d("DBContents", "Worked");
                } while (cursor.moveToNext());
            } else {
                Log.d("DBContents", "No rows found in the database");
            }
        } catch (SQLiteException e) {
            // Log the error message
            Log.e("DBError", "Error accessing database: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return isValid;
    }


}
