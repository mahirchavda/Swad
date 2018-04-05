package com.example.android.swad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.swad.Adapters.CatagoryAdapter;
import com.example.android.swad.Adapters.ItemAdapter;
import com.example.android.swad.Entities.Cart;
import com.example.android.swad.Entities.Item;
import com.example.android.swad.Library.TinyDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    private RecyclerView rview;
    private ArrayList<Item> a=new ArrayList<>();
    private DatabaseReference myRef;
    private int count;
    private Button addtocart;
    private ArrayList<Object> selected_items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        addtocart=(Button)findViewById(R.id.add_to_cart);
        selected_items=new ArrayList<Object>();
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(ItemActivity.this);
                SharedPreferences.Editor ed=sh.edit();
                TinyDB tinyDB=new TinyDB(ItemActivity.this);
                ArrayList<Object> temp= tinyDB.getListObject("selected_items",Cart.class);
                Toast.makeText(ItemActivity.this, "Added to cart "+temp.size(), Toast.LENGTH_SHORT).show();
                temp.addAll(selected_items);
                tinyDB.putListObject("selected_items",temp);
                startActivity(new Intent(ItemActivity.this,CartActivity.class));
            }
        });
        // Set the adapter



        rview=(RecyclerView)findViewById(R.id.list);
        final CatagoryAdapter.RecyclerViewClickListerner listener = new CatagoryAdapter.RecyclerViewClickListerner() {
            @Override
            public void onClick(View view, final int position) {
                //Toast.makeText(getContext(), "Position " + position, Toast.LENGTH_SHORT).show();
                LinearLayout lm=(LinearLayout)view.findViewById(R.id.itemlist_item);
                ColorDrawable cdw=(ColorDrawable) lm.getBackground();

                if(cdw.getColor()== Color.WHITE) {
                    count++;
                    selected_items.add(new Cart(a.get(position),1));
                    lm.setBackgroundColor(Color.GREEN);
                }
                else {
                    count--;
                    selected_items.remove(new Cart(a.get(position),1));
                    lm.setBackgroundColor(Color.WHITE);
                }
                //
                // Toast.makeText(getContext(),""+selected_items.size(),Toast.LENGTH_SHORT).show();
                if(count>0)
                    addtocart.setVisibility(View.VISIBLE);
                else
                    addtocart.setVisibility(View.GONE);
                //Toast.makeText(getContext(),count+" ",Toast.LENGTH_SHORT).show();
            }
        };

        Intent intent=getIntent();
        String message=intent.getStringExtra("catagory_name");

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Query q=FirebaseDatabase.getInstance().getReference("dishes").orderByChild("catagory").equalTo(message);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                a.clear();
                for (DataSnapshot o:dataSnapshot.getChildren())
                    a.add(o.getValue(Item.class));

                rview.setAdapter(new ItemAdapter(a,listener,getApplicationContext()));
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
                startActivity(new Intent(this,HistoryActivity.class));
                break;
        }
        return true;
    }

}
