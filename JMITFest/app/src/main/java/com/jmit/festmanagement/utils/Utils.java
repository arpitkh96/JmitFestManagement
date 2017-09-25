package com.jmit.festmanagement.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by arpitkh996 on 06-09-2016.
 */

public class Utils {

    public static String getdeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
