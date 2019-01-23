package shyn.zyot.mytravels.traveldetail;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import shyn.zyot.mytravels.R;
import shyn.zyot.mytravels.base.BaseActivity;
import shyn.zyot.mytravels.base.MyConst;
import shyn.zyot.mytravels.entity.TravelBaseEntity;
import shyn.zyot.mytravels.entity.TravelExpense;
import shyn.zyot.mytravels.main.TravelListItemClickListener;
import shyn.zyot.mytravels.traveldetail.expense.TravelExpenseListAdapter;
import shyn.zyot.mytravels.traveldetail.expense.TravelExpenseViewModel;
import shyn.zyot.mytravels.utils.MoneyEditText;
import shyn.zyot.mytravels.utils.MyDate;
import shyn.zyot.mytravels.utils.MyItemTouchHelper;
import shyn.zyot.mytravels.utils.MyString;

public class ExpenseDetailActivity extends BaseActivity implements TravelListItemClickListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, MyItemTouchHelper.MyItemTouchHelperListner {
    private static final String TAG = ExpenseDetailActivity.class.getSimpleName();

    private final Calendar mCalendar = MyDate.getCurrentCalendar();
    private TextView mDateTxt;
    private TextView mTimeTxt;
    private TextView mPlaceTxt;
    private View mDescLayout;
    private EditText mTitleTxt;
    private EditText mDescTxt;
    private FloatingActionButton mFab;
    private MoneyEditText mAmountTxt;
    private Spinner mTypeSpin;
    private Spinner mCurrSpin;

    private boolean mInEditMode;
    private TravelExpenseViewModel mViewModel;
    private TravelExpenseListAdapter mListAdapter;

    @Override
    public void onBackPressed() {
        if (mInEditMode) {
            setEditMode(false);
            return;
        }
        super.onBackPressed();
    }

    private void setEditMode(boolean editMode) {
        mInEditMode = editMode;
        if (mInEditMode) {
            mDescLayout.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_done_white_24dp);
        } else {
            mDescLayout.setVisibility(View.GONE);
            mTitleTxt.setText(null);
            mDescTxt.setText(null);
            mPlaceTxt.setText(null);
            mAmountTxt.setText(null);
            mFab.setImageResource(R.drawable.ic_add_black_24dp);
            TravelExpense item = new TravelExpense();
            item.setId(0);
            item.setTravelId(mViewModel.currentItem.getValue().getTravelId());
            item.setDateTime(mViewModel.currentItem.getValue().getDateTime());
            item.setType(getResources().getStringArray(R.array.expense_key)[mTypeSpin.getSelectedItemPosition()]);
            item.setCurrency(getResources().getStringArray(R.array.currency_key)[mCurrSpin.getSelectedItemPosition()]);
            mViewModel.currentItem.setValue(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_txt: {
                DatePickerDialog dpd = new DatePickerDialog(this, this,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
            break;
            case R.id.time_txt: {
                TimePickerDialog tpd = new TimePickerDialog(this, this
                        , mCalendar.get(Calendar.HOUR_OF_DAY)
                        , mCalendar.get(Calendar.MINUTE)
                        , false);
                tpd.show();
            }
            break;
            case R.id.place_txt:
                showPlacePicker();
                break;
            case R.id.fab: {
                hideKeyboard();
                if (!mInEditMode) {
                    setEditMode(true);
                    return;
                }
                TravelExpense item = mViewModel.currentItem.getValue();
                setValuesFromEditText(item);
                if (item.getAmount() == 0 || MyString.isEmpty(item.getTitle())) {
                    Snackbar.make(v, R.string.expense_edit_warn, Snackbar.LENGTH_LONG).show();
                    return;
                }
                item.setDateTime(mCalendar.getTimeInMillis());
                Log.d(TAG, "item=" + item);
                mViewModel.insertItem(item);
                setEditMode(false);
            }
            break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(year, month, dayOfMonth);
        TravelExpense item = mViewModel.currentItem.getValue();
        setValuesFromEditText(item);
        item.setDateTime(mCalendar.getTimeInMillis());
        mViewModel.currentItem.setValue(item);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, 0);
        TravelExpense item = mViewModel.currentItem.getValue();
        setValuesFromEditText(item);
        item.setDateTime(mCalendar.getTimeInMillis());
        mViewModel.currentItem.setValue(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDateTxt = findViewById(R.id.date_txt);
        mTimeTxt = findViewById(R.id.time_txt);
        mPlaceTxt = findViewById(R.id.place_txt);
        mDescLayout = findViewById(R.id.desc_layout);
        mDescTxt = findViewById(R.id.desc_txt);
        mTitleTxt = findViewById(R.id.title_txt);
        mAmountTxt = findViewById(R.id.amount_txt);
        mTypeSpin = findViewById(R.id.type_spin);
        mCurrSpin = findViewById(R.id.currency_spin);

        mDateTxt.setText(MyDate.getDateString(mCalendar.getTime()));
        mTimeTxt.setText(MyDate.getTimeMinString(mCalendar.getTime()));
        mTitleTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_NEXT:
                        mDescTxt.requestFocus();
                        return true;
                }
                return false;
            }
        });
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(TravelExpenseViewModel.class);
        mViewModel.currentItem.observe(this, new Observer<TravelExpense>() {
            @Override
            public void onChanged(TravelExpense item) {
                Log.d(TAG, "onChanged: item=" + item);
                mCalendar.setTimeInMillis(item.getDateTimeLong());
                mDateTxt.setText(item.getDateTimeText());
                mTimeTxt.setText(item.getDateTimeHourMinText());
                mPlaceTxt.setText(item.getPlaceName());
                mTitleTxt.setText(item.getTitle());
                mDescTxt.setText(item.getDesc());
                mAmountTxt.setText(item.getAmountText());
                mTypeSpin.setSelection(MyConst.getBudgetCode(item.getType()).id);
                mCurrSpin.setSelection(MyConst.getCurrencyCode(item.getCurrency()).id);
            }
        });

