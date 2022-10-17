package com.example.asm_adnc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.asm_adnc.database.Database;
import com.example.asm_adnc.models.AppCourse;

import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    private Database database;

    public CourseDao(Context context) {
        database = Database.getInstance(context);
    }

    // select id, name, code, time, room form courses
    public List<AppCourse> getAll() {
        List<AppCourse> list = new ArrayList<>();
        String sql = "SELECT ID, NAME, CODE, ROOM, TIME, AVAILABLE FROM COURSES ";
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Integer _id = cursor.getInt(0);
                    String _name = cursor.getString(1);
                    String _code = cursor.getString(2);
                    String _room = cursor.getString(3);
                    String _time = cursor.getString(4);
                    Integer _available = cursor.getInt(5);
                    AppCourse course = new AppCourse(_id, _available, _name, _code, _time, _room);
                    list.add(course);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.d(">>>>>>>>>>>>TAG", "getAll: " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return list;
    }


    public Boolean insert(AppCourse course) {
        Boolean result = false;
        SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("NAME", course.getName());
            values.put("CODE", course.getCode());
            values.put("TIME", course.getTime());
            values.put("ROOM", course.getRoom());
            values.put("AVAILABLE", course.getAvailable());
            long rows = db.insertOrThrow("COURSES", null, values);
            db.setTransactionSuccessful();
            result = rows >= 1;
        } catch (Exception e) {
            Log.d(">>>>>>>>>>>>TAG", "insert: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public Boolean update(AppCourse course) {
        Boolean result = false;
        SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("NAME", course.getName());
            values.put("CODE", course.getCode());
            values.put("TIME", course.getTime());
            values.put("ROOM", course.getRoom());
            values.put("AVAILABLE", course.getAvailable());
            long rows = db.update("COURSES", values, " ID = ? ", new String[]{course.getId().toString()});
            db.setTransactionSuccessful();
            result = rows >= 1;
        } catch (Exception e) {
            Log.d(">>>>>>>>>>>>TAG", "update: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
        return result;
    }

    public Boolean delete(Integer id) {
        Boolean result = false;
        SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            long rows = db.delete("COURSES", " ID = ? ", new String[]{id.toString()});
            db.setTransactionSuccessful();
            result = rows >= 1;
        } catch (Exception e) {
            Log.d(">>>>>>>>>>>>TAG", "delete: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
        return result;
    }
}
