package com.example.android.swad.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.swad.R;

import java.util.List;



public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.ViewHolder> {
    public interface RecyclerViewClickListerner{
        void onClick(View view,int position);
    }


    public List<String> mValues;
     public RecyclerViewClickListerner mListener;

    public CatagoryAdapter(List<String> items, RecyclerViewClickListerner listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catagory_item, parent, false);
        return new CatagoryAdapter.ViewHolder(view,mListener);
    }

    public void setmValues(List<String> mValues) {
        this.mValues = mValues;
    }

    public List<String> getmValues() {
        return mValues;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(mValues.get(position));
        holder.mOnclick=mListener;
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mOnclick.onClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        Button name;
        RecyclerViewClickListerner mOnclick;




        public ViewHolder(View view,RecyclerViewClickListerner r) {
            super(view);
            name=(Button)view.findViewById(R.id.catagory_name);
            mOnclick=r;
        }

        @Override
        public String toString() {
            return  " ";
        }
    }
}
