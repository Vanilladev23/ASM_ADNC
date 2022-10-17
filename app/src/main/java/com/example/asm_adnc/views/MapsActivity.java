package com.example.asm_adnc.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.asm_adnc.R;
import com.example.asm_adnc.fragment.MapsFragment;

public class MapsActivity extends AppCompatActivity {
    private EditText edtAddress;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        edtAddress = findViewById(R.id.edtAddress);
        btnSearch = findViewById(R.id.btnSearch);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(
                    this, new String []{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        btnSearch.setOnClickListener(v -> {
            String address = edtAddress.getText().toString();
            System.out.println("Địa chỉ: " + address);
            getSupportFragmentManager().beginTransaction().replace(R.id.flMap, MapsFragment.newInstance(address)).commit();
        });

    }

}