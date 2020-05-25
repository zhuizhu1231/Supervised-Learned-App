package com.example.demo.ui.note.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.demo.R;
import com.example.demo.ui.note.ui.main.notes_fragment.NoteFragment;
import com.example.demo.ui.note.ui.main.notes_label_fragment.NoteLabelFragment;

import java.util.ArrayList;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends myFragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.fragment_tab_note, R.string.fragment_tab_note_label};
    private final Context mContext;


    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mFragments.add(new NoteFragment());
        mFragments.add(new NoteLabelFragment());
    }

    @Override
    public Fragment getItem(int position) {

        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return mFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return mFragments.size();
    }

    @Override
    public Fragment getmCurrentPrimaryItem() {
        return super.getmCurrentPrimaryItem();
    }
    public boolean ifFragmentEquals(Fragment fragment1,Fragment fragment2){
        return fragment1==fragment2;
    }

    public ArrayList<Fragment> getmFragments() {
        return mFragments;
    }
}