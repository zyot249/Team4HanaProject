package shyn.zyot.mytravels.main;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;
import shyn.zyot.mytravels.base.MyApplication;
import shyn.zyot.mytravels.base.TravelSort;
import shyn.zyot.mytravels.entity.HotPlace;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.repository.TravelRepository;

public class TravelViewModel extends AndroidViewModel {
    private final TravelRepository mRepository;
    private final MutableLiveData<TravelSort> mTravelSort = new MutableLiveData<>();
    private final LiveData<List<Travel>> mAllTravels = Transformations.switchMap(mTravelSort, new Function<TravelSort, LiveData<List<Travel>>>() {
        @Override
        public LiveData<List<Travel>> apply(TravelSort option) {
            return mRepository.getAllTravels(option);
        }
    });

    public TravelViewModel(@NonNull Application application) {
        super(application);
        mRepository = TravelRepository.getInstance(application);
    }

    public void setTravelSort(TravelSort option) {
        mTravelSort.setValue(option);
        ((MyApplication) getApplication()).setTravelSort(option);
    }

    public LiveData<List<Travel>> getAllTravels() {
        return mAllTravels;
    }

    public void insert(Travel travel) {
        mRepository.insert(travel);
    }

    public void update(Travel travel) {
        mRepository.update(travel);
    }

    public void delete(Travel... travels) {
        mRepository.delete(travels);
    }

    public final LiveData<PagedList<HotPlace>> getAllHotPlaces() {
        return mRepository.getAllHotPlaces();
    }

    public LiveData<List<Travel>> getAllTravelsByTypingCity(String placeName) {
        return mRepository.getAllTravelsByTypingCity(placeName);
    }

    public final LiveData<HotPlace> geHotPlaceToDownload(String dateTime) {
        return mRepository.geHotPlaceToDownload(dateTime);
    }

    public void saveHotPlaceImage(HotPlace item, Bitmap bitmap) {
        mRepository.saveHotPlaceImage(item, bitmap);
    }

}
