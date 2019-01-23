package shyn.zyot.mytravels.traveldetail.expense;

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
import shyn.zyot.mytravels.entity.TravelExpense;
import shyn.zyot.mytravels.main.TravelListItemClickListener;
import shyn.zyot.mytravels.traveldetail.ExpenseDetailActivity;
import shyn.zyot.mytravels.traveldetail.TravelDetailBaseFragment;
import shyn.zyot.mytravels.utils.MyDate;

public class ExpenseFragment extends TravelDetailBaseFragment implements TravelListItemClickListener {
    private static final String TAG = ExpenseFragment.class.getSimpleName();
    public static final int TITLE_ID = R.string.title_frag_expenses;
    private TravelExpenseListAdapter mListAdapter;

    public ExpenseFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ExpenseFragment newInstance(int sectionNumber) {
        Log.d(TAG, "newInstance: sectionNumber=" + sectionNumber);
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TravelExpenseListAdapter(getContext());
        mListAdapter.setListItemClickListener(this);
        mViewModel.getTravelExpenseList().observe(this, new Observer<PagedList<TravelExpense>>() {
            @Override
            public void onChanged(PagedList<TravelExpense> items) {
                mListAdapter.submitList(items);
            }
        });
        mViewModel.recentTravelExpense.observe(this, new Observer<TravelExpense>() {
            @Override
            public void onChanged(TravelExpense travelExpense) {
                // do nothing. just observe it.
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expense, container, false);
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
        mViewModel.setTravelExpenseListOption(option);
    }

    @Override
    public void requestAddItem() {
        Travel travel = mViewModel.getTravel().getValue();
        Log.d(TAG, "requestAddItem: travel=" + travel);
        if (travel == null) return;

        TravelExpense item = new TravelExpense();
        item.setTravelId(travel.getId());
        item.setDateTime(MyDate.getCurrentTime());
        if (mViewModel.recentTravelExpense.getValue() != null) {
            item.setType(mViewModel.recentTravelExpense.getValue().getType());
            item.setCurrency(mViewModel.recentTravelExpense.getValue().getCurrency());
        }
        Intent intent = new Intent(getContext(), ExpenseDetailActivity.class);
        intent.putExtra(MyConst.REQKEY_TRAVEL, item);
        startActivity(intent);
    }

    @Override
    public void onListItemClick(View view, int position, TravelBaseEntity entity, boolean longClick) {
        TravelExpense item = (TravelExpense) entity;
        Log.d(TAG, "onListItemClick: item=" + item);
        Log.d(TAG, "onListItemClick: longClick=" + longClick);
        if (!longClick) return;
        Intent intent = new Intent(getContext(), ExpenseDetailActivity.class);
        intent.putExtra(MyConst.REQKEY_TRAVEL, item);
        startActivity(intent);
    }
}
