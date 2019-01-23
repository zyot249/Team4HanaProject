package shyn.zyot.mytravels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SecurityActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton zero, one, two, three, four, five, six, seven, eight, nine;
    AppCompatButton delete, enter;
    AppCompatRadioButton check1, check2, check3, check4;
    TextView title;
    private String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        zero = findViewById(R.id.zero_number);
        one = findViewById(R.id.one_number);
        two = findViewById(R.id.two_number);
        three = findViewById(R.id.three_number);
        four = findViewById(R.id.four_number);
        five = findViewById(R.id.five_number);
        six = findViewById(R.id.six_number);
        seven = findViewById(R.id.seven_number);
        eight = findViewById(R.id.eight_number);
        nine = findViewById(R.id.nine_number);

        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check3 = findViewById(R.id.check3);
        check4 = findViewById(R.id.check4);

        delete = findViewById(R.id.delete);
        enter = findViewById(R.id.enter);
        title = findViewById(R.id.title);

        // get action to set title of action
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.compareTo("new_password") == 0) title.setText("Enter Your New Password");
        if (action.compareTo("confirm") == 0) title.setText("Confirm Your Password ");

        zero.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);

        delete.setOnClickListener(this);
        enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zero_number: {
                if (input.length() < 4)
                    input += "0";
                UpdateCheckBox();
                break;
            }
            case R.id.one_number: {
                if (input.length() < 4)
                    input += "1";
                UpdateCheckBox();
                break;
            }
            case R.id.two_number: {
                if (input.length() < 4)
                    input += "2";
                UpdateCheckBox();
                break;
            }
            case R.id.three_number: {
                if (input.length() < 4)
                    input += "3";
                UpdateCheckBox();
                break;
            }
            case R.id.four_number: {
                if (input.length() < 4)
                    input += "4";
                UpdateCheckBox();
                break;
            }
            case R.id.five_number: {
                if (input.length() < 4)
                    input += "5";
                UpdateCheckBox();
                break;
            }
            case R.id.six_number: {
                if (input.length() < 4)
                    input += "6";
                UpdateCheckBox();
                break;
            }
            case R.id.seven_number: {
                if (input.length() < 4)
                    input += "7";
                UpdateCheckBox();
                break;
            }
            case R.id.eight_number: {
                if (input.length() < 4)
                    input += "8";
                UpdateCheckBox();
                break;
            }
            case R.id.nine_number: {
                if (input.length() < 4)
                    input += "9";
                UpdateCheckBox();
                break;
            }
            case R.id.delete: {
                if (input.length() != 0) {
                    String newString = input.substring(0, input.length() - 1);
                    input = newString;
                }
                UpdateCheckBox();
                break;
            }
            case R.id.enter: {
                if (input.length() < 4){
                    Toast.makeText(getBaseContext(),"The Password must have 4 digits!",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("newPassword",input);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
            }
        }
    }

    // xử lý radio button
    private void UpdateCheckBox() {
        int num = input.length();
        if (num == 0) {
            check1.setChecked(false);
            check2.setChecked(false);
            check3.setChecked(false);
            check4.setChecked(false);
        }
        if (num == 1) {
            check1.setChecked(true);
            check2.setChecked(false);
            check3.setChecked(false);
            check4.setChecked(false);
        }
        if (num == 2) {
            check1.setChecked(true);
            check2.setChecked(true);
            check3.setChecked(false);
            check4.setChecked(false);
        }
        if (num == 3) {
            check1.setChecked(true);
            check2.setChecked(true);
            check3.setChecked(true);
            check4.setChecked(false);
        }
        if (num == 4) {
            check1.setChecked(true);
            check2.setChecked(true);
            check3.setChecked(true);
            check4.setChecked(true);
        }
    }


}
