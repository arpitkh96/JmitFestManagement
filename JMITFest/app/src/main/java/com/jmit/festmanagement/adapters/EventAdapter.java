package com.jmit.festmanagement.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jmit.festmanagement.R;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.data.Fest;
import com.transitionseverywhere.TransitionManager;

import java.util.List;

/**
 * Created by arpitkh96 on 2/10/16.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> headerList;
    private Context context;
    OnItemClickListener onItemClickListener;
    boolean myeventsMode = false;
    SparseBooleanArray expandArray;
    RecyclerView recyclerView;
    public EventAdapter(List<Event> headersList,RecyclerView recyclerView, Context context, boolean myeventsMode) {
        this.context = context;
        this.headerList = headersList;
        this.myeventsMode = myeventsMode;
        expandArray=new SparseBooleanArray();
        this.recyclerView=recyclerView;
        for(int i=0;i<headersList.size();i++)
            expandArray.put(i,false);
    }

    public void setHeaderList(List<Event> headerList) {
        this.headerList = headerList;
        expandArray=new SparseBooleanArray();
        for(int i=0;i<headerList.size();i++)
            expandArray.put(i,false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, null);
        return new MyViewHolder(view);
    }

    public interface OnItemClickListener {
        void onEventItemClick(Event event);

        void onRegisterClick(Event event,boolean register);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Event drawerItem = headerList.get(position);
        holder.title.setText(drawerItem.getEventName());
        holder.desc.setText(drawerItem.getEventDesc());
        holder.venue.setText(drawerItem.getVenue());
        if(!expandArray.get(position))
        {
            holder.button.setVisibility(View.GONE);
            holder.results.setVisibility(View.GONE);
            holder.imageButton.setImageResource(R.drawable.ic_keyboard_arrow_down_black);
            holder.desc.setMaxLines(2);
        }
        else {
            holder.desc.setMaxLines(Integer.MAX_VALUE);
            holder.imageButton.setImageResource(R.drawable.ic_keyboard_arrow_up_black);
            holder.button.setVisibility(View.VISIBLE);
            holder.results.setVisibility(View.VISIBLE);
        }
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.row_view.performClick();
            }
        });
        holder.row_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!expandArray.get(position))
                    expandArray.put(position,true);
                else expandArray.put(position,false);
                TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();
            }
        });
        holder.date.setText(drawerItem.getStartTime() + " to " + drawerItem.getEndTime());
        holder.results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onEventItemClick(headerList.get(position));

            }
        });
        if (drawerItem.isRegistered()) {
            if (myeventsMode) {

                holder.button.setText("UNREGISTER");
                holder.button.setTextColor(Color.parseColor("#8D6E63"));
                holder.button.setEnabled(true);
            } else {
                holder.button.setText("REGISTERED");
                holder.button.setTextColor(Color.GRAY);
                holder.button.setEnabled(false);
            }
        } else {
            holder.button.setText("REGISTER");
            holder.button.setTextColor(Color.parseColor("#795548"));
            holder.button.setEnabled(true);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onRegisterClick(headerList.get(position),!drawerItem.isRegistered());
            }
        });
    }

    public void setMyeventsMode(boolean myeventsMode) {
        this.myeventsMode = myeventsMode;
    }

    @Override
    public int getItemCount() {
        return headerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, desc, venue, date;
        private View row_view;
        AppCompatButton button,results;
        ImageButton imageButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            row_view = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
            venue = (TextView) itemView.findViewById(R.id.venue);
            date = (TextView) itemView.findViewById(R.id.date);
            button = (AppCompatButton) itemView.findViewById(R.id.register);

            results = (AppCompatButton) itemView.findViewById(R.id.results);
            imageButton = (ImageButton) itemView.findViewById(R.id.expanded_menu);
        }
    }
}