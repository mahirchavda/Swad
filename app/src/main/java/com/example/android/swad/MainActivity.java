package com.example.android.swad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.swad.Entities.Item;
import com.example.android.swad.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(this);

        TextView emptyTextView = (TextView) findViewById(R.id.empty_view);


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if ( ! (networkInfo != null && networkInfo.isConnected())) {
            emptyTextView.setText(R.string.no_internet_connection);
        }

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {

            DatabaseReference dbre = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
            dbre.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && user.getType().compareTo("U") == 0) {
                        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                        finish();
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        loginpage(null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            loginpage(null);
        }

    }

    public void loginpage(View v)
    {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }

}
