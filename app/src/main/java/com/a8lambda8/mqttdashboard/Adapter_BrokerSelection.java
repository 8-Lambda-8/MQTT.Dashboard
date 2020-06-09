package com.a8lambda8.mqttdashboard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.a8lambda8.mqttdashboard.myUtils.TAG;

public class Adapter_BrokerSelection extends RecyclerView.Adapter<Adapter_BrokerSelection.MyViewHolder> {
    private List<String> mDataset;
    private OnBrokerSelectListener mOnBrokerSelectListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public View view;
        OnBrokerSelectListener onBrokerSelectListener;
        public MyViewHolder(View v,OnBrokerSelectListener onBrokerSelectListener) {
            super(v);
            view = v;
            this.onBrokerSelectListener = onBrokerSelectListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBrokerSelectListener.onBrokerClick(getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Adapter_BrokerSelection(List<String> myDataset, OnBrokerSelectListener onBrokerSelectListener) {
        mDataset =  myDataset;
        mOnBrokerSelectListener = onBrokerSelectListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Adapter_BrokerSelection.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_brokerselection, parent, false);

        MyViewHolder vh = new MyViewHolder(v, mOnBrokerSelectListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        View v = holder.view;

        holder.itemView.setLongClickable(true);

        TextView TV_brokerName = v.findViewById(R.id.tv_brokerName);

        TV_brokerName.setText(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnBrokerSelectListener{
        void onBrokerClick(int pos);
    }


}
