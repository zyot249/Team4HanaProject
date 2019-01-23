package shyn.zyot.mytravels;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import shyn.zyot.mytravels.base.BaseActivity;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.entity.TravelBaseEntity;
import shyn.zyot.mytravels.main.ListItemClickListener;
import shyn.zyot.mytravels.main.TravelViewModel;
import shyn.zyot.mytravels.search.SearchTravelsListAdapter;

public class SearchableActivity extends BaseActivity implements ListItemClickListener,
        View.OnClickListener{
    private final static String TAG = SearchableActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private EditText etTypeCity;
    private ImageView ivCancelSearch;
    private String typingCity = "";

    private SearchTravelsListAdapter searchAdapter;
    private TravelViewModel travelsViewModel;
    private final Observer<List<Travel>> travelsObserver = new Observer<List<Travel>>() {
        @Override
        public void onChanged(List<Travel> travels) {
            Log.d(TAG, "onChanged: travels.size=" + travels.size());
            searchAdapter.setTravelList(travels);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = findViewById(R.id.tbToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_blue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        etTypeCity = findViewById(R.id.etTypeCity);
        ivCancelSearch = findViewById(R.id.ivCancelSearch);
        ivCancelSearch.setOnClickListener(this);
        // on typing city --> get typed words
        etTypeCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivCancelSearch.setVisibility(View.VISIBLE);
                typingCity = s.toString();
                Log.d(TAG, "onTextChanged: typing text="+typingCity);
                doMySearch(typingCity);
            }

            @Override
            public void afterTextChanged(Editable s) {
                typingCity = s.toString();
                Log.d(TAG, "afterTextChanged: typedTxt="+typingCity);
                doMySearch(typingCity);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        searchAdapter = new SearchTravelsListAdapter(this);
        travelsViewModel = ViewModelProviders.of(this).get(TravelViewModel.class);
    }
    private void doMySearch(String placeName) {
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        travelsViewModel.getAllTravelsByTypingCity(placeName).observe(this, travelsObserver);
    }

    @Override
    public void onListItemClick(View v, int position, TravelBaseEntity entity) {
        // todo go to travel detail
        finish();
    }

    @Override
    public void onMoreVertMenuItemClick(int viewId, int position, TravelBaseEntity entity) {
        return;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCancelSearch:
                // clear inputted text
                typingCity = "";
                etTypeCity.setText("");
                break;
        }
    }
}
