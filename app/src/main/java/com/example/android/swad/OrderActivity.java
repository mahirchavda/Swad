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
import android.widget.Toast;

import com.example.android.swad.Adapters.Order2Adapter;
import com.example.android.swad.Adapters.OrderAdapter;
import com.example.android.swad.Entities.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {

    Order2Adapter adapter;
    RecyclerView rview;
    Order prevorder=new Order();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        adapter=new Order2Adapter(new ArrayList<Order>(),getApplicationContext());
        rview=findViewById(R.id.order_list);
        rview.setAdapter(adapter);

        Query db= FirebaseDatabase.getInstance().getReference("orders").orderByChild("ordertime");

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order o=dataSnapshot.getValue(Order.class);
                if((o.getStatus().compareTo("waiting")==0 || o.getStatus().compareTo("preparing")==0) && o.getUid().compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid())==0)
                {
                    adapter.getmValues().add(o);
                    adapter.notifyDataSetChanged();
                    rview.scheduleLayoutAnimation();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Order o=dataSnapshot.getValue(Order.class);
                if(o.getStatus().compareTo("completed")==0 && o.getUid().compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid())==0)
                {
                    int index=-1;
                    for(int i=0;i<adapter.getmValues().size();i++)
                    {
                        Order ui=adapter.getmValues().get(i);
                        if(ui.getOrdernumber().compareTo(o.getOrdernumber())==0)
                        {
                            index=i;
                        }
                        //Toast.makeText(OrderActivity.this, ui.getOrdernumber()+ " "+o.getOrdernumber()+" "+index+" "+i, Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(OrderActivity.this, ""+index, Toast.LENGTH_SHORT).show();
                    if(index!=-1)
                    adapter.getmValues().remove(index);
                    adapter.notifyDataSetChanged();
                    rview.scheduleLayoutAnimation();
                }
                if(o.getStatus().compareTo("preparing")==0 &&  o.getUid().compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid())==0 && (!o.equals(prevorder) ||  o.getQuantity()==o.getRemaining()+1 && o.getChefs().size()==1))
                {
                    //Toast.makeText(OrderActivity.this, "started preparing "+o.getQuantity()+" "+o.getCompleted(), Toast.LENGTH_SHORT).show();
                    int index=-1;
                    for(int i=0;i<adapter.getmValues().size();i++)
                    {
                        Order ui=adapter.getmValues().get(i);
                        if(ui.getOrdernumber().compareTo(o.getOrdernumber())==0)
                        {
                            index=i;
                        }
                    }
                    //int index=adapter.getmValues().indexOf(o);
                    //DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("dishes").child("name").equalTo(o.getItem_name());

                    Date d=new Date();

                    prevorder=o;
                    adapter.getmValues().get(index).setWaiting_time(o.getWaiting_time());
                    adapter.notifyDataSetChanged();
                    rview.scheduleLayoutAnimation();
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });












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
                startActivity(new Intent(this,CartActivity.class));
                break;
            case R.id.orders:
                break;
            case R.id.history:
                startActivity(new Intent(this,HistoryActivity.class));
                break;
        }
        return true;
    }
}
