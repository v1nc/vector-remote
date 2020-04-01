package de.reckendrees.systems.vectorremote.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chaquo.python.PyObject;

import de.reckendrees.systems.vectorremote.R;
import de.reckendrees.systems.vectorremote.interfaces.MainInterface;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter implements MainInterface {
    private PyObject robot;

    public void setRobot(PyObject robot){
        this.robot = robot;
    }
    public boolean isConnected(){
        try{
            if(this.robot != null){
                return true;
            }
        }catch(Exception e){
            return false;
        }
        return false;
    }
    public PyObject getRobot(){
        if(isConnected()){
            return robot;
        }
        return null;
    }

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_0, R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return HomeFragment.newInstance(this);
            case 1:
                return ControllerFragment.newInstance();
            case 2:
                return UtilitiesFragment.newInstance(this);
        }
        return HomeFragment.newInstance(this);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}