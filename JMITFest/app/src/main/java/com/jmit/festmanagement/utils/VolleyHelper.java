package com.jmit.festmanagement.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jmit.festmanagement.Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by arpitkh996 on 06-09-2016.
 */

public class VolleyHelper {

    public static void postRequestVolley(final Context ctx, String url, final HashMap<String, String> hm, final int request_code,boolean cache) throws NullPointerException {

        if (ctx == null)
            return;
        //Disable the onclick event of the view element

        FLog.d("Request",url);
        final VolleyInterface vi = (VolleyInterface) ctx;
        printHashMapValues(hm);
        vi.requestStarted(request_code);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // calculate the duration in milliseconds
                vi.requestCompleted(request_code, response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // calculate the duration in milliseconds
                vi.requestEndedWithError(request_code, error);

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                return checkParams(hm);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        RetryPolicy retryPolicy=new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 20000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        };
        sr.setShouldCache(cache);
        sr.setRetryPolicy(retryPolicy);
        Main.getInstance().add(sr);
    }
    public static void postRequestVolley(final Context ctx,VolleyInterface volleyInterface, String url, final HashMap<String, String> hm, final int request_code,boolean cache) throws NullPointerException {

        if (ctx == null)
            return;
        //Disable the onclick event of the view element

        FLog.d("Request",url);
        final VolleyInterface vi = volleyInterface;
        printHashMapValues(hm);
        vi.requestStarted(request_code);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // calculate the duration in milliseconds
                vi.requestCompleted(request_code, response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // calculate the duration in milliseconds
                vi.requestEndedWithError(request_code, error);

            }
        }
        )
        {
            @Override
            protected Map<String,String> getParams(){
                return checkParams(hm);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        RetryPolicy retryPolicy=new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 20000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        };
        sr.setRetryPolicy(retryPolicy);
        Main.getInstance().add(sr);
    }
    private static Map<String, String> checkParams(Map<String, String> map){
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            if(pairs.getValue()==null){
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }
    private static void printHashMapValues(HashMap<String, String> hm) {
        Set setOfKeys = hm.keySet();
        Iterator iterator = setOfKeys.iterator();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = hm.get(key);
            FLog.d("API", "Key: " + key + ", Value: " + value);
        }
    }

}