package shyn.zyot.mytravels.traveldetail.diary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;
import shyn.zyot.mytravels.entity.TravelDiary;
import shyn.zyot.mytravels.repository.TravelDetailRepository;

public class TravelDiaryViewModel extends AndroidViewModel {
    public final MutableLiveData<TravelDiary> currentItem = new MutableLiveData<>();
    private final TravelDetailRepository mRepository;
    public final LiveData<PagedList<TravelDiary>> travelDiaryList = Transformations.switchMap(currentItem, new Function<TravelDiary, LiveData<PagedList<TravelDiary>>>() {
        @Override
        public LiveData<PagedList<TravelDiary>> apply(TravelDiary item) {
            return mRepository.getAllDiariesOfTravel(item.getTravelId(), item.getDateTime());

        }
    });

    public TravelDiaryViewModel(@NonNull Application application) {
        super(application);
        mRepository = TravelDetailRepository.getInstance(application);
    }

    public void insertItem(TravelDiary item) {
        mRepository.insertTravelDiary(item);
    }

    public void updateItem(TravelDiary item) {
        mRepository.updateTravelDiary(item);
    }

    public void deleteItem(TravelDiary item) {
        mRepository.deleteTravelDiary(item);
    }


}
