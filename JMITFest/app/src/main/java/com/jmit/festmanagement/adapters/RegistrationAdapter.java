package com.jmit.festmanagement.adapters;

/**
 * Created by arpitkh96 on 11/10/16.
 */
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jmit.festmanagement.R;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.data.Fest;
import com.jmit.festmanagement.data.Registration;

import java.util.List;

/**
 * Created by arpitkh96 on 2/10/16.
 */

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.MyViewHolder> {

    private List<Registration> headerList;
    private Context context;
    OnItemClickListener onItemClickListener;
    public RegistrationAdapter(List<Registration> headersList, Context context) {
        this.context = context;
        this.headerList = headersList;
    }

    public void setHeaderList(List<Registration> headerList) {
        this.headerList = headerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registration_row, null);
        return new MyViewHolder(view);
    }

    public interface OnItemClickListener {
        void onRegistrationItemClick(Registration event);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Registration drawerItem = headerList.get(position);
        holder.name.setText(drawerItem.getUserName());
        holder.dept.setText(drawerItem.getDepartment());
        holder.email.setText(drawerItem.getEmail());
        holder.roll.setText(drawerItem.getRollNo());
        holder.phone.setText(drawerItem.getPhNo());
    }


    @Override
    public int getItemCount() {
        return headerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, dept, roll, phone,email;
        private View row_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            row_view = itemView;
            name = (TextView) itemView.findViewById(R.id.title);
            dept = (TextView) itemView.findViewById(R.id.department);
            roll = (TextView) itemView.findViewById(R.id.rollno);
            phone = (TextView) itemView.findViewById(R.id.phone);
            email = (TextView) itemView.findViewById(R.id.email);
        }
    }
}
