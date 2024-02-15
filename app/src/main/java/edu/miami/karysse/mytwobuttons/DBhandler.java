package edu.miami.karysse.mytwobuttons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhandler extends SQLiteOpenHelper {

    // Database constants
    private static final String DB_NAME = "uwallet_db";
    private static final int DB_VERSION = 1;

    // Table constants
    private static final String TABLE_PERSON = "person";
    private static final String TABLE_MEAL_PLANS = "meal_plans";
    private static final String TABLE_STUDENT_MEAL_PLAN = "student_meal_plan";
    private static final String TABLE_BUILDING_ACCESS = "building_access";
    private static final String TABLE_DOOLEY_BUILDING = "dooley_building";
    private static final String TABLE_WHITTEN_BUILDING = "whitten_building";

    // Common column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CANE_ID = "cane_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_PASSWORD = "password";

    // Student table column names
    private static final String COLUMN_STUDENT_MEAL_PLAN_ID = "student_meal_plan_id";

    // Meal Plan table column names
    private static final String COLUMN_MEAL_PLAN_NAME = "meal_plan_name";
    private static final String COLUMN_SWIPES = "swipes";
    private static final String COLUMN_GUEST_SWIPES = "guest_swipes";
    private static final String COLUMN_DINING_DOLLARS = "dining_dollars";

    // Building Access table column names
    private static final String COLUMN_BUILDING_NAME = "building_name";
    private static final String COLUMN_ROOM_NUMBER = "room_number";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_END_TIME = "end_time";

    // Building tables column names
    private static final String COLUMN_CAPACITY = "capacity"; // Added this line

    // Constructor
    public DBhandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Person table
        String createPersonTable = "CREATE TABLE " + TABLE_PERSON + "("
                + COLUMN_CANE_ID + " TEXT PRIMARY KEY,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(createPersonTable);

        // Create Meal Plans table
        String createMealPlansTable = "CREATE TABLE " + TABLE_MEAL_PLANS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MEAL_PLAN_NAME + " TEXT,"
                + COLUMN_SWIPES + " INTEGER,"
                + COLUMN_GUEST_SWIPES + " INTEGER,"
                + COLUMN_DINING_DOLLARS + " REAL" + ")";
        db.execSQL(createMealPlansTable);

        // Create Student Meal Plan table
        String createStudentMealPlanTable = "CREATE TABLE " + TABLE_STUDENT_MEAL_PLAN + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CANE_ID + " TEXT,"
                + COLUMN_STUDENT_MEAL_PLAN_ID + " INTEGER,"
                + "FOREIGN KEY (" + COLUMN_CANE_ID + ") REFERENCES " + TABLE_PERSON + "(" + COLUMN_CANE_ID + "),"
                + "FOREIGN KEY (" + COLUMN_STUDENT_MEAL_PLAN_ID + ") REFERENCES " + TABLE_MEAL_PLANS + "(" + COLUMN_ID + ")" + ")";
        db.execSQL(createStudentMealPlanTable);

        // Create Building Access table
        String createBuildingAccessTable = "CREATE TABLE " + TABLE_BUILDING_ACCESS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CANE_ID + " TEXT,"
                + COLUMN_BUILDING_NAME + " TEXT,"
                + COLUMN_ROOM_NUMBER + " TEXT,"
                + COLUMN_START_TIME + " DATETIME,"
                + COLUMN_END_TIME + " DATETIME,"
                + "FOREIGN KEY (" + COLUMN_CANE_ID + ") REFERENCES " + TABLE_PERSON + "(" + COLUMN_CANE_ID + ")"
                + ")";
        db.execSQL(createBuildingAccessTable);

        // Create Dooley Building table
        String createDooleyBuildingTable = "CREATE TABLE " + TABLE_DOOLEY_BUILDING + "("
                + COLUMN_ROOM_NUMBER + " TEXT PRIMARY KEY,"
                + COLUMN_CAPACITY + " INTEGER"
                + ")";
        db.execSQL(createDooleyBuildingTable);

        // Create Whitten Building table
        String createWhittenBuildingTable = "CREATE TABLE " + TABLE_WHITTEN_BUILDING + "("
                + COLUMN_ROOM_NUMBER + " TEXT PRIMARY KEY,"
                + COLUMN_CAPACITY + " INTEGER"
                + ")";
        db.execSQL(createWhittenBuildingTable);
    }

    // Upgrade tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL_PLANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_MEAL_PLAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING_ACCESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOOLEY_BUILDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHITTEN_BUILDING);
        onCreate(db);
    }

    // Add a person
    public void addPerson(String caneId, String email, String firstName, String lastName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANE_ID, caneId);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_PERSON, null, values);
        db.close();
    }

    // Add a meal plan
    public void addMealPlan(String name, int swipes, int guestSwipes, double diningDollars) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL_PLAN_NAME, name);
        values.put(COLUMN_SWIPES, swipes);
        values.put(COLUMN_GUEST_SWIPES, guestSwipes);
        values.put(COLUMN_DINING_DOLLARS, diningDollars);
        db.insert(TABLE_MEAL_PLANS, null, values);
        db.close();
    }

    // Assign a meal plan to a student
    public void assignMealPlanToStudent(String caneId, int mealPlanId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANE_ID, caneId);
        values.put(COLUMN_STUDENT_MEAL_PLAN_ID, mealPlanId);
        db.insert(TABLE_STUDENT_MEAL_PLAN, null, values);
        db.close();
    }

    // Add building access
    public void addBuildingAccess(String caneId, String buildingName, String roomNumber, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CANE_ID, caneId);
        values.put(COLUMN_BUILDING_NAME, buildingName);
        values.put(COLUMN_ROOM_NUMBER, roomNumber);
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);
        db.insert(TABLE_BUILDING_ACCESS, null, values);
        db.close();
    }

    // Add a room to the Dooley building
    public void addRoomToDooley(String roomNumber, int capacity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROOM_NUMBER, roomNumber);
        values.put(COLUMN_CAPACITY, capacity);
        db.insert(TABLE_DOOLEY_BUILDING, null, values);
        db.close();
    }

    // Add a room to the Whitten building
    public void addRoomToWhitten(String roomNumber, int capacity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ROOM_NUMBER, roomNumber);
        values.put(COLUMN_CAPACITY, capacity);
        db.insert(TABLE_WHITTEN_BUILDING, null, values);
        db.close();
    }
}
