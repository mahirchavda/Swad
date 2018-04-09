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

import com.example.android.swad.Adapters.OrderAdapter;
import com.example.android.swad.Entities.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView rview;
    OrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        adapter=new OrderAdapter(new ArrayList<Order>(),getApplicationContext());
        rview=findViewById(R.id.history_order_list);
        rview.setAdapter(adapter);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("orders");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order o=dataSnapshot.getValue(Order.class);
                if(o.getStatus().compareTo("completed")==0 && o.getUid().compareTo(FirebaseAuth.getInstance().getCurrentUser().getUid())==0) {
                    adapter.getmValues().add(0,o);
                    adapter.notifyDataSetChanged();
                    rview.scheduleLayoutAnimation();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                   /* Order o=dataSnapshot.getValue(Order.class);
                    if(o.getStatus().compareTo("completed")==0) {
                        adapter.getmValues().add(0,o);
                        adapter.notifyDataSetChanged();
                    }
                    */
                Toast.makeText(HistoryActivity.this, "changed", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(this,OrderActivity.class));
                break;
            case R.id.history:
                break;
        }
        return true;
    }


}
