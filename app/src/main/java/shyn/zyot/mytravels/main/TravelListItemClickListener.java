package shyn.zyot.mytravels.main;

import android.view.View;

import shyn.zyot.mytravels.entity.TravelBaseEntity;

/**
 * Interface definition for the callback to call when the list item is clicked.
 */
public interface TravelListItemClickListener {
    /**
     * Called when the list item been clicked.
     *
     * @param view      The view that was clicked.
     * @param position  The position that was clicked.
     * @param entity    The item that was clicked.
     * @param longClick Whether the item was clicked or long-clicked.
     */
    void onListItemClick(View view, int position, TravelBaseEntity entity, boolean longClick);
}

