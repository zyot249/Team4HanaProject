package shyn.zyot.mytravels.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Locale;

import androidx.appcompat.widget.AppCompatEditText;

public class MoneyEditText extends AppCompatEditText {
    private static final String TAG = MoneyEditText.class.getSimpleName();
    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        /**
         * Separates the editText input with comma while user is entering the value
         *
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {
            MoneyEditText.this.removeTextChangedListener(this);

            String str = s.toString().trim();
            double val = 0;
            if (str.length() > 0) {
                str = str.replace(",", "");
                if (str.length() > 0) {
                    try {
                        val = Double.parseDouble(str);
                        if (val >= Integer.MAX_VALUE) val = (int) (val / 10f);
                        if (!str.contains(".")) {
                            str = String.format(Locale.ENGLISH, "%,.0f", val);
                        } else if (str.charAt(str.length() - 1) == '.') {
                            str = String.format(Locale.ENGLISH, "%,.0f.", val);
                        } else {
                            int digit = str.length() - str.lastIndexOf('.');
                            if (digit > 4) digit = 4;
                            str = String.format(Locale.ENGLISH, "%,.6f", val);
                            str = str.substring(0, str.lastIndexOf('.') + digit);
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }
            //setting text after format to EditText
            setText(str);
            setSelection(getText().length());

            MoneyEditText.this.addTextChangedListener(this);
        }
    };

    public MoneyEditText(Context context) {
        super(context);
        init();
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(mTextWatcher);
    }

    /**
     * Returns the numeric value of the input text.
     * @return
     */
    public double getMoney() {
        double val = 0;
        try {
            String str = getText().toString().trim();
            if (str.length() == 0) return val;
            str = str.replace(",", "");
            val = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return val;
    }

}
