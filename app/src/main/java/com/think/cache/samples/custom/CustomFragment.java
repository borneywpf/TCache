package com.think.cache.samples.custom;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by borney on 3/6/17.
 */

public class CustomFragment extends Fragment {

    public static CustomFragment instance() {
        CustomFragment fragment = new CustomFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
