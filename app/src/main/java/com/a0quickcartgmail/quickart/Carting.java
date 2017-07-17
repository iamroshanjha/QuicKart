package com.a0quickcartgmail.quickart;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carting extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CartActivity";
    private TextView textView1, textView2, textView3, textView4,textView, GrandTotal;
    private Button Button02;
    private DatabaseReference databaseReference1, unameref,unameref2,databaseReference3,databaseReference2;
    Query queryRef, queryRef1,queryRef2,queryRef3;
    private FirebaseAuth firebaseAuth;
    private TableLayout TableLayout;
    private TableRow tablerow1;
    int GTT = 0;
    String string1,string2,username;
    int string3,string4;
    private Boolean isstop=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carting);
        Button02 = (Button) findViewById(R.id.Button02);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        GrandTotal = (TextView) findViewById(R.id.GrandTotal);
        TableLayout = (TableLayout) findViewById(R.id.myTableLayout);
        tablerow1 = (TableRow) findViewById(R.id.tablerow1);
        textView = (TextView) findViewById(R.id.textView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button02.setOnClickListener(this);
        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        unameref = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        unameref2 = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        queryRef1 = unameref.child("Users").orderByChild("userid").equalTo(user.getUid());

        queryRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    username = user.getusername();

                    queryRef = databaseReference1.child("Carts").child(username);
                    queryRef.addValueEventListener(new ValueEventListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChildren()){
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                final Cart cart = postSnapshot.getValue(Cart.class);
                                final TableRow tableRow1 = new TableRow(Carting.this);
                                final TextView textView1 = new TextView(Carting.this);
                                TextView textView2 = new TextView(Carting.this);
                                TextView textView3 = new TextView(Carting.this);
                                TextView textView4 = new TextView(Carting.this);

                                textView1.setText(cart.getPname());
                                textView3.setText(cart.getPprice() + ".00");
                                textView2.setText(cart.getPquantity() + "");
                                textView4.setText(cart.getTotal() + ".00");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                textView4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


                                tableRow1.addView(textView1);
                                tableRow1.addView(textView3);
                                tableRow1.addView(textView2);
                                tableRow1.addView(textView4);

                                tableRow1.setPadding(0,10,0,10);
                                GTT = GTT + cart.getTotal();
                                GrandTotal.setText("Total: " + GTT + " Rs.");
                                TableLayout.addView(tableRow1, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                                tableRow1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        // int row_id= TableLayout.indexOfChild(tableRow1);
                                        // String str=Integer.toString(row_id);

                                        final String pid = cart.getPid();
                                        final int price = cart.getPprice();
                                        final String pname = cart.getPname();

                                        AlertDialog dialog = new AlertDialog.Builder(Carting.this)
                                                .setCancelable(true)
                                                .setTitle(pname)
                                                .setMessage(string1)
                                                .setPositiveButton("Delete Item", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        databaseReference2.child("Carts").child(username).child(pid).setValue(null);
                                                        Toast.makeText(Carting.this, pname + " Deleted from Cart", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        Intent intent = getIntent();
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setNegativeButton("Modify Quantity", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Carting.this);
                                                        builder.setTitle("Enter New Quantity");
                                                        final EditText input = new EditText(Carting.this);
                                                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                        builder.setView(input);
                                                        builder.setNeutralButton("Submit", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                String pquantity1 = input.getText().toString().trim();
                                                                int pquantity = Integer.parseInt(pquantity1);
                                                                Cart cart = new Cart(pid, pname, price, pquantity);
                                                                databaseReference3.child("Carts").child(username).child(pid).setValue(cart);
                                                                finish();
                                                                Intent intent = getIntent();
                                                                startActivity(intent);
                                                            }
                                                        });
                                                        builder.create();
                                                        builder.show();
                                                    }
                                                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {

                                                    }
                                                })
                                                .create();
                                        dialog.show();
                                    }
                                });
                            }
                            }else{
                                tablerow1.setVisibility(View.INVISIBLE);
                                textView.setVisibility(View.INVISIBLE);
                                int imageResource = getResources().getIdentifier("@drawable/cartempt", null, getPackageName());
                                Drawable imageview = getResources().getDrawable(imageResource);
                                TableLayout.setBackground(imageview);

                                //GrandTotal.setText("Oops No Item in Cart");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case R.id.menu_item_1:

                AlertDialog dialog = new AlertDialog.Builder(Carting.this)
                        .setCancelable(true)
                        .setTitle("Confirmation")
                        .setMessage("Please Confirm to Clear Your Cart")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                queryRef1 = databaseReference1.child("Carts").child(username);
                                queryRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, "onCancelled", databaseError.toException());
                                    }
                                });
                                finish();
                                startActivity(new Intent(Carting.this, Carting.class));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {


                                finish();
                                startActivity(new Intent(Carting.this, Carting.class));

                            }
                        })
                        .create();
                dialog.show();
                return true;

            case R.id.menu_item_2:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, Login.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {

        if (v == Button02) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Carting.this);
            builder.setTitle("Confirmation to Proceed");
            builder.setMessage("You Won't be able to do changes again in Cart")
                    .setCancelable(false)

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            startActivity(new Intent(Carting.this,Billing.class));

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = getIntent();
                            startActivity(intent);
                        }
                    });

            AlertDialog alert1 = builder.create();
            alert1.show();
        }





    }


}
