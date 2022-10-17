package com.example.asm_adnc.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.asm_adnc.R;
import com.example.asm_adnc.models.View_mot_o;

public class AppAdapter extends BaseAdapter {
    String[] ten = {"Courses","Map","News","Social"};
    int[] hinh = {R.drawable.course, R.drawable.maps, R.drawable.news, R.drawable.social};

    private Context context;

    public AppAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return hinh.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View_mot_o mot_o = new View_mot_o();
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        convertView = layoutInflater.inflate(R.layout.item_app, null);

        mot_o.img = convertView.findViewById(R.id.img);
        mot_o.tv = convertView.findViewById(R.id.tv);

        mot_o.img.setImageResource(hinh[position]);
        mot_o.tv.setText(ten[position]);

        return convertView;
    }
}
