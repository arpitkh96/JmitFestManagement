package com.jmit.festmanagement.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmit.festmanagement.R;
import com.jmit.festmanagement.data.Fest;

import java.util.List;

/**
 * Created by lively on 12/9/16.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {

    private List<Fest> headerList;
    private Context context;
    OnItemClickListener onItemClickListener;
    public DrawerAdapter(List<Fest> headersList, Context context) {
        this.context = context;
        this.headerList = headersList;
    }

    public void setHeaderList(List<Fest> headerList) {
        this.headerList = headerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, null);
        return new MyViewHolder(view);
    }
    public interface OnItemClickListener{
        void onDrawerItemClick(int item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Fest drawerItem = headerList.get(position);
        holder.headerText.setText(drawerItem.getFestName());
        holder.row_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                    onItemClickListener.onDrawerItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return headerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView headerText;
        private View row_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            row_view = itemView;
            headerText = (TextView) itemView.findViewById(R.id.headerItems);
        }
    }
}