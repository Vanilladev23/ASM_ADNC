package com.example.asm_adnc.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.asm_adnc.R;
import com.example.asm_adnc.models.AppCourse;
import com.example.asm_adnc.views.ActivityInterface;

import java.util.ArrayList;

public class CourseAdapter extends BaseAdapter {
    private ArrayList<AppCourse> list;

    public CourseAdapter(ArrayList<AppCourse> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = View.inflate(parent.getContext(), R.layout.layout_course_detail_item, null);
            TextView tvCourseName = view.findViewById(R.id.tvCourseName);
            TextView tvCourseCode = view.findViewById(R.id.tvCourseCode);
            TextView tvCourseTime = view.findViewById(R.id.tvCourseTime);
            TextView tvCourseRoom = view.findViewById(R.id.tvCourseRoom);
            ImageButton ivEdit = view.findViewById(R.id.ivEdit);
            ImageButton ivDelete = view.findViewById(R.id.ivDelete);
            ViewHolder viewHolder = new ViewHolder(tvCourseName, tvCourseCode, tvCourseTime, tvCourseRoom, ivEdit, ivDelete);
            view.setTag(viewHolder);
        }
        AppCourse appCourse = (AppCourse) getItem(position);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (appCourse != null) {
            viewHolder.tvCourseName.setText(appCourse.getName());
            viewHolder.tvCourseCode.setText(appCourse.getCode());
            viewHolder.tvCourseTime.setText(appCourse.getTime());
            viewHolder.tvCourseRoom.setText(appCourse.getRoom());
            viewHolder.ivEdit.setOnClickListener(v -> {
                ActivityInterface activityInterface = (ActivityInterface) parent.getContext();
                activityInterface.onEditCourseClick(appCourse);
            });

            viewHolder.ivDelete.setOnClickListener(v -> {
                ActivityInterface activityInterface = (ActivityInterface) parent.getContext();
                activityInterface.onDeleteCourseClick(appCourse);
            });
        }
        return view;
    }

    private static class ViewHolder {
        final TextView tvCourseName, tvCourseCode, tvCourseTime, tvCourseRoom;
        final ImageButton ivEdit, ivDelete;

        private ViewHolder(TextView tvCourseName, TextView tvCourseCode, TextView tvCourseTime, TextView tvCourseRoom, ImageButton ivEdit, ImageButton ivDelete) {
            this.tvCourseName = tvCourseName;
            this.tvCourseCode = tvCourseCode;
            this.tvCourseTime = tvCourseTime;
            this.tvCourseRoom = tvCourseRoom;
            this.ivEdit = ivEdit;
            this.ivDelete = ivDelete;
        }
    }
}
