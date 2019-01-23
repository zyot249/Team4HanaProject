package shyn.zyot.mytravels.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import shyn.zyot.mytravels.BuildConfig;
import shyn.zyot.mytravels.R;
import shyn.zyot.mytravels.entity.TravelBaseEntity;
import shyn.zyot.mytravels.utils.ImageViewerDialog;
import shyn.zyot.mytravels.utils.MyString;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private Uri mCurrentPhotoUri;

    public Uri getCropImagePath() {
        return mCurrentPhotoUri;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    /**
     * Displays a common alert dialog with Ok and Cancel buttons.
     *
     * @param titleId             title string resource id
     * @param messageId           message string resource id
     * @param okClickListener     the callback when the ok button is clicked
     * @param cancelClickListener the callback when the cancel button is clicked
     */
    protected void showAlertOkCancel(@StringRes int titleId
            , @StringRes int messageId
            , final DialogInterface.OnClickListener okClickListener
            , final DialogInterface.OnClickListener cancelClickListener) {
        new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(android.R.string.ok, okClickListener)
                .setNegativeButton(android.R.string.cancel, cancelClickListener)
                .show();
    }

    protected void showPlacePicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), MyConst.REQCD_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed
            // or not up to date.
            // Prompt the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this,
                    e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available
            // and the problem is not easily resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, message, e);
            Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    protected void showPlaceAutocomplete() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, MyConst.REQCD_PLACE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed
            // or not up to date.
            // Prompt the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this,
                    e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available
            // and the problem is not easily resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, message, e);
            Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Take a photo with a gallery app
     */
    protected void takePhotoFromGallery() {
        mCurrentPhotoUri = null;//
        // Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        if (intent.resolveActivity(getPackageManager()) == null) {
//            Snackbar.make(getWindow().getDecorView().getRootView(), "Your device does not have a gallery.", Snackbar.LENGTH_LONG).show();
//            return;
//        }
//        startActivityForResult(intent, MyConst.REQCD_IMAGE_GALLERY);

        Intent intent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }
        else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Pick"),MyConst.REQCD_IMAGE_GALLERY);

    }

    /**
     * Take a photo with a camera app
     */
    protected void takePhotoFromCamera() {
        mCurrentPhotoUri = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = createImageFile();
            Log.d(TAG, "takePhotoFromCamera: mCurrentPhotoUri=" + mCurrentPhotoUri);
            // use a FileProvider
            Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
            Log.d(TAG, "takePhotoFromCamera: photoURI=" + photoURI);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            if (intent.resolveActivity(getPackageManager()) == null) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Your device does not have a camera.", Snackbar.LENGTH_LONG).show();
                return;
            }
            // grant read/write permissions to other apps.
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, MyConst.REQCD_IMAGE_CAMERA);
        } catch (IOException e) {
            // Error occurred while creating the File
            Log.e(TAG, e.getMessage(), e);
            Snackbar.make(getWindow().getDecorView().getRootView(), "Cannot create an image file.", Snackbar.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * Create an image file name
     *
     * @return
     * @throws IOException
     */
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File storageDir = new File(getFilesDir(), "mytravel");
        if (!storageDir.exists()) storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoUri = Uri.fromFile(image);
        Log.d(TAG, "createImageFile: mCurrentPhotoUri=" + mCurrentPhotoUri);
        return image;
    }

    protected void cropImage(Uri srcUri) {
        mCurrentPhotoUri = null;
        if (srcUri == null) return;
        Log.d(TAG, "cropImage: srcUri=" + srcUri);
        try {
            File outputFile = createImageFile();
            mCurrentPhotoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", outputFile);
            Log.d(TAG, "cropImage: outputFile=" + outputFile.getAbsolutePath());
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(srcUri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("outputX", 1080);
            intent.putExtra("outputY", 1080);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("max-width", 1080);
            intent.putExtra("max-height", 1080);
//            intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.setClipData(ClipData.newRawUri("", mCurrentPhotoUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, MyConst.REQCD_IMAGE_CROP);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    protected Uri copyCropImageForTravel(long travelId) {
        if (mCurrentPhotoUri == null) return null;

        final File srcFile = new File(mCurrentPhotoUri.getPath());
//        mCurrentPhotoUri = null;
//        if (!srcFile.exists()) {
//            Log.e(TAG, "Not Exist: " + srcFile.getAbsolutePath());
//            return null;
//        }
        final File rootDir = new File(getFilesDir(), "t" + travelId);
        if (!rootDir.exists()) rootDir.mkdirs();
        final File targetFile = new File(getFilesDir()+"/mytravel", srcFile.getName());
        final File thumbFile = new File(rootDir, MyConst.THUMBNAIL_PREFIX + srcFile.getName());
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        FileOutputStream thumbFos = null;
        try {
            // copy file
            sourceChannel = new FileInputStream(targetFile).getChannel();
            destChannel = new FileOutputStream(thumbFile).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            // make a thumbnail image
            thumbFos = new FileOutputStream(thumbFile);
            Bitmap imageBitmap = BitmapFactory.decodeFile(targetFile.getAbsolutePath());
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, MyConst.THUMBNAIL_SIZE, MyConst.THUMBNAIL_SIZE, false);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, thumbFos);
            thumbFos.flush();

            mCurrentPhotoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", targetFile);
            return mCurrentPhotoUri;
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
            try {
                if (thumbFos != null) thumbFos.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode);
        if (resultCode != RESULT_OK) {
            if (mCurrentPhotoUri != null) {
                // delete unused temporary files
                File file = new File(mCurrentPhotoUri.getPath());
                file.delete();
            }
            return;
        }
        switch (requestCode) {
            case MyConst.REQCD_IMAGE_CAMERA: {
                if (mCurrentPhotoUri == null) return;
                Log.d(TAG, "onActivityResult: mCurrentPhotoUri=" + mCurrentPhotoUri);
                // use a FileProvider
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(mCurrentPhotoUri.getPath()));
                cropImage(photoURI);
//                String getPhotoURI = String.valueOf(photoURI);
//
//                Log.d("getPhotoURI : " , getPhotoURI );
//                Uri photoURIJ = Uri.parse(mCurrentPhotoUri.getPath());
//                cropImage(photoURI);
            }
            break;
            case MyConst.REQCD_IMAGE_CROP: {
                Log.d(TAG, "onActivityResult: mCurrentPhotoUri=" + mCurrentPhotoUri);
            }
            break;
            case MyConst.REQCD_IMAGE_GALLERY: {
                mCurrentPhotoUri = null;
                Log.d(TAG, "onActivityResult: getData=" + data.getData());
                if (data.getData() == null) return;
                try {
                    Uri uri = data.getData();
                    if (uri == null) return;
                    cropImage(uri);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Failed to load a image.", Snackbar.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    /**
     * Hide the keyboard.
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Opens the full screen image viewer dialog.
     *
     * @param imgUri
     * @param title
     * @param subtitle
     * @param desc
     */
    public void showImageViewer(String imgUri, String title, String subtitle, String desc, TravelBaseEntity entity) {
        Bundle b = new Bundle();
        b.putString(MyConst.KEY_ID, imgUri);
        b.putString(MyConst.KEY_TITLE, title);
        b.putString(MyConst.KEY_SUBTITLE, subtitle);
        b.putString(MyConst.KEY_DESC, desc);
        Log.d("ShowImage", "Show Image");
        if (entity != null) {
            b.putSerializable(MyConst.REQKEY_TRAVEL, entity);
        }

        ImageViewerDialog dialog = new ImageViewerDialog();
        dialog.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        dialog.show(ft, ImageViewerDialog.TAG);
    }

    protected void postRequestPermissionsResult(final int reqCd, final boolean result) {
        Log.d(TAG, "postRequestPermissionsResult: reqCd=" + reqCd + ", result=" + result);
    }

    /**
     * Prompts the user for permission to use APIs.
     */
    protected void requestPermissions(final int reqCd) {
        switch (reqCd) {
            case MyConst.REQCD_ACCESS_GALLERY:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    postRequestPermissionsResult(reqCd, true);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // If we should give explanation of requested permissions
                        // Show an alert dialog here with request explanation
                        showAlertOkCancel(R.string.permission_dialog_title, R.string.permission_camera_gallery, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        reqCd);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postRequestPermissionsResult(reqCd, false);
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                reqCd);
                    }

                }
                break;
            case MyConst.REQCD_ACCESS_CAMERA:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        + ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    postRequestPermissionsResult(reqCd, true);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // If we should give explanation of requested permissions
                        // Show an alert dialog here with request explanation
                        showAlertOkCancel(R.string.permission_dialog_title, R.string.permission_camera_msg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        reqCd);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postRequestPermissionsResult(reqCd, false);
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                reqCd);
                    }
                }
                break;
            case MyConst.REQCD_ACCESS_FINE_LOCATION:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    postRequestPermissionsResult(reqCd, true);
                } else {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // If we should give explanation of requested permissions
                        // Show an alert dialog here with request explanation
                        showAlertOkCancel(R.string.permission_dialog_title, R.string.permission_camera_msg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        reqCd);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postRequestPermissionsResult(reqCd, false);
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                reqCd);
                    }
                }
                break;
        }

    }

    /**
     * Handles the result of the request for permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    postRequestPermissionsResult(requestCode, false);
                    return;
                }
            }
            postRequestPermissionsResult(requestCode, true);
            return;
        }

        postRequestPermissionsResult(requestCode, false);
    }

    /**
     * Launches Google Maps app
     *
     * @param v
     * @param item
     * @param query
     */
    public void openGoogleMap(View v, TravelBaseEntity item, String query) {
        final String appPackageName = "com.google.android.apps.maps";
        Uri gmmIntentUri;
        if (MyString.isNotEmpty(query)) {
            gmmIntentUri = Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f?q=%s", item.getPlaceLat(), item.getPlaceLng(), Uri.encode(query)));
        } else {
            gmmIntentUri = Uri.parse(String.format(Locale.ENGLISH, "geo:0,0?q=%f,%f(%s)", item.getPlaceLat(), item.getPlaceLng(), Uri.encode(item.getPlaceName())));
        }
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(appPackageName);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            if (v != null) Snackbar.make(v, R.string.no_google_map, Snackbar.LENGTH_SHORT).show();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }
}
