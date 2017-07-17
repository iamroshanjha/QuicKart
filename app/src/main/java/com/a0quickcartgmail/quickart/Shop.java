package com.a0quickcartgmail.quickart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Shop extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private DatabaseReference databaseReference1,databaseReference3,unameref;
    Query queryRef,queryRef1;
    String a,pquantity1;
    String pname;
    int pprice,pquantity;
    String pid;
    String uname;

    private FirebaseAuth firebaseAuth;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        firebaseAuth = FirebaseAuth.getInstance();
        unameref = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        QrScanner();
    }

    public void QrScanner(){
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();// Start camera

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();// Stop camera on pause

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case R.id.action_cart:
                  startActivity(new Intent(this, Carting.class));
                  return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 200);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);
        v.vibrate(200);


        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        a=rawResult.getText();
        databaseReference1  = FirebaseDatabase.getInstance().getReference();
        queryRef = databaseReference1.child("Products").orderByChild("id").equalTo(a);
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(a)){
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        //Getting the data from snapshot
                        Product product = postSnapshot.getValue(Product.class);
                        pid = product.getId();
                        pname = product.getName();
                        pprice = product.getPrice();


                        // show the scanner result into dialog box.
                        AlertDialog.Builder builder = new AlertDialog.Builder(Shop.this);
                        builder.setTitle("Product Description");
                        final EditText input = new EditText(Shop.this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);


                        builder.setMessage(pname + "   " + pprice + " Rs.\n\nQuantity (in digits) ")

                                .setCancelable(false)

                                .setPositiveButton("Add to Cart", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        queryRef1 = unameref.child("Users").orderByChild("userid").equalTo(user.getUid());

                                        queryRef1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot2) {
                                                for (DataSnapshot postSnapshot2 : snapshot2.getChildren()) {
                                                    User user1 = postSnapshot2.getValue(User.class);
                                                    uname = user1.getusername();
                                                    pquantity1 = input.getText().toString().trim();
                                                    pquantity = Integer.parseInt(pquantity1);
                                                }
                                                Cart cart = new Cart(pid, pname, pprice, pquantity);
                                                databaseReference3.child("Carts").child(uname).child(pid).setValue(cart);
                                                Toast.makeText(Shop.this, pname + " Added to Cart", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                        dialog.cancel();
                                        mScannerView.resumeCameraPreview(Shop.this);
                                    }
                                })
                                .setNegativeButton("Not Interested", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        mScannerView.resumeCameraPreview(Shop.this);
                                    }
                                });

                        AlertDialog alert1 = builder.create();
                        alert1.show();
                    }
            }else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Shop.this);
                    builder.setCancelable(false);
                    builder.setMessage("Invalid QR Code")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    mScannerView.resumeCameraPreview(Shop.this);
                                }
                            });
                    builder.create();
                    builder.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });}


}