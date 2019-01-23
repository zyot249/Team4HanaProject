package shyn.zyot.mytravels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import shyn.zyot.mytravels.utils.FingerprintHandler;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener {

    Button zero, one, two, three, four, five, six, seven, eight, nine;
    Button delete;
    RadioButton check1, check2, check3, check4;
    private String password = "";
    private String input = "";
    SharedPreferences sp;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        password = sp.getString("password", "");

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

        // fingerprint

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {

                //mParaLabel.setText("Fingerprint Scanner not detected in Device");
                Snackbar.make(findViewById(R.id.zero_number), "Fingerprint Scanner not detected in Device", Snackbar.LENGTH_SHORT).show();

            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                //mParaLabel.setText("Permission not granted to use Fingerprint Scanner");
                Snackbar.make(findViewById(R.id.zero_number), "Permission not granted to use Fingerprint Scanner", Snackbar.LENGTH_SHORT).show();
            } else if (!keyguardManager.isKeyguardSecure()) {

                //mParaLabel.setText("Add Lock to your Phone in Settings");
                Snackbar.make(findViewById(R.id.zero_number), "Add Lock to your Phone in Settings", Snackbar.LENGTH_SHORT).show();
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {

                //mParaLabel.setText("You should add atleast 1 Fingerprint to use this Feature");
                Snackbar.make(findViewById(R.id.zero_number), "You should add atleast 1 Fingerprint to use this Feature", Snackbar.LENGTH_SHORT).show();
            } else {

                //mParaLabel.setText("Place your Finger on Scanner to Access the App.");
                Snackbar.make(findViewById(R.id.zero_number), "Place your Finger on Scanner to Access the App", Snackbar.LENGTH_SHORT).show();
                generateKey();

                if (cipherInit()) {

                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject);

                }
            }

        }
    }


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
            CheckPassword();
        }
    }

    private void CheckPassword() {
        if (input.compareTo(password) == 0) {
            input = "";
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            UpdateCheckBox();
            finish();
        } else {
            Snackbar.make(findViewById(R.id.zero_number), "Wrong Password", Snackbar.LENGTH_SHORT).show();
            input = "";
            UpdateCheckBox();
        }

        if (password.length() == 0) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zero_number: {
                input += "0";
                UpdateCheckBox();
                break;
            }
            case R.id.one_number: {
                input += "1";
                UpdateCheckBox();
                break;
            }
            case R.id.two_number: {
                input += "2";
                UpdateCheckBox();
                break;
            }
            case R.id.three_number: {
                input += "3";
                UpdateCheckBox();
                break;
            }
            case R.id.four_number: {
                input += "4";
                UpdateCheckBox();
                break;
            }
            case R.id.five_number: {
                input += "5";
                UpdateCheckBox();
                break;
            }
            case R.id.six_number: {
                input += "6";
                UpdateCheckBox();
                break;
            }
            case R.id.seven_number: {
                input += "7";
                UpdateCheckBox();
                break;
            }
            case R.id.eight_number: {
                input += "8";
                UpdateCheckBox();
                break;
            }
            case R.id.nine_number: {
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
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {

            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();

        }

    }
}
