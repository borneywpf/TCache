package com.think.cache.samples.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.think.cache.ByteMapper;
import com.think.cache.TCache;
import com.think.cache.samples.R;
import com.think.cache.samples.ToastUtils;
import com.think.cache.samples.main.MainActivity;

import java.net.URISyntaxException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by borney on 3/6/17.
 */

public class CustomFragment extends Fragment implements View.OnClickListener {
    private EditText mKey;
    private TextView mCacheData;
    private TCache mTCache;

    public static CustomFragment instance() {
        CustomFragment fragment = new CustomFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);
        view.findViewById(R.id.put).setOnClickListener(this);
        view.findViewById(R.id.get).setOnClickListener(this);
        view.findViewById(R.id.expired).setOnClickListener(this);
        view.findViewById(R.id.evict).setOnClickListener(this);
        mKey = (EditText) view.findViewById(R.id.key);
        mCacheData = (TextView) view.findViewById(R.id.cachedata);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTCache = TCache.get(context, "custom");
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
                Intent intent = new Intent(getContext(), MainActivity.class);
                String key = mKey.getText().toString();
                mTCache.putByteMapper(key, intent, new IntentByteMapper());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void get() {
        String key = mKey.getText().toString();
        Intent intent = mTCache.getByteMapper(key, new IntentByteMapper());
        mCacheData.setText(intent.toUri(0));
    }

    class IntentByteMapper implements ByteMapper<Intent> {

        @Override
        public byte[] getBytes(Intent obj) {
            String uri = obj.toUri(0);
            return uri.getBytes();
        }

        @Override
        public Intent getObject(byte[] bytes) {
            String uri = new String(bytes);
            try {
                return Intent.parseUri(uri, 0);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
