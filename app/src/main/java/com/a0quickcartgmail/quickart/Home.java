package com.a0quickcartgmail.quickart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private FirebaseAuth firebaseAuth;
    boolean doubleBackToExitPressedOnce = false;
    private EditText editTextID,editTextName,editTextPrice;
    private Button buttonSave,buttonShop;
    private DatabaseReference databaseReference,unameref;
    private Query queryRef1;
    private StorageReference storageReference;
    ImageView imageView;
    TextView TextView01,TextView02;



    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        unameref = FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_home);
        TextView01  = (TextView)hView.findViewById(R.id.TextView01);
        TextView02 = (TextView)hView.findViewById(R.id.TextView02);
        TextView02.setText(user.getEmail());

        queryRef1 = unameref.child("Users").orderByChild("userid").equalTo(user.getUid());

        queryRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    TextView01.setText(user.getusername());}}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});


        imageView = (ImageView)hView.findViewById(R.id.imageView);
        StorageReference riversRef = storageReference.child("Profile_Pictures/"+user.getEmail()+".jpg");
        Glide.with(this).using(new FirebaseImageLoader()).load(riversRef).into(imageView);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



      // editTextID = (EditText) findViewById(R.id.editTextID);
      // editTextName = (EditText) findViewById(R.id.editTextName);
      // editTextPrice = (EditText) findViewById(R.id.editTextPrice);
      // buttonSave = (Button) findViewById(R.id.buttonSave);
       buttonShop = (Button) findViewById(R.id.buttonShop);
      // buttonSave.setOnClickListener(this);
       buttonShop.setOnClickListener(this);

        navigationView.setNavigationItemSelectedListener(this);



        Hash_file_maps = new HashMap<String, String>();

        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        Hash_file_maps.put("Maggi", "https://n3.sdlcdn.com/imgs/b/c/d/MAGGI-2-Minute-Noodles-Masala-SDL421975270-4-1a414.jpg");
        Hash_file_maps.put("Lays", "http://www.perspectivebranding.com/images/uploads/portfolio/_960/Global_Lays_SINGLE.jpg");
        Hash_file_maps.put("Britannia Biscuits", "http://britannia.co.in/images/products/426x197/bourbon-orgina.png");
        Hash_file_maps.put("Haldiram Products", "http://4.imimg.com/data4/QS/FG/MY-16591991/haldiram-s-nut-cracker-500x500.png");

        for(String name : Hash_file_maps.keySet()){

            TextSliderView textSliderView = new TextSliderView(Home.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);
    }

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;}

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notifications) {
            Intent intent=new Intent(this,Notification.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, Home.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, MyProfile.class));
        } else if (id == R.id.nav_list) {
            startActivity(new Intent(this, CurrentProducts.class));
        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(this, Notification.class));
        }else if (id == R.id.nav_search) {
            startActivity(new Intent(this, SearchProduct.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, Aboutus.class));
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void saveProduct() {
        //Getting values from database
        String id = editTextID.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String price1 = editTextPrice.getText().toString().trim();


        if(TextUtils.isEmpty(id)){
            Toast.makeText(this, "Please Enter Product ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(price1)){
            Toast.makeText(this, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!price1.matches(".*\\d.*")){
            Toast.makeText(this, "Please enter price in number format", Toast.LENGTH_SHORT).show();
            return;
        }

        int price=Integer.parseInt(price1);
        Product product = new Product(id, name, price);


        databaseReference.child("Products").child(id).setValue(product);

        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this, Home.class));

    }


    @Override
    public void onClick(View v) {
     // if(v == buttonSave){
       //   saveProduct();
     //  }

        if(v == buttonShop){
            startActivity(new Intent(this, Shop.class));
        }



    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}


}
