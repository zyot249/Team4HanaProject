package shyn.zyot.mytravels.traveldetail;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import shyn.zyot.mytravels.entity.Travel;

public abstract class TravelDetailBaseFragment extends Fragment {
    private final static String TAG = TravelDetailBaseFragment.class.getSimpleName();
    protected static final String ARG_SECTION_NUMBER = "section_number";
    private final Observer<Travel> mTravelObserver = new Observer<Travel>() {
        @Override
        public void onChanged(Travel travel) {
            onTravelChanged(travel);
        }
    };
    protected TravelDetailViewModel mViewModel;

    protected abstract void onTravelChanged(Travel travel);

    public abstract void requestAddItem();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(TravelDetailViewModel.class);
        mViewModel.getTravel().observe(this, mTravelObserver);
        Log.d(TAG, "onCreate: mViewModel=" + mViewModel);
    }


}
