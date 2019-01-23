package shyn.zyot.mytravels;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import shyn.zyot.mytravels.base.MyConst;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    // var for password
    private String newPass = "";
    private String confirmPass = "";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AppCompatButton settingPassword;
    private AppCompatButton removePassword;
    private ImageView imgvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingPassword = findViewById(R.id.setting_password);
        removePassword = findViewById(R.id.remove_password);
        imgvBack = findViewById(R.id.imgvBack);

        settingPassword.setOnClickListener(this);
        removePassword.setOnClickListener(this);
        imgvBack.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case MyConst.REQCD_CONFIRMPASS: { // xác nhận lại password
                confirmPass = data.getStringExtra("newPassword");
                if (confirmPass.compareTo(newPass) == 0) { // nếu xác nhận đúng
                    Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_SHORT).show();
                    sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    editor = sp.edit();
                    editor.putString("password", newPass);
                    editor.commit();
                } else
                    Toast.makeText(getBaseContext(), "wrong confirm password", Toast.LENGTH_SHORT).show();
                break;
            }
            case MyConst.REQCD_SETPASS: { // đặt password mới
                newPass = data.getStringExtra("newPassword");
                Intent intent = new Intent(getBaseContext(), SecurityActivity.class);
                intent.setAction("confirm");
                startActivityForResult(intent, MyConst.REQCD_CONFIRMPASS);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_password: {
                Intent intent = new Intent(getBaseContext(), SecurityActivity.class); // gọi SecurityActivity
                intent.setAction("new_password");
                startActivityForResult(intent, MyConst.REQCD_SETPASS);
                break;
            }
            case R.id.remove_password: {
                // first: check xem đã có password chưa
                sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String currentPass = sp.getString("password", "");
                if (currentPass.compareTo("") == 0) // chưa có thì báo là chưa có
                    Snackbar.make(findViewById(R.id.remove_password), "You don't have password!", Snackbar.LENGTH_SHORT).show();
                else {
                    // hiển thị dialog hỏi người dùng có muốn xóa password cũ không
                    AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("Warning")
                            .setMessage("Are you sure to remove your password!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() { // nếu xóa thì commit pass = ""
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editor = sp.edit();
                                    editor.putString("password", "");
                                    editor.commit();
                                    Snackbar.make(findViewById(R.id.remove_password), "You've removed your password!", Snackbar.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();

                    alertDialog.show();

                }
                break;
            }
            case R.id.imgvBack:{
                finish();
                break;
            }
        }
    }
}

