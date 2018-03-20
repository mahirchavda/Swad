package com.example.android.swad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.android.swad.Adapters.CartAdapter;
import com.example.android.swad.Adapters.CatagoryAdapter;
import com.example.android.swad.Entities.Cart;
import com.example.android.swad.Entities.Order;
import com.example.android.swad.Library.TinyDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashSet;

public class CartActivity extends AppCompatActivity {

    private HashSet<Cart> cartitems;
    private RecyclerView recyclerView;
    private CartAdapter cartadapter;
    private ArrayList<Cart> arcartitems;
    private Button remove,ordernow;
    private ArrayList<String> itemnames;
    DatabaseReference db;
    String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        super.setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        itemnames=new ArrayList<>();
        cartitems=new HashSet<Cart>();
        arcartitems=new ArrayList<>();

        final CatagoryAdapter.RecyclerViewClickListerner mlistener=new CatagoryAdapter.RecyclerViewClickListerner() {
            @Override
            public void onClick(View view,final int position) {
                Toast.makeText(CartActivity.this, "hey", Toast.LENGTH_SHORT).show();
            }
        };
        cartadapter=new CartAdapter(new ArrayList<Cart>(cartitems),mlistener);
        ordernow=(Button)findViewById(R.id.item_order);
        db= FirebaseDatabase.getInstance().getReference("orders");
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        TinyDB tiny=new TinyDB(CartActivity.this);
        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Cart c:cartitems) {
                    Order order = new Order(uid, c.getItem().getName(), "waiting", c.getQuantitiy());
                    db.push().setValue(order);
                }
                Toast.makeText(CartActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
            }
        });


        displayReceivedData(tiny.getListObject("selected_items",Cart.class));


        // Set the adapter
        recyclerView=(RecyclerView) findViewById(R.id.cart_item_list);




        recyclerView.setAdapter(cartadapter);

        RecyclerView.AdapterDataObserver adb;
        adb = new RecyclerView.AdapterDataObserver(){
            @Override
            public void onChanged() {
                itemnames.clear();
                cartitems=new HashSet<>(cartadapter.getmValues());
                for(Cart c:cartitems)
                    if(!itemnames.contains(c.getItem().getName()))
                        itemnames.add(c.getItem().getName());
                if(cartadapter.getmValues().size()==0)
                    ordernow.setVisibility(View.GONE);
                else
                    ordernow.setVisibility(View.VISIBLE);

                ArrayList<Object> a=new ArrayList<Object>(cartitems);
                new TinyDB(CartActivity.this).putListObject("selected_items",a);
            }


        };

        cartadapter.registerAdapterDataObserver(adb);


    }

    protected void displayReceivedData(ArrayList<Object> data)
    {
        //cartitems.clear();
        for(Object o:data) {
         Cart c=(Cart)o;
            if (!itemnames.contains(c.getItem().getName())) {
                itemnames.add(c.getItem().getName());
                //cartitems.add(c);
                cartadapter.getmValues().add(c);
                // cartadapter.notifyItemInserted(cartadapter.getmValues().size());
                cartadapter.notifyDataSetChanged();
            }
        }
        ArrayList<Object> a=new ArrayList<Object>(cartadapter.getmValues());
        new TinyDB(CartActivity.this).putListObject("selected_items",a);

    }

}
