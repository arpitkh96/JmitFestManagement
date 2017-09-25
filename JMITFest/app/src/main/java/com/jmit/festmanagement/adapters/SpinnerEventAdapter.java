package com.jmit.festmanagement.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jmit.festmanagement.R;
import com.jmit.festmanagement.data.Event;

import java.util.ArrayList;

/**
 * Created by arpitkh96 on 11/10/16.
 */

public class SpinnerEventAdapter extends ArrayAdapter<Event>{

    public SpinnerEventAdapter(Context context, int textViewResourceId,
                           ArrayList<Event> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    public View getCustomView(int position, ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(getContext());
        View row=inflater.inflate(R.layout.spinner_row, parent, false);
        TextView label=(TextView)row.findViewById(android.R.id.text1);
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).getEventName());

        return row;
    }
}