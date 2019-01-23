package shyn.zyot.mytravels.traveldetail.expense;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;
import shyn.zyot.mytravels.entity.TravelExpense;
import shyn.zyot.mytravels.repository.TravelDetailRepository;

public class TravelExpenseViewModel extends AndroidViewModel {
    public final MutableLiveData<TravelExpense> currentItem = new MutableLiveData<>();
    private final TravelDetailRepository mRepository;
    public final LiveData<PagedList<TravelExpense>> travelExpenseList = Transformations.switchMap(currentItem, new Function<TravelExpense, LiveData<PagedList<TravelExpense>>>() {
        @Override
        public LiveData<PagedList<TravelExpense>> apply(TravelExpense item) {
            return mRepository.getAllExpensesOfTravel(item.getTravelId(), item.getDateTime());

        }
    });

    public TravelExpenseViewModel(@NonNull Application application) {
        super(application);
        mRepository = TravelDetailRepository.getInstance(application);
    }

    public void insertItem(TravelExpense item) {
        mRepository.insertTravelExpense(item);
    }

    public void updateItem(TravelExpense item) {
        mRepository.updateTravelExpense(item);
    }

    public void deleteItem(TravelExpense item) {
        mRepository.deleteTravelExpense(item);
    }


}
