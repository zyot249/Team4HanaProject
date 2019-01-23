package shyn.zyot.mytravels.main;

import android.view.View;

import shyn.zyot.mytravels.entity.TravelBaseEntity;

public interface ListItemClickListener {
    void onListItemClick(View v, int position, TravelBaseEntity entity);

    void onMoreVertMenuItemClick(int viewId, int position, TravelBaseEntity entity);
}