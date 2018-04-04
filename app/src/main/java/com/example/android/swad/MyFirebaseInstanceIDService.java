package com.example.android.swad;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        if(user!=null) {
            Log.d(TAG, user.getUid());
            Log.d(TAG, user.getEmail());
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            if (refreshedToken != null) //
                mDatabase.child("token").setValue(refreshedToken);
            else
                Log.d(TAG, "onTokenRefresh: token was null");
        }
    }

}