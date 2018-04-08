package com.example.android.swad.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.swad.Entities.Order;
import com.example.android.swad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by jenil on 22-03-2018.
 */

public class Order2Adapter extends RecyclerView.Adapter<Order2Adapter.ViewHolder>{
    public List<Order> getmValues() {
        return mValues;
    }

    private  List<Order> mValues;
    private Context context;
    public Order2Adapter(List<Order> items) {
        mValues = items;
    }

    public Order2Adapter(List<Order> items, Context context) {
        mValues = items;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order2_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.ordernumber.setText(mValues.get(position).getOrdernumber().toString());
        holder.itemname.setText(mValues.get(position).getItem_name().toString());
        holder.quantity.setText(Integer.toString(mValues.get(position).getQuantity()));
        holder.time.setText(Long.toString(mValues.get(position).getWaiting_time()) + "min");
        Glide.with(context).load(mValues.get(position).getItem_image()).placeholder(R.drawable.food).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ordernumber,itemname,quantity,time;
        ImageView image;
        public ViewHolder(View view) {
            super(view);
            ordernumber=(TextView)view.findViewById(R.id.order_number);
            itemname=(TextView)view.findViewById(R.id.order_item_name);
            quantity=(TextView)view.findViewById(R.id.order_quantity);
            time=(TextView)view.findViewById(R.id.order_preparing_time);
            image=(ImageView)view.findViewById(R.id.item_image);
        }

        @Override
        public String toString() {
            return null;
        }
    }


}
