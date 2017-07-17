package com.a0quickcartgmail.quickart;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.messaging.FirebaseMessaging;

import static com.a0quickcartgmail.quickart.R.id.TextView01;

public class Notification extends AppCompatActivity {
private TextView textView;
    private ImageView imageView;
    private DatabaseReference databaseReference,unameref;
    private FirebaseAuth firebaseAuth;
    Query queryRef1;
    String uname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        textView=(TextView) findViewById(R.id.textView);
        imageView=(ImageView) findViewById(R.id.imageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                //Toast.makeText(this,"Hello"+token,Toast.LENGTH_LONG).show();
                //textView.setText(token);

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("FCM-Token").child(uname).setValue(token);}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});


        String s = getIntent().getStringExtra("Message");
        if(s!=null) {
            textView.setText(s);

            imageView.setImageResource(R.drawable.verified);
               }


    }
}
