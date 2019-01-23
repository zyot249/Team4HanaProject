package shyn.zyot.mytravels;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import shyn.zyot.mytravels.base.BaseActivity;
import shyn.zyot.mytravels.base.MyConst;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.main.TravelViewModel;
import shyn.zyot.mytravels.utils.MyDate;
import shyn.zyot.mytravels.utils.MyString;

public class EditTravelActivity extends BaseActivity
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = EditTravelActivity.class.getSimpleName();
    private TravelViewModel mTravelViewModel;

    private long mStartDt;
    private long mEndDt;
    private Place mPlace;
    private EditText mTitleEt;
    private TextView mStartDtEt;
    private TextView mEndDtEt;
    private EditText mPlaceEt;
    private Travel mTravel;
    private Button add_btn;
    /**
     * Whether it is a new mode or a edit mode.
     **/
    private boolean mInEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_travel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_btn = findViewById(R.id.add_btn);
        mTitleEt = findViewById(R.id.title_et);
        mStartDtEt = findViewById(R.id.start_dt);
        mEndDtEt = findViewById(R.id.end_dt);
//        findViewById(R.id.start_dt_layout).setOnClickListener(this);
        mStartDtEt.setOnClickListener(this);
//        findViewById(R.id.end_dt_layout).setOnClickListener(this);
        mEndDtEt.setOnClickListener(this);
        mPlaceEt = findViewById(R.id.place_et);
//        findViewById(R.id.place_layout).setOnClickListener(this);
        mPlaceEt.setOnClickListener(this);
        add_btn.setOnClickListener(this);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(MyConst.REQKEY_TRAVEL)) {
            mTravel = (Travel) getIntent().getExtras().getSerializable(MyConst.REQKEY_TRAVEL);
            mPlaceEt.setText(mTravel.getPlaceName());
        }
        if (MyConst.REQACTION_EDIT_TRAVEL.equals(getIntent().getAction())) {
            mInEditMode = true;
            setTitle(R.string.title_activity_edit_travel);
            mTitleEt.setText(mTravel.getTitle());
            mStartDt = mTravel.getDateTimeLong();
            mStartDtEt.setText(mTravel.getDateTimeText());
            mEndDt = mTravel.getEndDtLong();
            mEndDtEt.setText(mTravel.getEndDtText());
            add_btn.setText("Update");
        }


        mTravelViewModel = ViewModelProviders.of(this).get(TravelViewModel.class);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_btn:
                validate();
                break;

//            case R.id.start_dt_layout:
            case R.id.start_dt: {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(this, this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setTag(view.getId());
                if (mEndDt > 0) {
                    dpd.getDatePicker().setMaxDate(mEndDt);
                }
                dpd.show();
            }
            break;

//            case R.id.end_dt_layout:
            case R.id.end_dt: {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(this, this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setTag(view.getId());
                if (mStartDt > 0) {
                    dpd.getDatePicker().setMinDate(mStartDt);
                }
                dpd.show();
            }
            break;

//            case R.id.place_layout:
            case R.id.place_et: {
                showPlaceAutocomplete();
            }
            break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Object tag = view.getTag();
        Calendar calendar = Calendar.getInstance();
        if (tag.equals(R.id.start_dt)) {
            calendar.set(year, month, dayOfMonth, 0, 0, 0);
            if (mEndDt > 0 && mEndDt < calendar.getTimeInMillis()) return;
            mStartDt = calendar.getTimeInMillis();
            mStartDtEt.setText(MyDate.getDateString(calendar.getTime()));
        } else {
            calendar.set(year, month, dayOfMonth, 23, 59, 59);
            if (mStartDt > 0 && mStartDt > calendar.getTimeInMillis()) return;
            mEndDt = calendar.getTimeInMillis();
            mEndDtEt.setText(MyDate.getDateString(calendar.getTime()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case MyConst.REQCD_PLACE_AUTOCOMPLETE: {
                mPlace = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + mPlace);
                mPlaceEt.setText(mPlace.getName());
            }
            break;
        }
    }

    /**
     * validate user's inputs and add a new travel.
     */
    private void validate() {
        String title = mTitleEt.getText().toString();
        if (MyString.isEmpty(title)) {
            Snackbar.make(mTitleEt, R.string.travel_title_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!mInEditMode && MyString.isEmpty(mPlaceEt.getText().toString())) {
            Snackbar.make(mTitleEt, R.string.travel_city_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (mStartDt == 0) {
            Snackbar.make(mTitleEt, R.string.travel_startdt_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (mEndDt == 0) {
            Snackbar.make(mTitleEt, R.string.travel_enddt_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }
        // add a new travel.
        if (mTravel == null) {
            mTravel = new Travel(title);
        } else {
            mTravel.setTitle(title);
        }
        mTravel.setDateTime(mStartDt);
        mTravel.setEndDt(mEndDt);
        if (mPlace != null) {
            mTravel.setPlaceId(mPlace.getId());
            mTravel.setPlaceName((String) mPlace.getName());
            mTravel.setPlaceAddr((String) mPlace.getAddress());
            mTravel.setPlaceLat(mPlace.getLatLng().latitude);
            mTravel.setPlaceLng(mPlace.getLatLng().longitude);
        }
        if (mInEditMode) {
            // update a existing item
            mTravelViewModel.update(mTravel);
        } else {
            // insert a new travel
            mTravelViewModel.insert(mTravel);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mInEditMode) {
            getMenuInflater().inflate(R.menu.menu_edittravel, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_travel_del: {
                showAlertOkCancel(R.string.travel_del_title, R.string.travel_del_msg
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTravelViewModel.delete(mTravel);
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                        , null);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
