package com.a0quickcartgmail.quickart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CurrentProducts extends AppCompatActivity {
    private TextView textView0,textView1, textView2;
    private DatabaseReference databaseReference1;
    Query queryRef;
    private FirebaseAuth firebaseAuth;
    private TableLayout TableLayout;
    private TableRow tablerow1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TableLayout = (TableLayout) findViewById(R.id.myTableLayout);

        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        queryRef = databaseReference1.child("Products");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    TableRow tableRow1 = new TableRow(CurrentProducts.this);
                    TextView textView0 = new TextView(CurrentProducts.this);
                    TextView textView1 = new TextView(CurrentProducts.this);
                    TextView textView2 = new TextView(CurrentProducts.this);
                    textView0.setText(product.getId());
                    textView1.setText(product.getName());
                    textView2.setText(product.getPrice()+" Rs.");
                    tableRow1.addView(textView0);
                    tableRow1.addView(textView1);
                    tableRow1.addView(textView2);
                    TableLayout.addView(tableRow1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

