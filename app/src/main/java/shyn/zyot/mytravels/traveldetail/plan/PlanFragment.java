package shyn.zyot.mytravels.traveldetail.plan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import shyn.zyot.mytravels.R;
import shyn.zyot.mytravels.base.MyConst;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.entity.TravelBaseEntity;
import shyn.zyot.mytravels.entity.TravelPlan;
import shyn.zyot.mytravels.main.TravelListItemClickListener;
import shyn.zyot.mytravels.traveldetail.PlanDetailActivity;
import shyn.zyot.mytravels.traveldetail.TravelDetailBaseFragment;
import shyn.zyot.mytravels.utils.MyDate;

public class PlanFragment extends TravelDetailBaseFragment implements TravelListItemClickListener {
    private static final String TAG = PlanFragment.class.getSimpleName();
    public static final int TITLE_ID = R.string.title_frag_daily_plans;
    private TravelPlanListAdapter mListAdapter;

    public PlanFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlanFragment newInstance(int sectionNumber) {
        Log.d(TAG, "newInstance: sectionNumber=" + sectionNumber);
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TravelPlanListAdapter(getContext());
        mListAdapter.setListItemClickListener(this);
//        mListAdapter.setOnRatingChangeListener(new TravelPlanListAdapter.OnRatingChangeListener() {
//            @Override
//            public void onRatingChanged(int position, TravelPlan entity) {
//                mViewModel.updateTravelPlan(entity);
//            }
//        });
        mViewModel.getTravelPlanList().observe(this, new Observer<PagedList<TravelPlan>>() {
            @Override
            public void onChanged(PagedList<TravelPlan> items) {
                mListAdapter.submitList(items);
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mListAdapter);
        return rootView;
    }

    @Override
    protected void onTravelChanged(Travel travel) {
        Log.d(TAG, "onTravelChanged: travel=" + travel);
        if (travel == null) return;
        Map<String, Object> option = new HashMap<>();
        option.put(MyConst.KEY_ID, travel.getId());
        mViewModel.setTravelPlanListOption(option);
    }

    @Override
    public void requestAddItem() {
        Travel travel = mViewModel.getTravel().getValue();
        Log.d(TAG, "requestAddItem: travel=" + travel);
        if (travel == null) return;

        TravelPlan item = new TravelPlan();
        item.setTravelId(travel.getId());
        item.setDateTime(MyDate.getCurrentTime());
        Intent intent = new Intent(getContext(), PlanDetailActivity.class);
        intent.putExtra(MyConst.REQKEY_TRAVEL, item);
        startActivity(intent);
    }

    @Override
    public void onListItemClick(View view, int position, TravelBaseEntity entity, boolean longClick) {
        TravelPlan item = (TravelPlan) entity;
        Log.d(TAG, "onListItemClick: item=" + item);
        Log.d(TAG, "onListItemClick: longClick=" + longClick);
        if (!longClick) return;
        Intent intent = new Intent(getContext(), PlanDetailActivity.class);
        intent.putExtra(MyConst.REQKEY_TRAVEL, item);
        startActivity(intent);
    }
}
