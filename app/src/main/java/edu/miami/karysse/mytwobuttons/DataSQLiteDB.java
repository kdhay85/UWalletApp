package edu.miami.karysse.mytwobuttons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

public class DataSQLiteDB {
    public static final String DB_NAME = "uwallet_db";
    private static final int DB_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase theDB;

    public DataSQLiteDB(Context theContext) {
        dbHelper = new DatabaseHelper(theContext);
        theDB = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        theDB.close();
    }

    public boolean checkCredentials(String email, String password) {
        String[] columns = {"cane_id AS _id", "email", "first_name", "last_name", "password"};
        String selection = "email=? AND password=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = theDB.query("person", columns, selection, selectionArgs, null, null, null);
        Log.d("DataSQLiteDB", "Table name: person");
        boolean isValid = cursor.getCount() > 0;

        if (cursor.moveToFirst()) {
            Log.d("DataSQLiteDB", "First row: " + cursor.getInt(0) + ", " + cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4));
        }

        cursor.close();
        Log.d("DataSQLiteDB", "SQL query: " + selection + ", selectionArgs: " + Arrays.toString(selectionArgs) + ", result: " + isValid);
        return isValid;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
