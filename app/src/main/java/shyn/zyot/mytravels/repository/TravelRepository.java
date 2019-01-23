package shyn.zyot.mytravels.repository;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import shyn.zyot.mytravels.base.TravelSort;
import shyn.zyot.mytravels.dao.HotPlaceDao;
import shyn.zyot.mytravels.dao.TravelDao;
import shyn.zyot.mytravels.database.AppDatabase;
import shyn.zyot.mytravels.entity.HotPlace;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.utils.MyDate;

public class TravelRepository {
    private static final String TAG = TravelRepository.class.getSimpleName();

    private static volatile TravelRepository INSTANCE;
    private final Application mApplication;
    private final TravelDao mTravelDao;
    private final HotPlaceDao mHotPlaceDao;

    private TravelRepository(Application application) {
        mApplication = application;
        AppDatabase db = AppDatabase.getDatabase(application);
        mTravelDao = db.travelDao();
        mHotPlaceDao = db.hotPlaceDao();
    }

    public static TravelRepository getInstance(final Application application) {
        if (INSTANCE == null) {
            synchronized (TravelRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TravelRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Travel>> getAllTravelsByTypingCity(String placeName) {
        return mTravelDao.getAllTravelsByTypingCity(placeName);
    }

    public LiveData<List<Travel>> getAllTravels() {
        return mTravelDao.getAllTravels();
    }

    public LiveData<List<Travel>> getAllTravels(TravelSort travelSort) {
        switch (travelSort) {
            case DEFAULT:
                return mTravelDao.getAllTravels();
            case TITLE_ASC:
                return mTravelDao.getAllTravelsByTitleAsc();
            case TITLE_DESC:
                return mTravelDao.getAllTravelsByTitleDesc();
            case START_ASC:
                return mTravelDao.getAllTravelsByStartAsc();
            case START_DESC:
                return mTravelDao.getAllTravelsByStartDesc();
        }
        return mTravelDao.getAllTravels();
    }

    public void insert(Travel travel) {
        new insertAsyncTask(mTravelDao).execute(travel);
    }

    public void update(Travel travel) {
        new updateAsyncTask(mTravelDao).execute(travel);
    }

    public void delete(Travel... travels) {
        new deleteAsyncTask(mTravelDao).execute(travels);
    }

    private static class insertAsyncTask extends AsyncTask<Travel, Void, Void> {
        private TravelDao mAsyncTaskDao;

        insertAsyncTask(TravelDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Travel... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Travel, Void, Void> {
        private TravelDao mAsyncTaskDao;

        updateAsyncTask(TravelDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Travel... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Travel, Void, Void> {
        private TravelDao mAsyncTaskDao;

        deleteAsyncTask(TravelDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Travel... params) {
            for (Travel travel : params) {
                TravelRepository.INSTANCE.deleteTravelDirectory(travel.getId());
            }
            mAsyncTaskDao.delete(params);
            return null;
        }
    }

    /**
     * Delete the image directory of a selected travel
     *
     * @param travelId
     */
    private void deleteTravelDirectory(long travelId) {
        try {
            final File rootDir = new File(mApplication.getFilesDir(), "t" + travelId);
            if (!rootDir.exists()) return;
            // Get all files of the directory to be deleted
            File[] files = rootDir.listFiles();
            if (files != null) {
                // delete all files
                for (File file : files) file.delete();
            }
            // delete a directory
            rootDir.delete();
        } catch (Exception e) {
            Log.e("TravelRepository", e.getMessage(), e);
        }
    }

    // hot places
    public LiveData<PagedList<HotPlace>> getAllHotPlaces() {
        return new LivePagedListBuilder<>(mHotPlaceDao.getAllHotPlaces(), 20).build();
    }

    public LiveData<HotPlace> geHotPlaceToDownload(String dateTime) {
        return mHotPlaceDao.geHotPlaceToDownload(dateTime);
    }

    public void saveHotPlaceImage(HotPlace item, Bitmap bitmap) {
        new saveHotPlaceImageAsyncTask(mHotPlaceDao, bitmap).execute(item);
    }


    private static class saveHotPlaceImageAsyncTask extends AsyncTask<HotPlace, Void, Void> {
        private final HotPlaceDao mAsyncTaskDao;
        private final Bitmap bitmap;

        saveHotPlaceImageAsyncTask(HotPlaceDao dao, Bitmap bitmap) {
            mAsyncTaskDao = dao;
            this.bitmap = bitmap;
        }

        @Override
        protected Void doInBackground(final HotPlace... params) {
            final HotPlace item = params[0];
            if (bitmap == null) {
                item.setImgUri(null);
                item.setDateTime(MyDate.getCurrentDate());
                mAsyncTaskDao.update(item);
                return null;
            }
            final File rootDir = new File(TravelRepository.INSTANCE.mApplication.getFilesDir(), "hotplaces");
            if (!rootDir.exists()) rootDir.mkdirs();
            final File imgFile = new File(rootDir, item.getId() + ".jpg");
            // convert bitmap to file
            FileOutputStream imgFos = null;
            try {
                imgFos = new FileOutputStream(imgFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imgFos);
                imgFos.flush();
                bitmap.recycle();
                item.setImgUri(Uri.fromFile(imgFile).toString());
                item.setDateTime(MyDate.getCurrentDate());
                mAsyncTaskDao.update(item);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            } finally {
                try {
                    if (imgFos != null) imgFos.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            return null;
        }
    }
}
