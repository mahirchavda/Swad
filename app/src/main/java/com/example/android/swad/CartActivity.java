package com.example.android.swad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.android.swad.Adapters.CartAdapter;
import com.example.android.swad.Adapters.CatagoryAdapter;
import com.example.android.swad.Entities.Cart;
import com.example.android.swad.Entities.Order;
import com.example.android.swad.Library.TinyDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Date;
public class CartActivity extends AppCompatActivity {

    private HashSet<Cart> cartitems;
    private RecyclerView recyclerView;
    private CartAdapter cartadapter;
    private ArrayList<Cart> arcartitems;
    private Button remove,ordernow;
    private ArrayList<String> itemnames;
    private HashMap<String,Long> as=new HashMap<>();
    DatabaseReference db;
    String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
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
                for(Cart c:cartadapter.getmValues()) {
                    Order order = new Order(uid, c.getItem().getName(), "waiting", c.getQuantitiy());
                    order.setOrdernumber(db.push().getKey());
                      String mini="";
                      long min=Long.MAX_VALUE;
                    for(String key:as.keySet())
                    {
                        if(as.get(key)<min) {
                            min = as.get(key);
                            mini=key;
                        }
                    }


                    Long minutes=Math.max(0,(min-new Date().getTime()))/(60*1000)+Long.parseLong(c.getItem().getAverage_making_time())*(c.getQuantitiy());
                    order.setItem_waiting_time(Integer.parseInt(c.getItem().getAverage_making_time()));
                    order.setWaiting_time(minutes);
                   //order.setChefs(new ArrayList<String>());
                   order.setRemaining(c.getQuantitiy());
                   FirebaseDatabase.getInstance().getReference("orders/"+ order.getOrdernumber()).setValue(order);
                   //db.push().setValue(order);
                }
                Toast.makeText(CartActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
            }
        });



        DatabaseReference db=FirebaseDatabase.getInstance().getReference("chefs");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    as.put(d.getKey(),(Long)d.getValue());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_options,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);
                sh.edit().putString("username","1111");
                Intent i=new Intent(this,LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.dishes:
                startActivity(new Intent(this,WelcomeActivity.class));
                break;
            case R.id.cart:
                break;
            case R.id.orders:
                startActivity(new Intent(this,OrderActivity.class));
                break;
            case R.id.history:
                startActivity(new Intent(this,HistoryActivity.class));
                break;
        }
        return true;
    }


}
