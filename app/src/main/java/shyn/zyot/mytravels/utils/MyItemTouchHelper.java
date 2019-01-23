package shyn.zyot.mytravels.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// refer to https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
public class MyItemTouchHelper extends ItemTouchHelper.Callback {

    private final MyItemTouchHelperListner mMyItemTouchHelperListner;

    public MyItemTouchHelper(Context context) {
        if (context instanceof MyItemTouchHelperListner)
            mMyItemTouchHelperListner = (MyItemTouchHelperListner) context;
        else mMyItemTouchHelperListner = null;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        if (mMyItemTouchHelperListner != null)
            return mMyItemTouchHelperListner.onRequestItemViewSwipeEnabled();
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (mMyItemTouchHelperListner != null)
            mMyItemTouchHelperListner.onItemDismiss(viewHolder.getAdapterPosition());
    }

    public interface MyItemTouchHelperListner {
        boolean onRequestItemViewSwipeEnabled();

        void onItemDismiss(int position);
    }
}
