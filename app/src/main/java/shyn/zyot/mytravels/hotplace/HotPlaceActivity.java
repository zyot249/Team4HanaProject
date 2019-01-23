package shyn.zyot.mytravels.hotplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import shyn.zyot.mytravels.EditTravelActivity;
import shyn.zyot.mytravels.R;
import shyn.zyot.mytravels.base.BaseActivity;
import shyn.zyot.mytravels.base.MyConst;
import shyn.zyot.mytravels.entity.HotPlace;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.entity.TravelBaseEntity;
import shyn.zyot.mytravels.main.TravelListItemClickListener;
import shyn.zyot.mytravels.main.TravelViewModel;
import shyn.zyot.mytravels.utils.MyDate;

public class HotPlaceActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, TravelListItemClickListener, GoogleMap.OnInfoWindowClickListener {
    private static final String TAG = HotPlaceActivity.class.getSimpleName();

    private static final int DEFAULT_ZOOM = 13;
    // hanoi
    private LatLng mDefaultLocation = new LatLng(21.0277644, 105.8341598);
    private GoogleMap mMap;
    private Travel mTravel;
    private TravelViewModel mViewModel;
    private HotPlaceListAdapter mHotPlaceListAdapter;
    private RecyclerView mRecyclerView;
    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private SparseArray<Marker> mMarkers = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotplace);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTravel = new Travel("Hanoi");
        mTravel.setPlaceId("ChIJoRyG2ZurNTERqRfKcnt_iOc");
        mTravel.setPlaceName("Hanoi");
        mTravel.setPlaceAddr("Hanoi, Hoàn Kiếm, Hanoi, Vietnam");
        mTravel.setPlaceLat(mDefaultLocation.latitude);
        mTravel.setPlaceLng(mDefaultLocation.longitude);

        // google map options
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .mapToolbarEnabled(false)
                .liteMode(false);
        final SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);
        // Get the FragmentManager and start a transaction.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Add mapFragment.
        fragmentTransaction.add(R.id.fragment_container, mapFragment).commit();
        mapFragment.getMapAsync(this);
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this);

        mHotPlaceListAdapter = new HotPlaceListAdapter(this);
        mHotPlaceListAdapter.setListItemClickListener(this);
        mRecyclerView = findViewById(R.id.hot_place_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHotPlaceListAdapter);
        mViewModel = ViewModelProviders.of(this).get(TravelViewModel.class);
        mViewModel.getAllHotPlaces().observe(this, new Observer<PagedList<HotPlace>>() {
            @Override
            public void onChanged(PagedList<HotPlace> items) {
                mHotPlaceListAdapter.submitList(items);
                if (mMap != null) {
                    for (HotPlace item : items) {
                        LatLng p = new LatLng(item.getPlaceLat(), item.getPlaceLng());
                        // add marker
                        Marker marker = mMap.addMarker(new MarkerOptions().position(p).title(item.getPlaceName()));
                        marker.setTag(item);
                        if (item.getId() == 1) {
                            // hanoi
                            marker.showInfoWindow();
                        }
                        mMarkers.put((int) item.getId(), marker);
                    }
                }
            }
        });

        mViewModel.geHotPlaceToDownload(MyDate.getCurrentDate()).observe(this, new Observer<HotPlace>() {
            @Override
            public void onChanged(HotPlace item) {
                if (item != null) {
                    Log.d(TAG, "item=" + item);
                    getPhotos(item);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hotplace, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_new_travel: {
                // call EditTravelActivity
                Intent intent = new Intent(this, EditTravelActivity.class);
                intent.putExtra(MyConst.REQKEY_TRAVEL, mTravel);
                startActivityForResult(intent, MyConst.REQCD_TRAVEL_ADD);
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        // move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_restaurants:
                openGoogleMap(v, mTravel, "restaurants");
                break;
            case R.id.btn_hotels:
                openGoogleMap(v, mTravel, "hotels");
                break;
            case R.id.btn_attractions:
                openGoogleMap(v, mTravel, "attractions");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case MyConst.REQCD_TRAVEL_ADD:
                finish();
                break;
        }
    }

    // Request photos and metadata for the specified place.
    private void getPhotos(final HotPlace item) {
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(item.getPlaceId());
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                if (photoMetadataBuffer.getCount() == 0) {
                    mViewModel.saveHotPlaceImage(item, null);
                    photoMetadataBuffer.release();
                    return;
                }
                // Get the first photo in the list.
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                // Get the attribution text.
                item.setDesc(photoMetadata.getAttributions().toString());
                // Get a scaled bitmap for the photo.
                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getScaledPhoto(photoMetadata, 150, 150);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        mViewModel.saveHotPlaceImage(item, photo.getBitmap());
                    }
                });
                photoMetadataBuffer.release();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object object = marker.getTag();
        if (object instanceof HotPlace) {
            HotPlace item = (HotPlace) object;
            mHotPlaceListAdapter.setSelectedPosition((int) item.getId() - 1);
            mRecyclerView.smoothScrollToPosition((int) item.getId() - 1);
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Object object = marker.getTag();
        if (object instanceof HotPlace) {
            HotPlace item = (HotPlace) object;
            openGoogleMap(mRecyclerView, item, null);
        }
    }

    @Override
    public void onListItemClick(View view, int position, TravelBaseEntity entity, boolean longClick) {
        HotPlace item = (HotPlace) entity;
        if (!longClick & mMap != null) {
            LatLng p = new LatLng(item.getPlaceLat(), item.getPlaceLng());
            // move the camera
            Marker marker = mMarkers.get((int) item.getId());
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, DEFAULT_ZOOM));
            mHotPlaceListAdapter.setSelectedPosition(position);
        }
    }
}
