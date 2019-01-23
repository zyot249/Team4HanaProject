package shyn.zyot.mytravels.traveldetail;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import shyn.zyot.mytravels.traveldetail.diary.DiaryFragment;
import shyn.zyot.mytravels.traveldetail.expense.ExpenseFragment;
import shyn.zyot.mytravels.traveldetail.plan.PlanFragment;

/**
 * Returns a fragment corresponding to one of the sections/tabs/pages.
 */


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    // Sparse array to keep track of registered fragments in memory
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private final Context mContext;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return DiaryFragment.newInstance(position);
            case 2:
                return ExpenseFragment.newInstance(position);
            default:
                return PlanFragment.newInstance(position);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return mContext.getString(DiaryFragment.TITLE_ID);
            case 2:
                return mContext.getString(ExpenseFragment.TITLE_ID);
            default:
                return mContext.getString(PlanFragment.TITLE_ID);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    // Register the fragment when the item is instantiated
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    // Unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    // Returns the fragment for the position (if instantiated)
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}
