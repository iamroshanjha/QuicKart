package com.a0quickcartgmail.quickart;

/**
 * Created by ricky on 1/10/2017.
 */

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private DatabaseReference databaseReference,unameref;
    private FirebaseAuth firebaseAuth;
    Query queryRef1;
    String uname="";

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        registerToken(token);
    }

    private void registerToken(String token) {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        unameref = FirebaseDatabase.getInstance().getReference();
        queryRef1 = unameref.child("Users").orderByChild("userid").equalTo(user.getUid());

        queryRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    uname=(user.getusername());}
                FirebaseMessaging.getInstance().subscribeToTopic("test");
                String token=FirebaseInstanceId.getInstance().getToken();

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("FCM-Token").child(uname).setValue(token);}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});

    }
}
