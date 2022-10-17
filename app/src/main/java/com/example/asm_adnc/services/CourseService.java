package com.example.asm_adnc.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.asm_adnc.dao.CourseDao;
import com.example.asm_adnc.models.AppCourse;

import java.util.ArrayList;

public class CourseService extends IntentService {
    public static final String COURSE_SERVICE_EVENT = "COURSE_SERVICE_EVENT";
    public static final String COURSE_SERVICE_ACTION_INSERT = "COURSE_SERVICE_ACTION_INSERT";
    public static final String COURSE_SERVICE_ACTION_UPDATE = "COURSE_SERVICE_ACTION_UPDATE";
    public static final String COURSE_SERVICE_ACTION_DELETE = "COURSE_SERVICE_ACTION_DELETE";
    public static final String COURSE_SERVICE_ACTION_GET = "COURSE_SERVICE_ACTION_GET";

    private CourseDao courseDao;

    public CourseService() {
        super("CourseService");
        courseDao = new CourseDao(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case COURSE_SERVICE_ACTION_INSERT:
                    String name = intent.getStringExtra("name");
                    String code = intent.getStringExtra("code");
                    String time = intent.getStringExtra("time");
                    String room = intent.getStringExtra("room");
                    Integer available = 1;
                    AppCourse course = new AppCourse(-1, available, name, code, time, room);
                    Boolean result = courseDao.insert(course);
                    Intent outIntent1 = new Intent(COURSE_SERVICE_EVENT);
                    outIntent1.putExtra("result", result);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(outIntent1);
                    break;
                case COURSE_SERVICE_ACTION_UPDATE:
                    String name1 = intent.getStringExtra("name");
                    String code1 = intent.getStringExtra("code");
                    String time1 = intent.getStringExtra("time");
                    String room1 = intent.getStringExtra("room");
                    Integer available1 = 1;
                    AppCourse course1 = new AppCourse(-1, available1, name1, code1, time1, room1);
                    Boolean result1 = courseDao.update(course1);
                    Intent outIntent2 = new Intent(COURSE_SERVICE_EVENT);
                    outIntent2.putExtra("result", result1);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(outIntent2);
                    break;
                case COURSE_SERVICE_ACTION_DELETE:
                    Integer id = intent.getIntExtra("id", 0);
                    if (id > 0) {
                        Boolean res = courseDao.delete(id);
                        if (res) {
                            ArrayList<AppCourse> courses = (ArrayList<AppCourse>) courseDao.getAll();
                            Intent outIntent4 = new Intent(COURSE_SERVICE_EVENT);
                            outIntent4.putExtra("result", courses);
                            LocalBroadcastManager.getInstance(this).sendBroadcast(outIntent4);
                        }
                    }
                    break;
                case COURSE_SERVICE_ACTION_GET:
                    ArrayList<AppCourse> courses = (ArrayList<AppCourse>) courseDao.getAll();
                    Intent outIntent4 = new Intent(COURSE_SERVICE_EVENT);
                    outIntent4.putExtra("result", courses);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(outIntent4);
                    break;
                default:
                    break;
            }
        }
    }
}
