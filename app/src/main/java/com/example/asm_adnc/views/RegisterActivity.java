package com.example.asm_adnc.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.asm_adnc.R;
import com.example.asm_adnc.services.UserService;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtRegisterName, edtRegisterEmail, edtRegisterPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtRegisterName = findViewById(R.id.edtRegisterName);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = edtRegisterName.getText().toString();
            String email = edtRegisterEmail.getText().toString();
            String password = edtRegisterPassword.getText().toString();
            Integer role = 1;

            Intent intent = new Intent(this, UserService.class);
            intent.setAction(UserService.ACTION_REGISTER);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("role", role);
            startService(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(UserService.USER_SERVICE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(registerReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registerReceiver);
    }

    private BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean result = intent.getBooleanExtra("result", false);
            if (result) {
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    };

}