package com.example.android.swad.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.swad.Entities.Item;
import com.example.android.swad.R;

import java.util.List;

/**
 * Created by jenil on 18-03-2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<Item> mValues;
    private Context context;

    //private final OnListFragmentInteractionListener mListener;
    private CatagoryAdapter.RecyclerViewClickListerner mListener;
    public ItemAdapter(List<Item> items, CatagoryAdapter.RecyclerViewClickListerner listener) {
        mValues = items;
        mListener = listener;

    }

    public ItemAdapter(List<Item> items, CatagoryAdapter.RecyclerViewClickListerner listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dishes_item, parent, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.name.setText(mValues.get(position).getName());
        //holder.catagory.setText(mValues.get(position).getCatagory());
        holder.average_making_time.setText(mValues.get(position).getAverage_making_time());
        //holder.image.setText(mValues.get(position).getImage());
        holder.rating.setText(mValues.get(position).getRating());
        holder.price.setText(mValues.get(position).getPrice());
        Glide.with(context).load(mValues.get(position).getImage()).into(holder.image);
        holder.mOnclicklisterner=mListener;






    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,catagory,average_making_time,rating,price;
        //CheckBox item_checkbox;
        ImageView image;
        private CatagoryAdapter.RecyclerViewClickListerner mOnclicklisterner;


        public ViewHolder(View view,CatagoryAdapter.RecyclerViewClickListerner r) {
            super(view);
            name=(TextView)view.findViewById(R.id.item_name);
            //catagory=(TextView)view.findViewById(R.id.item_catagory);
            average_making_time=(TextView)view.findViewById(R.id.item_avg_time);
            price=(TextView)view.findViewById(R.id.item_price);
            image=(ImageView)view.findViewById(R.id.item_image);
            rating=(TextView)view.findViewById(R.id.item_rating);
            LinearLayout lm=(LinearLayout) view.findViewById(R.id.itemlist_item);
            lm.setBackgroundColor(Color.WHITE);
            mOnclicklisterner=r;
            view.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() ;
        }


        @Override
        public void onClick(View v) {
            mOnclicklisterner.onClick(v,getAdapterPosition());
        }
    }
}
