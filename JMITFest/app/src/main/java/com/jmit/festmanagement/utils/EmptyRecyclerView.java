package com.jmit.festmanagement.utils;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by arpitkh996 on 10-07-2016.
 */

public class EmptyRecyclerView extends RecyclerView {
    int current_mode = 0;
    private View emptyView;
    ContentLoadingProgressBar progressView;
    boolean taskRunning, refresh = false;
    final private RecyclerView.AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void checkIfEmpty() {
        if (emptyView != null && progressView != null && getAdapter() != null) {
            if (refresh) {
                setDataMode();
                return;
            }
            if (taskRunning) {
                setProgressMode();
                return;
            } else {
                boolean emptyViewVisible = getAdapter().getItemCount() == 0;
                if (emptyViewVisible) setEmptyMode();
                else setDataMode();
            }
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }


    public void setEmptyView(ContentLoadingProgressBar progressView, View emptyView) {
        this.emptyView = emptyView;
        this.progressView = progressView;
    }

    public void setRefreshing() {
        refresh = true;
        checkIfEmpty();
    }

    public void setTaskRunning(boolean taskRunning) {
        this.taskRunning = taskRunning;
        if (!taskRunning) refresh = false;
        checkIfEmpty();
        System.out.println("running" + taskRunning);
    }

    void setEmptyMode() {
        progressView.hide();
        emptyView.setVisibility(VISIBLE);
        setVisibility(GONE);
    }

    void setProgressMode() {
        progressView.show();
        progressView.setVisibility(VISIBLE);
        emptyView.setVisibility(GONE);
        setVisibility(GONE);
    }

    void setDataMode() {
        progressView.hide();
        emptyView.setVisibility(GONE);
        setVisibility(VISIBLE);
    }

}