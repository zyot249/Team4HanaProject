package shyn.zyot.mytravels.traveldetail;

import android.app.Application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;
import shyn.zyot.mytravels.base.MyConst;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.entity.TravelDiary;
import shyn.zyot.mytravels.entity.TravelExpense;
import shyn.zyot.mytravels.entity.TravelPlan;
import shyn.zyot.mytravels.repository.TravelDetailRepository;

public class TravelDetailViewModel extends AndroidViewModel {
    private final TravelDetailRepository mRepository;

    public TravelDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = TravelDetailRepository.getInstance(application);
    }

    // travel
    private final MutableLiveData<Long> mTravelId = new MutableLiveData<>();
    private final LiveData<Travel> mTravel = Transformations.switchMap(mTravelId, new Function<Long, LiveData<Travel>>() {
        @Override
        public LiveData<Travel> apply(Long id) {
            return mRepository.getTravelById(id);
        }
    });

    public LiveData<Long> getTravelId() {
        return mTravelId;
    }

    public void setTravelId(long id) {
        mTravelId.setValue(id);
    }

    public LiveData<Travel> getTravel() {
        return mTravel;
    }

    // plan
    private final MutableLiveData<Map<String, Object>> mTravelPlanListOption = new MutableLiveData<>();

    private final LiveData<PagedList<TravelPlan>> mTravelPlanList = Transformations.switchMap(mTravelPlanListOption, new Function<Map<String, Object>, LiveData<PagedList<TravelPlan>>>() {
        @Override
        public LiveData<PagedList<TravelPlan>> apply(Map<String, Object> option) {
            return mRepository.getAllPlansOfTravel((long) option.get(MyConst.KEY_ID), null);

        }
    });

    public void setTravelPlanListOption(Map<String, Object> option) {
        if (option == null) {
            option = new HashMap<>();
            option.put(MyConst.KEY_ID, 0);
        }
        mTravelPlanListOption.setValue(option);
    }

    public LiveData<PagedList<TravelPlan>> getTravelPlanList() {
        return mTravelPlanList;
    }

    public void updateTravelPlan(TravelPlan entity) {
        mRepository.updateTravelPlan(entity);
    }
    // diary

    private final MutableLiveData<Map<String, Object>> mTravelDiaryListOption = new MutableLiveData<>();

    private final LiveData<PagedList<TravelDiary>> mTravelDiaryList = Transformations.switchMap(mTravelDiaryListOption, new Function<Map<String, Object>, LiveData<PagedList<TravelDiary>>>() {
        @Override
        public LiveData<PagedList<TravelDiary>> apply(Map<String, Object> option) {
            return mRepository.getAllDiariesOfTravel((long) option.get(MyConst.KEY_ID), null);
        }
    });

    public final LiveData<TravelDiary> recentDiaryImage = Transformations.switchMap(mTravelDiaryListOption, new Function<Map<String, Object>, LiveData<TravelDiary>>() {
        @Override
        public LiveData<TravelDiary> apply(Map<String, Object> option) {
            return mRepository.getRecentDiaryImage((long) option.get(MyConst.KEY_ID));
        }
    });

    public void setTravelDiaryListOption(Map<String, Object> option) {
        if (option == null) {
            option = new HashMap<>();
            option.put(MyConst.KEY_ID, 0);
        }
        mTravelDiaryListOption.setValue(option);
    }

    public LiveData<PagedList<TravelDiary>> getTravelDiaryList() {
        return mTravelDiaryList;
    }

    // expenses
    private final MutableLiveData<Map<String, Object>> mTravelExpenseListOption = new MutableLiveData<>();

    private final LiveData<PagedList<TravelExpense>> mTravelExpenseList = Transformations.switchMap(mTravelExpenseListOption, new Function<Map<String, Object>, LiveData<PagedList<TravelExpense>>>() {
        @Override
        public LiveData<PagedList<TravelExpense>> apply(Map<String, Object> option) {
            return mRepository.getAllExpensesOfTravel((long) option.get(MyConst.KEY_ID), null);
        }
    });

    private LiveData<List<TravelExpense>> mBudgetStatus = Transformations.switchMap(mTravelId, new Function<Long, LiveData<List<TravelExpense>>>() {
        @Override
        public LiveData<List<TravelExpense>> apply(Long id) {
            return mRepository.getBudgetStatus(id);
        }
    });


    public void setTravelExpenseListOption(Map<String, Object> option) {
        if (option == null) {
            option = new HashMap<>();
            option.put(MyConst.KEY_ID, 0);
        }
        mTravelExpenseListOption.setValue(option);
    }

    public LiveData<PagedList<TravelExpense>> getTravelExpenseList() {
        return mTravelExpenseList;
    }

    public LiveData<List<TravelExpense>> getBudgetStatus() {
        return mBudgetStatus;
    }

    public final LiveData<TravelExpense> recentTravelExpense = Transformations.switchMap(mTravelExpenseListOption
            , new Function<Map<String, Object>, LiveData<TravelExpense>>() {
                @Override
                public LiveData<TravelExpense> apply(Map<String, Object> option) {
                    return mRepository.getRecentTravelExpense((long) option.get(MyConst.KEY_ID));
                }
            });
}
