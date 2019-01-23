package shyn.zyot.mytravels.main;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import shyn.zyot.mytravels.R;
import shyn.zyot.mytravels.entity.Travel;
import shyn.zyot.mytravels.utils.MyImage;
import shyn.zyot.mytravels.utils.MyString;

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.TravelViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<Travel> mList;

    public TravelListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    private TravelListItemClickListener mTravelListItemClickListener;

    public void setListItemClickListener(TravelListItemClickListener travelListItemClickListener) {
        mTravelListItemClickListener = travelListItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    public void setList(List<Travel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        if (getItemCount() == 0) return RecyclerView.NO_ID;
        return mList.get(position).getId();
    }

    private Travel getItem(int position) {
        if (getItemCount() == 0) return null;
        return mList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        Travel item = getItem(position);
        if (item == null) {
            return;
        }
        if (MyString.isEmpty(item.getImgUri())) {
            holder.ivTravelImg.setImageResource(MyImage.getDefaultImgID(position));
        } else {
            holder.ivTravelImg.setImageURI(Uri.parse(item.getImgUri()));
        }
        holder.titleTxt.setText(item.getTitle());
        holder.placeTxt.setText(item.getPlaceName() + " / " + item.getPlaceAddr());
        holder.dateTxt.setText(item.getDateTimeText() + " ~ " + item.getEndDtText());
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.main_travel_item, parent, false);
        return new TravelViewHolder(v);
    }

    class TravelViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTxt;
        private final TextView placeTxt;
        private final TextView dateTxt;
        private final ImageView ivTravelImg;
        private final ImageView ivEdit;

        private TravelViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            titleTxt = v.findViewById(R.id.title_txt);
            placeTxt = v.findViewById(R.id.place_txt);
            dateTxt = v.findViewById(R.id.date_txt);
            ivTravelImg = v.findViewById(R.id.ivTravelImg);
            ivEdit  = v.findViewById(R.id.ivEdit);
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTravelListItemClickListener.onListItemClick(v
                            , getAdapterPosition()
                            , getItem(getAdapterPosition())
                            , true);
                }
            });
        }
    }
}
