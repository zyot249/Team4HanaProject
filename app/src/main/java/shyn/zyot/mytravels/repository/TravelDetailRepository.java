package shyn.zyot.mytravels.repository;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import shyn.zyot.mytravels.dao.TravelDao;
import shyn.zyot.mytravels.dao.TravelDiaryDao;
import shyn.zyot.mytravels.dao.TravelExpenseDao;
import shyn.zyot.mytravels.dao.TravelPlanDao;
import shyn.zyot.mytravels.database.AppDatabase;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.entity.TravelDiary;
import shyn.zyot.mytravels.entity.TravelExpense;
import shyn.zyot.mytravels.entity.TravelPlan;
import shyn.zyot.mytravels.utils.MyString;

public class TravelDetailRepository {
    private static final String TAG = TravelDetailRepository.class.getSimpleName();

    private static volatile TravelDetailRepository INSTANCE;
    private final TravelDao mTravelDao;
    private final TravelPlanDao mTravelPlanDao;
    private final TravelDiaryDao mTravelDiaryDao;
    private final TravelExpenseDao mTravelExpenseDao;

    private TravelDetailRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTravelDao = db.travelDao();
        mTravelPlanDao = db.travelPlanDao();
        mTravelDiaryDao = db.travelDiaryDao();
        mTravelExpenseDao = db.travelExpenseDao();
    }

    public static TravelDetailRepository getInstance(final Application application) {
        if (INSTANCE == null) {
            synchronized (TravelDetailRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TravelDetailRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    // travel
    public LiveData<Travel> getTravelById(long id) {
        return mTravelDao.getTravelById(id);
    }

    // plan
    public LiveData<PagedList<TravelPlan>> getAllPlansOfTravel(long travelId, String dateTime) {
        if (MyString.isEmpty(dateTime))
            return new LivePagedListBuilder<>(mTravelPlanDao.getAllPlansOfTravel(travelId), 20).build();
        return new LivePagedListBuilder<>(mTravelPlanDao.getPlansOnDate(travelId, dateTime), 20).build();
    }

    public void insertTravelPlan(TravelPlan item) {
        new insertTravelPlanTask(mTravelPlanDao).execute(item);
    }

    public void updateTravelPlan(TravelPlan item) {
        new updateTravelPlanTask(mTravelPlanDao).execute(item);
    }

    public void deleteTravelPlan(TravelPlan item) {
        new deleteTravelPlanTask(mTravelPlanDao).execute(item);
    }

    // expense
    public LiveData<PagedList<TravelExpense>> getAllExpensesOfTravel(long travelId, String dateTime) {
        if (MyString.isEmpty(dateTime))
            return new LivePagedListBuilder<>(mTravelExpenseDao.getAllExpensesOfTravel(travelId), 20).build();
        return new LivePagedListBuilder<>(mTravelExpenseDao.getExpensesOnDate(travelId, dateTime), 20).build();
    }

    public void insertTravelExpense(TravelExpense item) {
        new insertTravelExpenseTask(mTravelExpenseDao).execute(item);
    }

    public void updateTravelExpense(TravelExpense item) {
        new updateTravelExpenseTask(mTravelExpenseDao).execute(item);
    }

    public void deleteTravelExpense(TravelExpense item) {
        new deleteTravelExpenseTask(mTravelExpenseDao).execute(item);
    }

    public LiveData<List<TravelExpense>> getBudgetStatus(long travelId) {
        return mTravelExpenseDao.getBudgetStatus(travelId);
    }

    public LiveData<TravelExpense> getRecentTravelExpense(long travelId) {
        return mTravelExpenseDao.getRecentItem(travelId);
    }

    // diary
    public LiveData<PagedList<TravelDiary>> getAllDiariesOfTravel(long travelId, String dateTime) {
        if (MyString.isEmpty(dateTime))
            return new LivePagedListBuilder<>(mTravelDiaryDao.getAllDiariesOfTravel(travelId), 20).build();
        return new LivePagedListBuilder<>(mTravelDiaryDao.getDiariesOnDate(travelId, dateTime), 20).build();
    }

    public LiveData<TravelDiary> getRecentDiaryImage(long travelId) {
        return mTravelDiaryDao.getRecentDiaryImage(travelId);
    }

    public void insertTravelDiary(TravelDiary item) {
        new insertTravelDiaryTask(mTravelDiaryDao).execute(item);
    }

    public void updateTravelDiary(TravelDiary item) {
        new updateTravelDiaryTask(mTravelDiaryDao).execute(item);
    }

    public void deleteTravelDiary(TravelDiary item) {
        new deleteTravelDiaryTask(mTravelDiaryDao).execute(item);
    }

    private static class insertTravelPlanTask extends AsyncTask<TravelPlan, Void, Void> {
        private TravelPlanDao mAsyncTaskDao;

        insertTravelPlanTask(TravelPlanDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelPlan... params) {
            mAsyncTaskDao.insert(params);
            return null;
        }
    }

    private static class updateTravelPlanTask extends AsyncTask<TravelPlan, Void, Void> {
        private TravelPlanDao mAsyncTaskDao;

        updateTravelPlanTask(TravelPlanDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelPlan... params) {
            TravelPlan item = params[0];
            if (item.getId() == -99) {
                mAsyncTaskDao.undeleteAllMarkedYes(item.getTravelId());
            } else {
                mAsyncTaskDao.update(params);
            }
            return null;
        }
    }

    private static class deleteTravelPlanTask extends AsyncTask<TravelPlan, Void, Void> {
        private TravelPlanDao mAsyncTaskDao;

        deleteTravelPlanTask(TravelPlanDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelPlan... params) {
            TravelPlan item = params[0];
            if (item.getId() == -99) {
                mAsyncTaskDao.deleteAllMarkedYes(item.getTravelId());
            } else {
                mAsyncTaskDao.delete(params);
            }
            return null;
        }
    }

    private static class insertTravelExpenseTask extends AsyncTask<TravelExpense, Void, Void> {
        private TravelExpenseDao mAsyncTaskDao;

        insertTravelExpenseTask(TravelExpenseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelExpense... params) {
            mAsyncTaskDao.insert(params);
            return null;
        }
    }

    private static class updateTravelExpenseTask extends AsyncTask<TravelExpense, Void, Void> {
        private TravelExpenseDao mAsyncTaskDao;

        updateTravelExpenseTask(TravelExpenseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelExpense... params) {
            TravelExpense item = params[0];
            if (item.getId() == -99) {
                mAsyncTaskDao.undeleteAllMarkedYes(item.getTravelId());
            } else {
                mAsyncTaskDao.update(params);
            }
            return null;
        }
    }

    private static class deleteTravelExpenseTask extends AsyncTask<TravelExpense, Void, Void> {
        private TravelExpenseDao mAsyncTaskDao;

        deleteTravelExpenseTask(TravelExpenseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelExpense... params) {
            TravelExpense item = params[0];
            if (item.getId() == -99) {
                mAsyncTaskDao.deleteAllMarkedYes(item.getTravelId());
            } else {
                mAsyncTaskDao.delete(params);
            }
            return null;
        }
    }

    private static class insertTravelDiaryTask extends AsyncTask<TravelDiary, Void, Void> {
        private TravelDiaryDao mAsyncTaskDao;

        insertTravelDiaryTask(TravelDiaryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelDiary... params) {
            mAsyncTaskDao.insert(params);
            return null;
        }
    }

    private static class updateTravelDiaryTask extends AsyncTask<TravelDiary, Void, Void> {
        private TravelDiaryDao mAsyncTaskDao;

        updateTravelDiaryTask(TravelDiaryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelDiary... params) {
            TravelDiary item = params[0];
            if (item.getId() == -99) {
                mAsyncTaskDao.undeleteAllMarkedYes(item.getTravelId());
            } else {
                mAsyncTaskDao.update(params);
            }
            return null;
        }
    }

    private static class deleteTravelDiaryTask extends AsyncTask<TravelDiary, Void, Void> {
        private TravelDiaryDao mAsyncTaskDao;

        deleteTravelDiaryTask(TravelDiaryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TravelDiary... params) {
            TravelDiary item = params[0];
            if (item.getId() == -99) {
                List<TravelDiary> list = mAsyncTaskDao.selectAllMarkedYes(item.getTravelId());
                for (TravelDiary diary : list) {
                    if (MyString.isNotEmpty(diary.getImgUri())) {
                        try {
                            File file = new File((Uri.parse(diary.getImgUri()).getPath()));
                            file.delete();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                    if (MyString.isNotEmpty(diary.getThumbUri())) {
                        try {
                            File file = new File((Uri.parse(diary.getThumbUri()).getPath()));
                            file.delete();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }
                mAsyncTaskDao.deleteAllMarkedYes(item.getTravelId());
            } else {
                for (TravelDiary diary : params) {
                    if (MyString.isNotEmpty(diary.getImgUri())) {
                        try {
                            File file = new File((Uri.parse(diary.getImgUri()).getPath()));
                            file.delete();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                    if (MyString.isNotEmpty(diary.getThumbUri())) {
                        try {
                            File file = new File((Uri.parse(diary.getThumbUri()).getPath()));
                            file.delete();
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }
                mAsyncTaskDao.delete(params);
            }
            return null;
        }
    }
}
