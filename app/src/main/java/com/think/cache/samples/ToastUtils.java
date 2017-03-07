package com.think.cache.samples;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by borney on 3/6/17.
 */

public class ToastUtils {
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
