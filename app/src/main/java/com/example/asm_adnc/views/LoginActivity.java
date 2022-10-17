package com.example.asm_adnc.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.asm_adnc.MainActivity;
import com.example.asm_adnc.R;
import com.example.asm_adnc.models.AppUser;
import com.example.asm_adnc.services.UserService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private EditText edtLoginEmail, edtLoginPassword;
    private Button btnGoRegister, btnLogin;

    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // kiểm tra trạng thái login
        readLogin();
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnGoRegister = findViewById(R.id.btnGoRegister);
        btnLogin = findViewById(R.id.btnLogin);


        // Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        // kiểm tra login google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }

        // Facebook
        callbackManager = CallbackManager.Factory.create();

        LoginButton btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        btnFacebook.setReadPermissions("email", "public_profile");
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (isLoggedIn) {
                    Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
                Log.d("Facebook", "onSuccess: " + loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("Facebook", "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("Facebook", "onError: " + exception.getMessage());
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(com.facebook.Profile oldProfile, com.facebook.Profile currentProfile) {
                if (currentProfile != null) {
                    Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
            }
        };

        signInButton = findViewById(R.id.btnGoogle);
        signInButton.setOnClickListener(v -> {
            Intent intent = googleSignInClient.getSignInIntent();
            googleLauncher.launch(intent);
        });

        btnGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtLoginEmail.getText().toString();
            String password = edtLoginPassword.getText().toString();
            Intent intent = new Intent(this, UserService.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.setAction(UserService.ACTION_LOGIN);
            startService(intent);
        });
    }

    ActivityResultLauncher<Intent> googleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult();
                    String email = account.getEmail();
                    Log.d(">>>>>>>>>TAG", "onActivityResult Email: " + email);
                    // lưu email vào database
                    Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    );

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter loginFilter = new IntentFilter(UserService.USER_SERVICE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(loginReceiver, loginFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginReceiver);
    }

    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppUser appUser = (AppUser) intent.getSerializableExtra("appUser");
            if (appUser != null) {
                // lưu trạng thái đăng nhập vào bộ nhớ
                writeLogin(appUser);
                Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void writeLogin(AppUser appUser) {
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putInt("id", appUser.getId());
        editor.putInt("role", appUser.getRole());
        editor.putString("email", appUser.getEmail());
        editor.commit();
    }

    private void readLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_STATUS", MODE_PRIVATE);
        Boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}