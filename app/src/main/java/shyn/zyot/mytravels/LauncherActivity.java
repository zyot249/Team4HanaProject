package shyn.zyot.mytravels;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import shyn.zyot.mytravels.base.MyApplication;

public class LauncherActivity extends AppCompatActivity {

    private final static int SPLASH_DISPLAY_TIME = 1000;

    SharedPreferences sp;
    private String password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // no need
        setContentView(R.layout.activity_launcher);
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        password = sp.getString("password", ""); // lấy pass
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (((MyApplication) getApplication()).getTravelListSize() > 0) {
                    if (password.compareTo("") == 0) { // nếu không có pass
                        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else { // nếu có pass
                        // start LockScreen
                        Intent intent = new Intent(LauncherActivity.this, LockScreenActivity.class);
                        startActivity(intent);
                    }
                }else {
                    startActivity(new Intent(LauncherActivity.this, WelcomeActivity.class));
                }
                finish();
            }
        }, SPLASH_DISPLAY_TIME);

    }
}
