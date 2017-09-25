package com.jmit.festmanagement.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.utils.FLog;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;
import com.jmit.festmanagement.utils.VolleyInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by arpitkh96 on 16/10/16.
 */
public class EventDialog extends BottomSheetDialogFragment implements VolleyInterface {
    Event drawerItem;
    int bottomSheetHeight = 0;
    ContentLoadingProgressBar progressView;
    View emptyView;
    TextView content;
    TextView results;
    public static EventDialog newInstance(Event event, int peekHeight) {

        Bundle args = new Bundle();
        args.putParcelable("event", event);
        args.putInt("height", peekHeight);
        EventDialog fragment = new EventDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.event_dialog, null);
        dialog.setContentView(contentView);
        drawerItem = getArguments().getParcelable("event");
        bottomSheetHeight = getArguments().getInt("height");
        View itemView = contentView;
        TextView title, desc, venue, date;
        title = (TextView) itemView.findViewById(R.id.title);
        desc = (TextView) itemView.findViewById(R.id.desc);
        venue = (TextView) itemView.findViewById(R.id.venue);
        date = (TextView) itemView.findViewById(R.id.date);
        results=(TextView)itemView.findViewById(R.id.results);
        title.setText(drawerItem.getEventName());
        desc.setText(drawerItem.getEventDesc());
        venue.setText(drawerItem.getVenue());
        date.setText(drawerItem.getStartTime() + " to " + drawerItem.getEndTime());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        progressView=(ContentLoadingProgressBar) contentView.findViewById(R.id.event_progressBar);
        emptyView=contentView.findViewById(R.id.event_nodata);
        content=(TextView)contentView.findViewById(R.id.content);
        HashMap<String,String> ha=new HashMap<String, String>();
        ha.put("event_id",drawerItem.getEventId());
        VolleyHelper.postRequestVolley(getActivity(),this, URL_API.GET_RESULTS,ha, RequestCodes.GET_RESULTS,true);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            final BottomSheetBehavior mBottomSheetBehavior = ((BottomSheetBehavior) behavior);
            mBottomSheetBehavior.setHideable(true);
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(View bottomSheet, float slideOffset) {
                }
            });
        }
    }

    @Override
    public void requestStarted(int requestCode) {

    }

    @Override
    public void requestCompleted(int requestCode, String response) {

        FLog.d(response);
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.getInt("success")==0)
            results.setText(jsonObject.getString("message"));
            else {
                results.setText(jsonObject.getString("results"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        FLog.d(error.getMessage());
        String msg = "";
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
            msg = getResources().getString(R.string.network_error);
        } else if (error instanceof ParseError) {
            msg = "Response Parse Error";
        } else if (error instanceof AuthFailureError) {
            msg = "Authentication Failure Error";
        } else if (error instanceof ServerError) {
            msg = "Server Error";
        }
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}
