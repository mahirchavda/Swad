package com.example.android.swad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.swad.Adapters.CatagoryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class WelcomeActivity extends AppCompatActivity  {

    ArrayList<String> catagories;
    HashMap<String,Integer> hs=new HashMap<String,Integer>();
    CatagoryAdapter cad;
    RecyclerView rview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));

        catagories=new ArrayList<>();
         rview=(RecyclerView)findViewById(R.id.catagory_item_list);
         rview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DatabaseReference database= FirebaseDatabase.getInstance().getReference("dishes");

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    addchild(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                    removechild(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        CatagoryAdapter.RecyclerViewClickListerner mListener=new CatagoryAdapter.RecyclerViewClickListerner() {
            @Override
            public void onClick(View view, int position) {
                Button b=(Button)view.findViewById(R.id.catagory_name);
                Intent i=new Intent(WelcomeActivity.this,ItemActivity.class);
                i.putExtra("catagory_name",b.getText().toString());
                startActivity(i);
            }
        };


         cad=new CatagoryAdapter(catagories,mListener);
        rview.setAdapter(cad);
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
                break;
            case R.id.cart:
                startActivity(new Intent(this,CartActivity.class));
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

    void addchild(DataSnapshot dataSnapshot)
    {
        String catagory=dataSnapshot.child("catagory").getValue().toString();

        if(!hs.containsKey(catagory)) {
            hs.put(catagory, 1);
            catagories.add(catagory);
        }
        else
            hs.put(catagory,hs.get(catagory)+1);
        cad.setmValues(new ArrayList<String>(catagories));
        cad.notifyDataSetChanged();

    }

    void removechild(DataSnapshot dataSnapshot)
    {
        String catagory=dataSnapshot.child("catagory").getValue().toString();
        if(hs.get(catagory)==1)
        {
            catagories.remove(catagory);
            cad.notifyDataSetChanged();
            cad.getmValues().remove(catagory);
        }
        hs.put(catagory,hs.get(catagory)-1);
    }


}
