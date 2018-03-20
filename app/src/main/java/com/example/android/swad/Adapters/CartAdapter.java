package com.example.android.swad.Adapters;

/**
 * Created by jenil on 18-03-2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.swad.Entities.Cart;
import com.example.android.swad.R;
import com.example.android.swad.WelcomeActivity;

import java.util.List;


public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.ViewHolder>{
private List<Cart> mValues;
private CatagoryAdapter.RecyclerViewClickListerner mListener;
private ViewGroup parent;
private LayoutInflater inflater;
private WelcomeActivity wc;
public CartAdapter(List<Cart> items, CatagoryAdapter.RecyclerViewClickListerner mListener) {
        mValues = items;
        this.mListener=mListener;
        this.parent=parent;
        this.inflater=inflater;
        }

public void setmValues(List<Cart> mValues) {
        this.mValues = mValues;
        }

public List<Cart> getmValues() {
        return mValues;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //this.parent=parent;
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view,mListener);

        }

@Override
public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.name.setText(mValues.get(position).getItem().getName());
        holder.catagory.setText(mValues.get(position).getItem().getCatagory());
        holder.average_making_time.setText(mValues.get(position).getItem().getAverage_making_time());
        holder.image.setText(mValues.get(position).getItem().getImage());
        holder.rating.setText(mValues.get(position).getItem().getRating());
        holder.price.setText(mValues.get(position).getItem().getPrice());
        //holder.item_checkbo.setChecked(false);
        holder.quantity.setText("1");

        holder.incr.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Integer im=1+Integer.parseInt(holder.quantity.getText().toString());
        holder.quantity.setText(im.toString());
        mValues.get(position).setQuantitiy(im);
        }
        });

        holder.decr.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Integer im=Math.max(Integer.parseInt(holder.quantity.getText().toString())-1,1);
        holder.quantity.setText(im.toString());
        mValues.get(position).setQuantitiy(im);
        }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        mValues.remove(position);
        //notifyItemRemoved(position);
        //notifyItemRangeChanged(position,mValues.size());
        notifyDataSetChanged();

        }
        });
        holder.mOnclick=mListener;
        }



@Override
public int getItemCount() {
        return mValues.size();
        }

@Override
public long getItemId(int position) {
        return position;
        }

@Override
public int getItemViewType(int position) {
        return position;
        }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView name,catagory,average_making_time,image,rating,price,quantity;
    Button incr,decr,remove;
    CatagoryAdapter.RecyclerViewClickListerner mOnclick;

    public ViewHolder(View view,CatagoryAdapter.RecyclerViewClickListerner r) {
        super(view);
        name=(TextView)view.findViewById(R.id.cart_item_name);
        catagory=(TextView)view.findViewById(R.id.cart_item_catagory);
        average_making_time=(TextView)view.findViewById(R.id.cart_item_avg_time);
        price=(TextView)view.findViewById(R.id.cart_item_price);
        image=(TextView)view.findViewById(R.id.cart_item_image);
        rating=(TextView)view.findViewById(R.id.cart_item_rating);
        incr=(Button)view.findViewById(R.id.item_incr);
        decr=(Button)view.findViewById(R.id.item_decr);
        remove=(Button)view.findViewById(R.id.item_remove);
        quantity=(TextView) view.findViewById(R.id.item_quantity);
        mOnclick=r;

    }

    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public void onClick(View v) {
        mOnclick.onClick(v,getAdapterPosition());
    }
}
}
