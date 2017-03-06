package com.think.cache.samples.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.think.cache.TCache;
import com.think.cache.samples.R;
import com.think.cache.samples.custom.CustomActivity;
import com.think.cache.samples.parcelable.ParcelableActivity;
import com.think.cache.samples.serializable.SerializableActivity;

/**
 * Created by borney on 3/6/17.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    private TCache defTCache;
    private TCache custom;

    public static MainFragment instance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        defTCache = TCache.get(context);
        custom = TCache.get(context, "custom");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.findViewById(R.id.serializable).setOnClickListener(this);
        view.findViewById(R.id.parcelable).setOnClickListener(this);
        view.findViewById(R.id.custom).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Context context = getContext();
        switch (v.getId()) {
            case R.id.serializable:
                context.startActivity(new Intent(context, SerializableActivity.class));
                break;
            case R.id.parcelable:
                context.startActivity(new Intent(context, ParcelableActivity.class));
                break;
            case R.id.custom:
                context.startActivity(new Intent(context, CustomActivity.class));
                break;
            default:
                break;
        }
    }
}