        mListAdapter = new TravelExpenseListAdapter(this);
        mListAdapter.setListItemClickListener(this);
        mViewModel.travelExpenseList.observe(this, new Observer<PagedList<TravelExpense>>() {
            @Override
            public void onChanged(PagedList<TravelExpense> items) {
                mListAdapter.submitList(items);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mListAdapter);
        // attach MyItemTouchHelper
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        TravelExpense requestItem = (TravelExpense) getIntent().getExtras().getSerializable(MyConst.REQKEY_TRAVEL);
        Log.d(TAG, "onCreate: requestItem=" + requestItem);
        mViewModel.currentItem.setValue(requestItem);

        setEditMode(true);
    }

    @Override
    public void onListItemClick(View view, int position, TravelBaseEntity entity, boolean longClick) {
        TravelExpense item = (TravelExpense) entity;
        Log.d(TAG, "onListItemClick: item=" + item);
        if (!longClick) return;
        mViewModel.currentItem.setValue(item);
        setEditMode(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case MyConst.REQCD_PLACE_PICKER: {
                Place place = PlacePicker.getPlace(this, data);
                Log.d(TAG, "onActivityResult: place=" + place);
                TravelExpense item = mViewModel.currentItem.getValue();
                setValuesFromEditText(item);
                item.setPlaceId(place.getId());
                item.setPlaceName(place.getName().toString());
                item.setPlaceAddr(place.getAddress().toString());
                item.setPlaceLat(place.getLatLng().latitude);
                item.setPlaceLng(place.getLatLng().longitude);
                mViewModel.currentItem.setValue(item);
            }
            break;
        }
    }

    /**
     * Sets values from EditText
     */
    private void setValuesFromEditText(TravelExpense item) {
        item.setTitle(mTitleTxt.getText().toString().trim());
        item.setDesc(mDescTxt.getText().toString().trim());
        item.setAmount(mAmountTxt.getMoney());
        item.setType(getResources().getStringArray(R.array.expense_key)[mTypeSpin.getSelectedItemPosition()]);
        item.setCurrency(getResources().getStringArray(R.array.currency_key)[mCurrSpin.getSelectedItemPosition()]);
    }

    @Override
    public boolean onRequestItemViewSwipeEnabled() {
        // stop swipe-to-dismiss in edit mode
        return !mInEditMode;
    }

    @Override
    public void onItemDismiss(int position) {
        TravelExpense item = mListAdapter.getItem(position);
        Log.d(TAG, "onItemDismiss: item=" + item);
        item.setDeleteYn(true);
        mViewModel.updateItem(item);
        Snackbar.make(mFab, R.string.delete_warn_msg, Snackbar.LENGTH_INDEFINITE).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undeleleAllMarkedYes();
            }
        }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                switch (event) {
                    case BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_SWIPE:
                    case BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_TIMEOUT:
                        deleleAllMarkedYes();
                        break;
                }
            }
        }).show();
    }

    @Override
    public void finish() {
        deleleAllMarkedYes();
        super.finish();
    }

    // delete items marked as deleteYn=true
    private void deleleAllMarkedYes() {
        TravelExpense item = new TravelExpense();
        item.setId(-99);
        item.setTravelId(mViewModel.currentItem.getValue().getTravelId());
        mViewModel.deleteItem(item);
    }

    // undelete items marked as deleteYn=true
    private void undeleleAllMarkedYes() {
        TravelExpense item = new TravelExpense();
        item.setId(-99);
        item.setTravelId(mViewModel.currentItem.getValue().getTravelId());
        mViewModel.updateItem(item);
    }
}
