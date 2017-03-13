package com.think.cache.samples.string;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.think.cache.TCache;
import com.think.cache.samples.R;
import com.think.cache.samples.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by borney on 3/6/17.
 */

public class StringFragment extends Fragment implements View.OnClickListener {
    private EditText mKey;
    private EditText mData;
    private TextView mCacheData;

    private TCache mTCache;

    public static StringFragment instance() {
        StringFragment fragment = new StringFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTCache = TCache.get(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_string, container, false);
        mKey = (EditText) v.findViewById(R.id.key);
        mData = (EditText) v.findViewById(R.id.data);
        v.findViewById(R.id.put).setOnClickListener(this);
        v.findViewById(R.id.get).setOnClickListener(this);
        mCacheData = (TextView) v.findViewById(R.id.cachedata);
        v.findViewById(R.id.iscached).setOnClickListener(this);
        v.findViewById(R.id.expired).setOnClickListener(this);
        v.findViewById(R.id.evict).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.put:
                put();
                break;
            case R.id.get:
                get();
                break;
            case R.id.iscached:
                isCached();
                break;
            case R.id.expired:
                expired();
                break;
            case R.id.evict:
                evict();
                break;
            default:
                break;
        }

    }

    private void isCached() {
        String key = mKey.getText().toString();
        ToastUtils.toast(getContext(), "是否缓存:" + key + ":" + mTCache.isCached(key));
    }

    private void evict() {
        String key = mKey.getText().toString();
        mTCache.evict(key);
        ToastUtils.toast(getContext(), "清除缓存:" + key);
    }

    private void expired() {
        String key = mKey.getText().toString();
        ToastUtils.toast(getContext(), "Expired:" + mTCache.isExpired(key, 10));
    }

    private void put() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                String key = mKey.getText().toString();
                String data = mData.getText().toString();
                mTCache.putSerializable(key, data);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void get() {
        String key = mKey.getText().toString();
        String cacheData = mTCache.getSerializable(key);
        mCacheData.setText(cacheData);
    }
}
