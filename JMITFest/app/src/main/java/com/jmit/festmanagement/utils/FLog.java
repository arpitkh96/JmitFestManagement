package com.jmit.festmanagement.utils;

import android.util.Log;


/**
 * Created by Neeraj on 31-01-2016.
 */
public class FLog {
    private static String TAG = "JMIT";
    private static boolean showLog = true;

    public static void d(String VAL) {
        if (showLog) {
            d(TAG, VAL);
        }
    }


    public static void d(String TAG, String VAL) {
        if (showLog) {
            Log.d(TAG, "" + VAL);
        }
    }
}
