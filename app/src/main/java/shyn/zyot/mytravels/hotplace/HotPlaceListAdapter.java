package shyn.zyot.mytravels.hotplace;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
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
import shyn.zyot.mytravels.entity.HotPlace;
import shyn.zyot.mytravels.main.TravelListItemClickListener;
import shyn.zyot.mytravels.utils.CircleImageView;
import shyn.zyot.mytravels.utils.MyString;

public class HotPlaceListAdapter extends PagedListAdapter<HotPlace, HotPlaceListAdapter.TravelViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private int mSelectedPosition = 0;

    private TravelListItemClickListener mTravelListItemClickListener;

    public void setListItemClickListener(TravelListItemClickListener travelListItemClickListener) {
        mTravelListItemClickListener = travelListItemClickListener;
    }

    public HotPlaceListAdapter(Context context) {
        super(HotPlace.DIFF_CALLBACK);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setSelectedPosition(int position) {
        notifyItemChanged(mSelectedPosition);
        mSelectedPosition = position;
        notifyItemChanged(mSelectedPosition);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        HotPlace item = getItem(position);
        if (item == null) {
            return;
        }
        holder.titleTxt.setText(item.getPlaceName());
        if (MyString.isEmpty(item.getImgUri())) {
            holder.thumbnail.setImageResource(R.drawable.hanoi);
        } else {
            holder.thumbnail.setImageURI(Uri.parse(item.getImgUri()));
        }
        if (MyString.isNotEmpty(item.getDesc())) {
            holder.descTxt.setText(Html.fromHtml("Photo by " + item.getDesc()));
        }
        if (position == mSelectedPosition) {
            holder.thumbnail.setPadding(0, 0, 0, 0);
        } else {
            holder.thumbnail.setPadding(32, 32, 32, 32);
        }
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.hotplace_list_item, parent, false);
        return new TravelViewHolder(v);
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<HotPlace> previousList, @Nullable PagedList<HotPlace> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        notifyDataSetChanged();
    }

    class TravelViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView thumbnail;
        private final TextView titleTxt;
        private final TextView descTxt;

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
            thumbnail = v.findViewById(R.id.thumbnail);
            descTxt = v.findViewById(R.id.desc_txt);
            titleTxt = v.findViewById(R.id.title_txt);
        }
    }

    @Nullable
    @Override
    public HotPlace getItem(int position) {
        return super.getItem(position);
    }
}
