package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.Utils;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arpitkh96 on 3/10/16.
 */

public class SplashActivity extends BaseActivity {
    String uid,pswd;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DataHandler.setRegistered_events(new ArrayList<Event>(),false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("isStudent", true)) {
            if ((uid = sharedPreferences.getString("uid", null)) != null && (pswd=sharedPreferences.getString("pswd",null))!=null) {

                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("roll_no", uid);
                hashMap.put("password", pswd);
                hashMap.put("device_id", Utils.getdeviceId(this));
                VolleyHelper.postRequestVolley(this, URL_API.LOGIN_API, hashMap, RequestCodes.LOGIN,false);
                return;
            } else new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startHome();
                }
            }, 2000);
        } else {

        }
    }

    void startHome() {
        startActivity((StudentLogin.class));
    }


    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        try {
            response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
            JSONObject jsonObject = new JSONObject(response);
            int i = jsonObject.getInt("success");
            if (i == 1) {
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putString("user", jsonObject.getString("user_name")).commit();
                editor.putString("email", jsonObject.getString("email")).commit();
                editor.putString("phone",jsonObject.getString("ph_no")).commit();
                editor.putString("user_id", jsonObject.getString("user_id")).commit();
                editor.putBoolean("isAdmin",jsonObject.getInt("isadmin")==1).commit();
                if(jsonObject.getInt("isadmin")==1)
                    editor.putString("fest_id",jsonObject.getString("fest_id")).commit();
                startActivity( MainActivity.class);
            } else startHome();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
        startHome();
        }
        }
}
