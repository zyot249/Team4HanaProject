package shyn.zyot.mytravels.traveldetail.plan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;
import shyn.zyot.mytravels.entity.TravelPlan;
import shyn.zyot.mytravels.repository.TravelDetailRepository;

public class TravelPlanViewModel extends AndroidViewModel {
    public final MutableLiveData<TravelPlan> currentItem = new MutableLiveData<>();
    private final TravelDetailRepository mRepository;
    public final LiveData<PagedList<TravelPlan>> travelPlanList = Transformations.switchMap(currentItem, new Function<TravelPlan, LiveData<PagedList<TravelPlan>>>() {
        @Override
        public LiveData<PagedList<TravelPlan>> apply(TravelPlan item) {
            return mRepository.getAllPlansOfTravel(item.getTravelId(), item.getDateTime());

        }
    });

    public TravelPlanViewModel(@NonNull Application application) {
        super(application);
        mRepository = TravelDetailRepository.getInstance(application);
    }

    public void insertItem(TravelPlan item) {
        mRepository.insertTravelPlan(item);
    }

    public void updateItem(TravelPlan item) {
        mRepository.updateTravelPlan(item);
    }

    public void deleteItem(TravelPlan item) {
        mRepository.deleteTravelPlan(item);
    }


}
