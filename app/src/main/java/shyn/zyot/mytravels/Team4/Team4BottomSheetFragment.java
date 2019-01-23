package shyn.zyot.mytravels.Team4;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import shyn.zyot.mytravels.R;

public class Team4BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    BottomsheetItemHandler itemHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.team4_fragment_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.LLAddDailyPlan).setOnClickListener(this);
        view.findViewById(R.id.LLAddExpenses).setOnClickListener(this);
        view.findViewById(R.id.LLAddDiary).setOnClickListener(this);
    }

    public void setItemHandler(BottomsheetItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public void onClick(View v) {
        itemHandler.onItemClick(v);
    }
}

