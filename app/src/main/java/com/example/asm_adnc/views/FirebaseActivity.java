package com.example.asm_adnc.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_adnc.R;
import com.example.asm_adnc.adapters.CourseAdapter;
import com.example.asm_adnc.models.AppCourse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseActivity extends AppCompatActivity implements ActivityInterface {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    private void fillData() {
        db.collection("courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<AppCourse> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> item = document.getData();
                                String name = item.get("name").toString();
                                String code = item.get("code").toString();
                                String time = item.get("time").toString();
                                String room = item.get("room").toString();
                                String courseID = document.getId();
                                AppCourse course = new AppCourse();
                                course.setName(name);
                                course.setCode(code);
                                course.setTime(time);
                                course.setRoom(room);
                                course.setCourseID(courseID);
                                list.add(course);
                            }
                            CourseAdapter adapter = new CourseAdapter(list);
                            ListView lvCourse = (ListView) findViewById(R.id.lvCourses);
                            lvCourse.setAdapter(adapter);
                        }
                    }
                });
    }

    public void onClick(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_course_form, null);
        EditText edtCourseName = dialogLayout.findViewById(R.id.edtCourseName);
        EditText edtCourseCode = dialogLayout.findViewById(R.id.edtCourseCode);
        EditText edtCourseTime = dialogLayout.findViewById(R.id.edtCourseTime);
        EditText edtCourseRoom = dialogLayout.findViewById(R.id.edtCourseRoom);
        Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);
        Button btnSave = dialogLayout.findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(v -> {
            edtCourseName.setText(null);
            edtCourseCode.setText(null);
            edtCourseTime.setText(null);
            edtCourseRoom.setText(null);
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            String name = edtCourseName.getText().toString();
            String code = edtCourseCode.getText().toString();
            String time = edtCourseTime.getText().toString();
            String room = edtCourseRoom.getText().toString();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("code", code);
            user.put("time", time);
            user.put("room", room);

            // Add a new document with a generated ID
            db.collection("courses")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            edtCourseName.setText(null);
                            edtCourseCode.setText(null);
                            edtCourseTime.setText(null);
                            edtCourseRoom.setText(null);
                            dialog.dismiss();
                            fillData();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        });

        AlertDialog.Builder builder = new AlertDialog.Builder(FirebaseActivity.this);
        builder.setTitle("Course");
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onEditCourseClick(AppCourse appCourse) {
        // Load layout dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.layout_course_form, null);
        EditText edtCourseName = dialogLayout.findViewById(R.id.edtCourseName);
        EditText edtCourseCode = dialogLayout.findViewById(R.id.edtCourseCode);
        EditText edtCourseTime = dialogLayout.findViewById(R.id.edtCourseTime);
        EditText edtCourseRoom = dialogLayout.findViewById(R.id.edtCourseRoom);
        Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);
        Button btnSave = dialogLayout.findViewById(R.id.btnSave);

        // Fill data
        edtCourseName.setText(appCourse.getName());
        edtCourseCode.setText(appCourse.getCode());
        edtCourseTime.setText(appCourse.getTime());
        edtCourseRoom.setText(appCourse.getRoom());

        btnCancel.setOnClickListener(v -> {
            edtCourseName.setText(null);
            edtCourseCode.setText(null);
            edtCourseTime.setText(null);
            edtCourseRoom.setText(null);
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            String name = edtCourseName.getText().toString();
            String code = edtCourseCode.getText().toString();
            String time = edtCourseTime.getText().toString();
            String room = edtCourseRoom.getText().toString();

            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("code", code);
            user.put("time", time);
            user.put("room", room);

            // Update data
            db.collection("courses").document(appCourse.getCourseID())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            edtCourseName.setText(null);
                            edtCourseCode.setText(null);
                            edtCourseTime.setText(null);
                            edtCourseRoom.setText(null);
                            dialog.dismiss();
                            fillData();
                        }
                    });

        });


        // Show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(FirebaseActivity.this);
        builder.setTitle("Course");
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onDeleteCourseClick(AppCourse appCourse) {
        new AlertDialog
                .Builder(FirebaseActivity.this)
                .setTitle("Delete")
                .setMessage("Are you sure?")
                .setNegativeButton("Huỷ", null)
                .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("courses").document(appCourse.getCourseID())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        fillData();
                                    }
                                });
                    }
                }).show();
    }
}