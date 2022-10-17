package com.example.asm_adnc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_adnc.adapters.AppAdapter;
import com.example.asm_adnc.views.CoursesActivity;
import com.example.asm_adnc.views.LoginActivity;
import com.example.asm_adnc.views.MapsActivity;
import com.example.asm_adnc.views.NewsActivity;
import com.example.asm_adnc.views.SocialActivity;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity {
    private GridView gv;
    private Button btnLogout, btnLogoutGoogle, btnLogoutFacebook;

    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gv = findViewById(R.id.gridView);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogoutGoogle = findViewById(R.id.btnLogoutGoogle);
        btnLogoutFacebook = findViewById(R.id.btnLogoutFacebook);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        callbackManager = CallbackManager.Factory.create();


        AppAdapter appAdapter = new AppAdapter(this);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int po = position;
                System.out.println("vị trí: " + po);
                if (po == 0) {
                    Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
                    startActivity(intent);
                } else if (po == 1) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else if (po == 2) {
                    Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                    startActivity(intent);
                } else if (po == 3) {
                    Intent intent = new Intent(MainActivity.this, SocialActivity.class);
                    startActivity(intent);
                }
            }
        });
        gv.setAdapter(appAdapter);

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("isLoggedIn");
            editor.remove("id");
            editor.remove("role");
            editor.remove("email");
            editor.apply();

            Intent homeIntent = new Intent(this, LoginActivity.class);
            startActivity(homeIntent);
            finish();
        });

        // Đăng xuất google
        btnLogoutGoogle.setOnClickListener(v -> {
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                Intent homeIntent = new Intent(this, LoginActivity.class);
                startActivity(homeIntent);
                finish();
            });
        });

        // Đăng xuất facebook
        btnLogoutFacebook.setOnClickListener(v -> {

        });
    }
}