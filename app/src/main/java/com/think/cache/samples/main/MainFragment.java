package com.think.cache.samples.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.think.cache.samples.R;
import com.think.cache.samples.custom.CustomActivity;
import com.think.cache.samples.json.JsonActivity;
import com.think.cache.samples.bitmap.BitmapActivity;
import com.think.cache.samples.string.StringActivity;

/**
 * Created by borney on 3/6/17.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    public static MainFragment instance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.findViewById(R.id.string).setOnClickListener(this);
        view.findViewById(R.id.json).setOnClickListener(this);
        view.findViewById(R.id.other).setOnClickListener(this);
        view.findViewById(R.id.custom).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Context context = getContext();
        switch (v.getId()) {
            case R.id.string:
                context.startActivity(new Intent(context, StringActivity.class));
                break;
            case R.id.json:
                context.startActivity(new Intent(context, JsonActivity.class));
                break;
            case R.id.other:
                context.startActivity(new Intent(context, BitmapActivity.class));
                break;
            case R.id.custom:
                context.startActivity(new Intent(context, CustomActivity.class));
                break;
            default:
                break;
        }
    }
}
