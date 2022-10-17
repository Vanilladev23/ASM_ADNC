package com.example.asm_adnc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static Database instance;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
        return instance;
    }

    private Database(Context context) {
        super(context, "MY_DATABASE", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlUsers = "CREATE TABLE IF NOT EXISTS USERS (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT, PASSWORD TEXT, NAME TEXT, ROLE INTEGER)";
        sqLiteDatabase.execSQL(sqlUsers);
        String sqlCourses = "CREATE TABLE IF NOT EXISTS COURSES (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, CODE TEXT, ROOM TEXT, TIME TEXT, AVAILABLE INTEGER)";
        sqLiteDatabase.execSQL(sqlCourses);
        String sqlEnrols = "CREATE TABLE IF NOT EXISTS ENROLS (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERID INTEGER, COURSEID INTEGER, FOREIGN KEY(USERID) REFERENCES USERS(ID), FOREIGN KEY(COURSEID) REFERENCES COURSES(ID))";
        sqLiteDatabase.execSQL(sqlEnrols);

        sqLiteDatabase.execSQL("insert into USERS (EMAIL, PASSWORD, NAME, ROLE) values ('admin@gmail.com', '1', 'Hilde Skillitt', 1);");
        sqLiteDatabase.execSQL("insert into USERS (EMAIL, PASSWORD, NAME, ROLE) values ('student@gmail.com', '1', 'Jesus Cescot', 2);");

        sqLiteDatabase.execSQL("insert into COURSES (NAME, CODE, ROOM, TIME, AVAILABLE) values ('Macropus agilis', 'Lang Inc', 94, '11:27 AM', 0);");
        sqLiteDatabase.execSQL("insert into COURSES (NAME, CODE, ROOM, TIME, AVAILABLE) values ('Taxidea taxus', 'Walsh Group', 84, '12:22 AM', 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USERS");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS COURSES");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ENROLS");
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
