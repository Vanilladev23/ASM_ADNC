package com.example.asm_adnc.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.asm_adnc.dao.UserDao;
import com.example.asm_adnc.models.AppUser;

public class UserService extends IntentService {
    public static final String USER_SERVICE_EVENT = "USER_SERVICE_EVENT";
    public static final String ACTION_LOGIN = "ACTION_LOGIN";
    public static final String ACTION_REGISTER = "ACTION_REGISTER";

    private UserDao userDao;

    public UserService() {
        super("UserService");
        userDao = new UserDao(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_LOGIN:
                    String email = intent.getStringExtra("email");
                    String password = intent.getStringExtra("password");
                    AppUser appUser = userDao.login(email, password);
                    Intent outIntent = new Intent(USER_SERVICE_EVENT);
                    outIntent.putExtra("appUser", appUser);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(outIntent);
                    break;
                case ACTION_REGISTER:
                    String name = intent.getStringExtra("name");
                    String email1 = intent.getStringExtra("email");
                    String password1 = intent.getStringExtra("password");
                    Integer role = intent.getIntExtra("role", 1);
                    Boolean result = userDao.register(name, email1, password1, role);
                    Intent outIntent1 = new Intent(USER_SERVICE_EVENT);
                    outIntent1.putExtra("result", result);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(outIntent1);
                    break;
                default:
                    break;
            }
        }
    }
}