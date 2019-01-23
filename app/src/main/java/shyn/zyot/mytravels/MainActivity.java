package shyn.zyot.mytravels;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import shyn.zyot.mytravels.base.BaseActivity;
import shyn.zyot.mytravels.base.MyApplication;
import shyn.zyot.mytravels.base.MyConst;
import shyn.zyot.mytravels.base.TravelSort;
import shyn.zyot.mytravels.database.AppDatabase;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.entity.TravelBaseEntity;
import shyn.zyot.mytravels.hotplace.HotPlaceActivity;
import shyn.zyot.mytravels.main.TravelListAdapter;
import shyn.zyot.mytravels.main.TravelListItemClickListener;
import shyn.zyot.mytravels.main.TravelViewModel;
import shyn.zyot.mytravels.utils.MyString;

public class MainActivity extends BaseActivity implements TravelListItemClickListener, View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mToolbarLayout;
    private TravelViewModel mTravelViewModel;
    private TravelListAdapter mTravelListAdapter;
    private static final String EMAIL = "email";
    private LoginButton loginButton;
    private AccessToken accessToken;
    CallbackManager callbackManager;
    private final Observer<List<Travel>> mTravelObserver = new Observer<List<Travel>>() {
        @Override
        public void onChanged(List<Travel> travels) {
            Log.d(TAG, "onChanged: travels.size=" + travels.size());
            mTravelListAdapter.setList(travels);
            if (((MyApplication) getApplication()).getMainActivityShowInfo() && travels.size() > 0) {
                ((MyApplication) getApplication()).setTravelListSize(travels.size());
                Snackbar.make(findViewById(R.id.fab), R.string.travel_longclick, Snackbar.LENGTH_LONG)
                        .setAction(R.string.dont_show_again, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((MyApplication) getApplication()).setMainActivityShowInfo(false);
                            }
                        })
                        .show();
            }
        }
    };

    ///// Phung
    private ConstraintLayout UpComingLayout;
    private TextView tvUpcoming;
    private TextView tvNextTravel;
    Travel upComingTravel;
    boolean nowHappening;

    @Override
    protected void onResume() {
        updateUpComing();
        super.onResume();
    }

    private void updateUpComing() {
        new updateUpComingAsyncTask().execute();
    }

    private class updateUpComingAsyncTask extends AsyncTask<Void, Void, Void> {
        boolean foundUpComing = false;
        @Override
        protected Void doInBackground(Void... voids) {
            String cur_date ;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            cur_date = df.format(c.getTime());
            cur_date = cur_date.substring(0,8)+"000000";
            // -> tính current date dưới dạng: yyyyMMdd000000  -> vì khi tạo travel chỉ lấy ngày tháng năm không dùng giờ.

            // Xét trong tất cả các travel (đã được xếp ngày bắt đầu tăng dần) -> xét từ ngày bắt đầu sớm nhất đến khi thấy travel đầu tiên thõa mãn thì dừng
            List<Travel> listTravel = AppDatabase.getDatabase(getBaseContext()).travelDao().getAllTravelsByStartDateAsc();
            for (Travel travel: listTravel) {
                // so sánh ngày bắt đầu của travel hiện tại với ngày hiện tại.
                int compareStartDate = travel.getDateTime().compareTo(String.valueOf(cur_date));

                if ( compareStartDate > 0) {
                    // if date of this travel > current date  ----> upcoming
                    upComingTravel = travel;
                    foundUpComing = true;
                    nowHappening = false;
                    break;
                }
                else if (compareStartDate == 0) {
                    // if start date of this travel = current date  ----> today is the start date
                    upComingTravel = travel;
                    foundUpComing = true;
                    nowHappening = true;
                    break;
                }
                else if (travel.getEndDt().compareTo(String.valueOf(cur_date)) > 0 ) {
                    // if start date of this travel < current date   &&  end date of this travel > current date ---->  now happening
                    upComingTravel = travel;
                    foundUpComing = true;
                    nowHappening = true;
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String result;
            if (!foundUpComing) {
                upComingTravel = null;
                tvUpcoming.setVisibility(View.GONE);
                tvNextTravel.setText("\tYou have no upcoming travel!");
            }
            else if (!nowHappening){
                tvUpcoming.setText("Upcoming Travel:");
                tvUpcoming.setVisibility(View.VISIBLE);
                result =  upComingTravel.getTitle();
                tvNextTravel.setText(result);
            }
            else {
                tvUpcoming.setText("Now Happening:");
                tvUpcoming.setVisibility(View.VISIBLE);
                result =  upComingTravel.getTitle();
                tvNextTravel.setText(result);
            }
            super.onPostExecute(aVoid);
        }
    }


    /////

    @Override
    public void onListItemClick(View view, int position, TravelBaseEntity entity, boolean longClick) {
        Travel item = (Travel) entity;
        Log.d(TAG, "onListItemClick: item=" + item);
        Log.d(TAG, "onListItemClick: longClick=" + longClick);
        if (longClick) {
            // call EditTravelActivity with a selected Travel entity.
            Intent intent = new Intent(this, EditTravelActivity.class);
            intent.putExtra(MyConst.REQKEY_TRAVEL, item);
            intent.setAction(MyConst.REQACTION_EDIT_TRAVEL);
            startActivityForResult(intent, MyConst.REQCD_TRAVEL_EDIT);
        } else {
            // call TravelDetailActivity
            Intent intent = new Intent(MainActivity.this, TravelDetailActivity.class);
            intent.putExtra(MyConst.REQKEY_TRAVEL_ID, item.getId());
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        //login facebook team2
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", EMAIL);
        loginWithFacebook();
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//                        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//                        if (isLoggedIn) {
//                            Snackbar.make(loginButton, "Login Successful", Snackbar.LENGTH_LONG).show();
//                            Log.d("Login", "aaaa");
//
//                        } else {
//                            Snackbar.make(loginButton, "Login failed", Snackbar.LENGTH_LONG).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        Log.d("Login", "Error");
//                    }
//                });



        // navigation
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.team4_navigation_drawer_open, R.string.team4_navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();{

                    if(id == R.id.nav_gallery) {
                        Log.d("Clicked", "");
                        Intent intent = new Intent(getBaseContext(), Team2GalleryActivity.class);
                        startActivity(intent);
                    }
                    if (id == R.id.nav_setting){
                        Intent intent = new Intent(getBaseContext(),SettingActivity.class);
                        startActivity(intent);
                    }

                }
                drawer.closeDrawers();
                return false;
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call EditTravelActivity
                Intent intent = new Intent(MainActivity.this, EditTravelActivity.class);
                startActivityForResult(intent, MyConst.REQCD_TRAVEL_ADD);
            }
        });
        mTravelListAdapter = new TravelListAdapter(this);
        mTravelListAdapter.setListItemClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(mTravelListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTravelViewModel = ViewModelProviders.of(this).get(TravelViewModel.class);
        mTravelViewModel.getAllTravels().observe(this, mTravelObserver);
        mTravelViewModel.setTravelSort(((MyApplication) getApplication()).getTravelSort());

        mAppBarLayout = findViewById(R.id.app_bar);
        // set main image
        if (((MyApplication) getApplication()).getMainImageUri() != null) {
            Uri uri = ((MyApplication) getApplication()).getMainImageUri();
            if (!uri.equals(Uri.EMPTY)) {
                mAppBarLayout.setBackground(Drawable.createFromPath(uri.getPath()));
            }
        }
        mToolbarLayout = findViewById(R.id.toolbar_layout);
        // set main title
        if (MyString.isNotEmpty(((MyApplication) getApplication()).getMainTitle())) {
            mToolbarLayout.setTitle(((MyApplication) getApplication()).getMainTitle());
        }

        UpComingLayout = findViewById(R.id.UpComingLayout);
        tvUpcoming = findViewById(R.id.tvUpComing);
        tvNextTravel = findViewById(R.id.tvNextTravel);
        UpComingLayout.setOnClickListener(this);
        tvUpcoming.setOnClickListener(this);
        tvNextTravel.setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //login facebook team2
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: requestCode=" + requestCode);
        Log.d(TAG, "onActivityResult: resultCode=" + resultCode);
        Log.d(TAG, "onActivityResult: data=" + data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case MyConst.REQCD_IMAGE_CROP: {
                Uri cropImagePath = getCropImagePath();
                File bgFile = new File(cropImagePath.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(new File(MainActivity.this.getFilesDir()+"/mytravel/"+bgFile.getName()).getPath());
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                mAppBarLayout.setBackground(d);

                Log.d(TAG, "onActivityResult: cropImagePath=" + cropImagePath);
                if (cropImagePath == null) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Failed to load a image.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                // copy the cropped image to app's private directory.
                final File srcFile = new File(cropImagePath.getPath());
                if (!srcFile.exists()) {
                    Log.e(TAG, "Not Exist: " + srcFile.getAbsolutePath());
                    return;
                }
                final File targetFile = new File(getFilesDir(), "main_img.jpg");
                FileChannel sourceChannel = null;
                FileChannel destChannel = null;
                try {
                    sourceChannel = new FileInputStream(srcFile).getChannel();
                    destChannel = new FileOutputStream(targetFile).getChannel();
                    destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                    ((MyApplication) getApplication()).setMainImageUri(Uri.fromFile(targetFile));
                    mAppBarLayout.setBackground(Drawable.createFromPath(targetFile.getAbsolutePath()));
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                } finally {
                    try {
                        if (sourceChannel != null) sourceChannel.close();
                        srcFile.delete();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    try {
                        if (destChannel != null) destChannel.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        TravelSort travelSort = ((MyApplication) getApplication()).getTravelSort();
        switch (travelSort) {
            case DEFAULT:
                menu.findItem(R.id.action_sort_default).setChecked(true);
                break;
            case TITLE_ASC:
                menu.findItem(R.id.action_sort_travel_asc).setChecked(true);
                break;
            case TITLE_DESC:
                menu.findItem(R.id.action_sort_travel_desc).setChecked(true);
                break;
            case START_ASC:
                menu.findItem(R.id.action_sort_start_asc).setChecked(true);
                break;
            case START_DESC:
                menu.findItem(R.id.action_sort_start_desc).setChecked(true);
                break;
        }

        final MenuItem changeTitleMenu = menu.findItem(R.id.action_change_title);
        SearchView searchView = (SearchView) changeTitleMenu.getActionView();
        searchView.setQueryHint("title");
        searchView.setSubmitButtonEnabled(false);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (MyString.isNotEmpty(query)) {
                    query = query.trim();
                    ((MyApplication) getApplication()).setMainTitle(query);
                    mToolbarLayout.setTitle(query);
                }
                changeTitleMenu.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_default:
                mTravelViewModel.setTravelSort(TravelSort.DEFAULT);
                item.setChecked(true);
                return true;
            case R.id.action_sort_travel_asc:
                mTravelViewModel.setTravelSort(TravelSort.TITLE_ASC);
                item.setChecked(true);
                return true;
            case R.id.action_sort_travel_desc:
                mTravelViewModel.setTravelSort(TravelSort.TITLE_DESC);
                item.setChecked(true);
                return true;
            case R.id.action_sort_start_asc:
                mTravelViewModel.setTravelSort(TravelSort.START_ASC);
                item.setChecked(true);
                return true;
            case R.id.action_sort_start_desc:
                mTravelViewModel.setTravelSort(TravelSort.START_DESC);
                item.setChecked(true);
                return true;
            case R.id.action_change_title:
                return true;
            case R.id.action_change_photo_camera:
                requestPermissions(MyConst.REQCD_ACCESS_CAMERA);
                return true;
            case R.id.action_change_photo_gallery:
                requestPermissions(MyConst.REQCD_ACCESS_GALLERY);
                return true;
            case R.id.action_search:
//                startActivityForResult(new Intent(this, SearchableActivity.class)
//                        , MyConst.REQCD_SEARCH_TRAVELS_BY_CITY);
                Intent intent = new Intent(getBaseContext(),SearchableActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subtitle_txt:
                startActivity(new Intent(MainActivity.this, HotPlaceActivity.class));
                break;
            case R.id.UpComingLayout:
            case R.id.tvUpComing:
            case R.id.tvNextTravel: {
                if (upComingTravel != null) {
                    // call TravelDetailActivity
                    Intent intent = new Intent(MainActivity.this, TravelDetailActivity.class);
                    intent.putExtra(MyConst.REQKEY_TRAVEL_ID, upComingTravel.getId());
                    startActivity(intent);
                }
            }
            break;
        }
    }

    @Override
    protected void postRequestPermissionsResult(int reqCd, boolean result) {
        if (!result) {
            Snackbar.make(findViewById(R.id.fab), R.string.permission_not_granted, Snackbar.LENGTH_LONG).show();
            return;
        }
        switch (reqCd) {
            case MyConst.REQCD_ACCESS_CAMERA:
                takePhotoFromCamera();
                break;
            case MyConst.REQCD_ACCESS_GALLERY:
                takePhotoFromGallery();
                break;
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.nav_gallery){
            Log.d("Clicked","");
            Intent intent = new Intent(getBaseContext(),Team2GalleryActivity.class);
            startActivity(intent);
        }else{
            Log.d("Clicked","");
            Intent intent = new Intent(getBaseContext(),Team2GalleryActivity.class);
            startActivity(intent);
        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    void loginWithFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (isLoggedIn) {
                    Snackbar.make(loginButton, "LOGIN SUCCESSFUL", Snackbar.LENGTH_LONG).show();
                    loginButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Snackbar.make(loginButton, "LOGIN FAILED", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
