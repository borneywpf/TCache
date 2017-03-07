package com.think.cache.samples.json;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by borney on 3/6/17.
 */

public class JsonFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TCache";
    private EditText mKey;
    private TextView mCacheData;
    private TCache mTCache;
    private JSONObject mPutObject;

    public static JsonFragment instance() {
        JsonFragment fragment = new JsonFragment();
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
        View v = inflater.inflate(R.layout.fragment_json, container, false);
        mKey = (EditText) v.findViewById(R.id.key);
        TextView data = (TextView) v.findViewById(R.id.data);
        mPutObject = getJsonObject();
        data.setText(mPutObject.toString());
        v.findViewById(R.id.put).setOnClickListener(this);
        v.findViewById(R.id.get).setOnClickListener(this);
        mCacheData = (TextView) v.findViewById(R.id.cachedata);
        v.findViewById(R.id.expired).setOnClickListener(this);
        v.findViewById(R.id.evict).setOnClickListener(this);
        return v;
    }

    private JSONObject getJsonObject() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("name", "json");
            JSONArray ja = new JSONArray();
            ja.put(0, new Person("Json1", 11));
            ja.put(1, new Person("Json2", 22));
            jo.put("array", ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
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
                String key = mKey.getText().toString();
                mTCache.put(key, mPutObject);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void get() {
        String key = mKey.getText().toString();
        JSONObject cacheData = mTCache.get(key);
        mCacheData.setText(cacheData.toString());
    }

    private class Person {
        private int age;
        private String name;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
