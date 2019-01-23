package shyn.zyot.mytravels.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.RadioButton;

import com.google.android.material.snackbar.Snackbar;

import shyn.zyot.mytravels.MainActivity;
import shyn.zyot.mytravels.R;

@TargetApi(Build.VERSION_CODES.M) // android >= marshmallow
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    RadioButton check1, check2, check3, check4;
    private Context context;

    public FingerprintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("There was an Auth Error. " + errString, false);

    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Auth Failed. ", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        this.update("You can now access the app.", true);

    }

    // hàm xử lý chính

    private void update(String s, boolean b) {
        check1 = ((Activity)context).findViewById(R.id.check1);
        check2 = ((Activity)context).findViewById(R.id.check2);
        check3 = ((Activity)context).findViewById(R.id.check3);
        check4 = ((Activity)context).findViewById(R.id.check4);

        if (b == false){ // vân tay sai
            check1.setChecked(true);
            check2.setChecked(true);
            check3.setChecked(true);
            check4.setChecked(true);
            Snackbar.make(((Activity) context).findViewById(R.id.zero_number),s,Snackbar.LENGTH_SHORT).show();
            check1.setChecked(false);
            check2.setChecked(false);
            check3.setChecked(false);
            check4.setChecked(false);
        }else { // vân tay đúng
            check1.setChecked(true);
            check2.setChecked(true);
            check3.setChecked(true);
            check4.setChecked(true);
            // mở activity
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }
}
