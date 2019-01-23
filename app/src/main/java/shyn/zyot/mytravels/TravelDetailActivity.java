package shyn.zyot.mytravels;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import shyn.zyot.mytravels.Team4.BottomsheetItemHandler;
import shyn.zyot.mytravels.Team4.Team4BottomSheetFragment;
import shyn.zyot.mytravels.base.BaseActivity;
import shyn.zyot.mytravels.base.MyApplication;
import shyn.zyot.mytravels.base.MyConst;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.entity.TravelDiary;
import shyn.zyot.mytravels.traveldetail.SectionsPagerAdapter;
import shyn.zyot.mytravels.traveldetail.TravelDetailBaseFragment;
import shyn.zyot.mytravels.traveldetail.TravelDetailViewModel;

public class TravelDetailActivity extends BaseActivity {
    private static final String TAG = TravelDetailActivity.class.getSimpleName();

    private SectionsPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView mSubtitle;
    private AppBarLayout mAppBar;
    private Button btnAdd;
    private final Observer<Travel> mTravelObserver = new Observer<Travel>() {
        @Override
        public void onChanged(Travel travel) {
            Log.d(TAG, "onChanged: travel=" + travel);
            if (travel == null) return;
            mToolbarLayout.setTitle(travel.getTitle());
            mSubtitle.setText(travel.getPlaceName() + "\n" + travel.getDateTimeText() + "~" + travel.getEndDtText());
        }
    };
    private TravelDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppBar = findViewById(R.id.app_bar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mViewPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        //  mViewPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        final TabLayout tabLayout = findViewById(R.id.tabs);
        // Use setupWithViewPager(ViewPager) to link a TabLayout with a ViewPager.
        // The individual tabs in the TabLayout will be automatically populated
        // with the page titles from the PagerAdapter.
        tabLayout.setupWithViewPager(mViewPager);


        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Team4BottomSheetFragment bottomSheetFragment = new Team4BottomSheetFragment();
                bottomSheetFragment.setItemHandler(new BottomsheetItemHandler() {
                    @Override
                    public void onItemClick(View view) {
                        switch (view.getId()) {
                            case R.id.LLAddDailyPlan:
                                AddInfor(0);
                                break;
                            case R.id.LLAddDiary:
                                AddInfor(1);
                                break;
                            case R.id.LLAddExpenses:
                                AddInfor(2);
                                break;
                        }
                    }

                    private void AddInfor(int postition) {
                        TravelDetailBaseFragment fragment = (TravelDetailBaseFragment) mViewPagerAdapter.getRegisteredFragment(postition);
                        fragment.requestAddItem();
                        bottomSheetFragment.dismiss();
                    }
//

                });
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });


        mToolbarLayout = findViewById(R.id.toolbar_layout);
        mSubtitle = findViewById(R.id.subtitle_txt);

        long travelId = getIntent().getLongExtra(MyConst.REQKEY_TRAVEL_ID, 0);
        Log.d(TAG, "onCreate: travelId=" + travelId);

        mViewModel = ViewModelProviders.of(this).get(TravelDetailViewModel.class);
        mViewModel.setTravelId(travelId);
        mViewModel.getTravel().observe(this, mTravelObserver);

        mViewModel.recentDiaryImage.observe(this, new Observer<TravelDiary>() {
            @Override
            public void onChanged(TravelDiary item) {
                if (item == null) {
                    if (((MyApplication) getApplication()).getMainImageUri() != null) {
                        Uri uri = ((MyApplication) getApplication()).getMainImageUri();
                        if (!uri.equals(Uri.EMPTY)) {
                            mAppBar.setBackground(Drawable.createFromPath(uri.getPath()));
                        }
                    }
                    return;
                }
//                if (MyString.isNotEmpty(item.getImgUri())) {
//                    mAppBar.setBackground(Drawable.createFromPath(Uri.parse(item.getImgUri()).getPath()));
//                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return super.onCreateOptionsMenu(menu);
    }
}
