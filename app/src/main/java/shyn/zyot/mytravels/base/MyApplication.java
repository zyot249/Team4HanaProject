package shyn.zyot.mytravels.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.facebook.stetho.Stetho;

import shyn.zyot.mytravels.R;
import shyn.zyot.mytravels.utils.MyString;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    private static final String TRAVEL_SORT_NAME = "TRAVEL_SORT_NAME";
    private static final String TRAVEL_SORT_KEY = "TRAVEL_SORT_KEY";
    private static final String TRAVEL_LONGCLICK = "TRAVEL_LONGCLICK";
    private static final String TRAVEL_LONGCLICK_DONT_SHOW = "TRAVEL_LONGCLICK_DONT_SHOW";
    private static final String TRAVEL_MAIN_IMG = "TRAVEL_MAIN_IMG";
    private static final String TRAVEL_MAIN_TITLE = "TRAVEL_MAIN_TITLE";
    private TravelSort mTravelSort;
    private Boolean mMainActivityShowInfo;
    private Uri mMainImageUri;
    private String mMainTitle;
    private int listSize;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        String[] itemKey = getResources().getStringArray(R.array.currency_key);
        String[] itemLabel = getResources().getStringArray(R.array.currency_label);
        for (int i = 0; i < itemKey.length; i++) {
            MyConst.CURRENCY_CODE.put(itemKey[i], new MyKeyValue(i, itemKey[i], itemLabel[i]));
        }

        itemKey = getResources().getStringArray(R.array.expense_key);
        itemLabel = getResources().getStringArray(R.array.expense_label);
        for (int i = 0; i < itemKey.length; i++) {
            MyConst.BUDGET_CODE.put(itemKey[i], new MyKeyValue(i, itemKey[i], itemLabel[i]));
        }

        // init variables
        getTravelSort();
        getMainActivityShowInfo();
        getMainImageUri();
        getMainTitle();

        Stetho.initializeWithDefaults(this);
    }

    /**
     * Returns the sorting option selected by the user from SharedPreferences.
     *
     * @return the selected sorting option.
     */
    public TravelSort getTravelSort() {
        if (mTravelSort == null) {
            SharedPreferences sharedPref = getSharedPreferences(TRAVEL_SORT_NAME, Context.MODE_PRIVATE);
            String name = sharedPref.getString(TRAVEL_SORT_KEY, TravelSort.DEFAULT.name());
            mTravelSort = TravelSort.DEFAULT;
            try {
                mTravelSort = TravelSort.valueOf(name);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return mTravelSort;
    }


    /**
     * Saves the sorting option selected in SharedPreferences.
     *
     * @param travelSort the sorting option.
     */
    public void setTravelSort(TravelSort travelSort) {
        SharedPreferences sharedPref = getSharedPreferences(TRAVEL_SORT_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TRAVEL_SORT_KEY, travelSort.name());
        editor.apply();
        mTravelSort = travelSort;
    }

    /**
     * Decides whether to display a prompt message.
     */
    public boolean getMainActivityShowInfo() {
        if (mMainActivityShowInfo == null) {
            SharedPreferences sharedPref = getSharedPreferences(TRAVEL_LONGCLICK, Context.MODE_PRIVATE);
            mMainActivityShowInfo = sharedPref.getBoolean(TRAVEL_LONGCLICK_DONT_SHOW, true);
        }
        return mMainActivityShowInfo;
    }

    public void setMainActivityShowInfo(boolean dontShow) {
        SharedPreferences sharedPref = getSharedPreferences(TRAVEL_LONGCLICK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(TRAVEL_LONGCLICK_DONT_SHOW, dontShow);
        editor.apply();
        mMainActivityShowInfo = dontShow;
    }

    /**
     * returns the URI of the main photo
     */
    public Uri getMainImageUri() {
        if (mMainImageUri == null) {
            SharedPreferences sharedPref = getSharedPreferences(TRAVEL_MAIN_IMG, Context.MODE_PRIVATE);
            String uriString = sharedPref.getString(TRAVEL_MAIN_IMG, "");
            if (MyString.isEmpty(uriString)) mMainImageUri = Uri.EMPTY;
            else mMainImageUri = Uri.parse(uriString);

        }
        return mMainImageUri;
    }

    public void setMainImageUri(Uri uri) {
        SharedPreferences sharedPref = getSharedPreferences(TRAVEL_MAIN_IMG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TRAVEL_MAIN_IMG, uri.toString());
        editor.apply();
        mMainImageUri = uri;
    }

    /**
     * returns main title
     */
    public String getMainTitle() {
        if (mMainTitle == null) {
            SharedPreferences sharedPref = getSharedPreferences(TRAVEL_MAIN_TITLE, Context.MODE_PRIVATE);
            mMainTitle = sharedPref.getString(TRAVEL_MAIN_TITLE, "");
        }
        return mMainTitle;
    }

    public void setMainTitle(String title) {
        SharedPreferences sharedPref = getSharedPreferences(TRAVEL_MAIN_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TRAVEL_MAIN_TITLE, title);
        editor.apply();
        mMainTitle = title;
    }

    public void setTravelListSize(int size) {
        SharedPreferences sp = getSharedPreferences(MyConst.TRAVEL_LIST_SIZE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(MyConst.TRAVEL_LIST_SIZE, size);
        editor.apply();
        this.listSize = size;
    }

    public int getTravelListSize() {
        SharedPreferences sp = getSharedPreferences(MyConst.TRAVEL_LIST_SIZE, Context.MODE_PRIVATE);
        int size = sp.getInt(MyConst.TRAVEL_LIST_SIZE, 0);
        this.listSize = size;
        return size;
    }
}
