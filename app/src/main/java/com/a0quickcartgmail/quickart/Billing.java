package com.a0quickcartgmail.quickart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Billing extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG ="Billing" ;
  //  private TextView textView1, textView2, textView3, textView4, GrandTotal;
    private Button Email, Download,Payment;
    private DatabaseReference databaseReference1, unameref;
    Query queryRef, queryRef1;
    private FirebaseAuth firebaseAuth;
  //  private android.widget.TableLayout TableLayout;
    private TableRow tablerow1;
    private ProgressDialog progressDialog;
    int GTT1=0,GTT = 0;
    public String string="",string1,username;
    int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Download = (Button) findViewById(R.id.Download);
        Email = (Button) findViewById(R.id.Email);
        Payment = (Button) findViewById(R.id.Pay);

        //  textView1 = (TextView) findViewById(R.id.textView1);
      //  textView2 = (TextView) findViewById(R.id.textView2);
      //  textView3 = (TextView) findViewById(R.id.textView3);
      //  textView4 = (TextView) findViewById(R.id.textView4);
       // GrandTotal = (TextView) findViewById(R.id.GrandTotal);
      //  TableLayout = (TableLayout) findViewById(R.id.myTableLayout);
       // tablerow1 = (TableRow) findViewById(R.id.tablerow1);

        Download.setOnClickListener(this);
        Email.setOnClickListener(this);
        Payment.setOnClickListener(this);



        databaseReference1 = FirebaseDatabase.getInstance().getReference();
        unameref = FirebaseDatabase.getInstance().getReference();

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
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Cart cart = postSnapshot.getValue(Cart.class);
                                TableRow tableRow1 = new TableRow(Billing.this);
                                TextView textView1 = new TextView(Billing.this);
                                TextView textView2 = new TextView(Billing.this);
                                TextView textView3 = new TextView(Billing.this);
                                TextView textView4 = new TextView(Billing.this);
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
                                GTT = GTT + cart.getTotal();
                              //  Toast.makeText(Billing.this,"hello"+GTT,Toast.LENGTH_LONG).show();
                               // GrandTotal.setText("Total: " + GTT + " Rs.");
                              //  TableLayout.addView(tableRow1, new TableLayout.LayoutParams(android.widget.TableLayout.LayoutParams.MATCH_PARENT, android.widget.TableLayout.LayoutParams.WRAP_CONTENT));
                            }


                          //  Toast.makeText(Billing.this,"hello"+GTT,Toast.LENGTH_LONG).show();


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case R.id.menu_item_2:
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
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

    }

    public void pdf(){


        Intent intent = getIntent();
        startActivity(intent);

        queryRef = databaseReference1.child("Carts").child(username);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot1) {
                for (DataSnapshot postSnapshot1 : snapshot1.getChildren()) {
                    Cart cart = postSnapshot1.getValue(Cart.class);
                    string=string +(cart.getPname()+"                       "+cart.getPprice()+"                      "+cart.getPquantity()+"                        "+cart.getTotal()+"\n");
                    GTT1 = GTT1 + cart.getTotal();
                    string1="\n\nTotal: " + GTT1 + " Rs.";

                }
                Document doc = new Document();

                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/QuicKart";

                    File dir = new File(path);
                    if(!dir.exists())
                        dir.mkdirs();



                    File file = new File(dir, "Cash_Memo.pdf");
                    FileOutputStream fOut = new FileOutputStream(file);

                    PdfWriter.getInstance(doc, fOut);

                    Paragraph watermark = new Paragraph("QUICKART", new Font(Font.FontFamily.TIMES_ROMAN, 50f, Font.BOLD, BaseColor.BLACK));
                    watermark.setAlignment(Paragraph.ALIGN_CENTER);
                    // Image image = Image.getInstance("quickart.png");


                    //open the document
                    doc.open();

                    Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 26f, Font.BOLD, BaseColor.BLACK);
                    Font f4 = new Font(Font.FontFamily.TIMES_ROMAN, 20f, Font.BOLD, BaseColor.BLACK);
                    Font f5 = new Font(Font.FontFamily.TIMES_ROMAN, 19f, Font.BOLD, BaseColor.BLACK);
                    Font f6 = new Font(Font.FontFamily.TIMES_ROMAN, 23f, Font.BOLD, BaseColor.BLACK);

                    Paragraph p0 = new Paragraph("CASH MEMO",f3);
                    Paragraph p1=new Paragraph("\n\nProduct           |             Rate       |       Quantity      |      Value\n",f4);
                    Paragraph p2 = new Paragraph(string,f5);
                    Paragraph p3 = new Paragraph(string1,f6);

                    p0.setAlignment(Paragraph.ALIGN_CENTER);
                    p1.setAlignment(Paragraph.ALIGN_LEFT);
                    p2.setAlignment(Paragraph.ALIGN_LEFT);
                    p3.setAlignment(Paragraph.ALIGN_RIGHT);

                    doc.add(watermark);
                    doc.add(p0);
                    doc.add(p1);
                    doc.add(p2);
                    doc.add(p3);

                    // doc.add(image);


                } catch (DocumentException de) {
                    Log.e("PDFCreator", "DocumentException:" + de);
                } catch (IOException e) {
                    Log.e("PDFCreator", "ioException:" + e);
                }
                finally {
                    doc.close();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        viewPdf("Cash_Memo.pdf", "QuicKart");

    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Billing.this, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }


    }









    @Override
    public void onClick(View v) {
        if(v==Payment){
            String GTT2 = Integer.toString(GTT);
            Intent i = new Intent(this,Payment.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("GrandTotal", GTT2);
            startActivity(i);
         }

        if(v==Download){
            Toast.makeText(getApplicationContext(), "Please Wait for a Moment", Toast.LENGTH_SHORT).show();
            pdf();
        }

        if(v==Email) {
            Toast.makeText(getApplicationContext(), "Please Wait for a Moment", Toast.LENGTH_SHORT).show();
            queryRef = databaseReference1.child("Carts").child(username);
                        queryRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    Cart cart = postSnapshot.getValue(Cart.class);

                                    string=string + (cart.getPname()+" | "+cart.getPprice()+" | "+cart.getPquantity()+" | "+cart.getTotal()+"\n");
                                    GTT1 = GTT1 + cart.getTotal();
                                    string1="\n\nTotal: " + GTT1 + " Rs.";
                                }
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{user.getEmail()});
                                //i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"0quickcart@gmail.com"});
                                i.putExtra(Intent.EXTRA_SUBJECT, "CASH MEMO-QuicKart");
                                i.putExtra(Intent.EXTRA_TEXT   , "\n\nCASH MEMO\n\nProduct | Rate | Quantity | Value\n\n"+string+"\n"+string1);
                                try {
                                    startActivity(Intent.createChooser(i, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(Billing.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                }




}






