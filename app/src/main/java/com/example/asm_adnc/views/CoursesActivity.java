package com.example.asm_adnc.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.asm_adnc.R;
import com.example.asm_adnc.adapters.CourseAdapter;
import com.example.asm_adnc.models.AppCourse;
import com.example.asm_adnc.services.CourseService;

import java.util.ArrayList;

public class CoursesActivity extends AppCompatActivity {
    private ListView lvCourses;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        lvCourses = findViewById(R.id.lvCourses1);
    }

    private void onGetCourses() {
        Intent intent = new Intent(this, CourseService.class);
        intent.setAction(CourseService.COURSE_SERVICE_ACTION_GET);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onGetCourses();
        IntentFilter courseFilter = new IntentFilter(CourseService.COURSE_SERVICE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(coursesReceiver, courseFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(coursesReceiver);
    }

    private BroadcastReceiver coursesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<AppCourse> list = (ArrayList<AppCourse>) intent.getSerializableExtra("result");
            courseAdapter = new CourseAdapter(list);
            lvCourses.setAdapter(courseAdapter);
        }
    };
}