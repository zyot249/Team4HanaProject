package shyn.zyot.mytravels.traveldetail.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import shyn.zyot.mytravels.R;
import shyn.zyot.mytravels.base.BaseActivity;
import shyn.zyot.mytravels.base.MyConst;
import shyn.zyot.mytravels.entity.TravelExpense;
import shyn.zyot.mytravels.main.TravelListItemClickListener;
import shyn.zyot.mytravels.utils.MyString;

public class TravelExpenseListAdapter extends PagedListAdapter<TravelExpense, TravelExpenseListAdapter.TravelViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private TravelListItemClickListener mTravelListItemClickListener;
    private final Context mContext;

    public TravelExpenseListAdapter(Context context) {
        super(TravelExpense.DIFF_CALLBACK);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setListItemClickListener(TravelListItemClickListener travelListItemClickListener) {
        mTravelListItemClickListener = travelListItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        TravelExpense item = getItem(position);
        if (item == null) {
            return;
        }
        holder.dateTxt.setText(item.getDateTimeMinText());
        holder.titleTxt.setText(item.getTitle());
        holder.descTxt.setText(item.getDesc());
        holder.amount.setText(item.getAmountText());
        holder.currency.setText(MyConst.getCurrencyCode(item.getCurrency()).value);
        holder.type.setText(MyConst.getBudgetCode(item.getType()).value);

        if (MyString.isNotEmpty(item.getPlaceName())) {
            holder.placeTxt.setText(item.getPlaceName());
            holder.placeTxt.setVisibility(View.VISIBLE);
        } else {
            holder.placeTxt.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.fragment_expense_list_item, parent, false);
        return new TravelViewHolder(v);
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<TravelExpense> previousList, @Nullable PagedList<TravelExpense> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public TravelExpense getItem(int position) {
        return super.getItem(position);
    }

    class TravelViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTxt;
        private final TextView placeTxt;
        private final TextView titleTxt;
        private final TextView descTxt;
        private final TextView amount;
        private final TextView currency;
        private final TextView type;

        private TravelViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (descTxt.getMaxLines() == 3) {
                        descTxt.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        descTxt.setMaxLines(3);
                    }
                    if (mTravelListItemClickListener != null) {
                        mTravelListItemClickListener.onListItemClick(v
                                , getAdapterPosition()
                                , getItem(getAdapterPosition())
                                , false);
                    }
                }
            });
            if (mTravelListItemClickListener != null) {
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mTravelListItemClickListener.onListItemClick(v
                                , getAdapterPosition()
                                , getItem(getAdapterPosition())
                                , true);
                        return true;
                    }
                });
            }
            dateTxt = v.findViewById(R.id.date_txt);
            placeTxt = v.findViewById(R.id.place_txt);
            titleTxt = v.findViewById(R.id.title_txt);
            descTxt = v.findViewById(R.id.desc_txt);
            amount = v.findViewById(R.id.amount_txt);
            currency = v.findViewById(R.id.currency_txt);
            type = v.findViewById(R.id.type_txt);

            placeTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).openGoogleMap(v, getItem(getAdapterPosition()), null);
                }
            });
        }
    }
}
