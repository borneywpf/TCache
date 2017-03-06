package com.think.cache.samples;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Created by borney on 3/6/17.
 */

public class ToastUtils {
    private static Toast sToast;
    public static void toast(Context context, String msg) {
        if (sToast == null || sToast.getView().getVisibility() != View.VISIBLE) {
            sToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            sToast.show();
        } else {
            sToast.setText(msg);
        }
    }
}
