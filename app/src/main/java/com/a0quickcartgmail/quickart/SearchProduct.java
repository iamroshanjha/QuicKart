package com.a0quickcartgmail.quickart;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;




import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class SearchProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);// remove horizontal x labels and line
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);// remove vertical labels and lines
        int imageResource = getResources().getIdentifier("@drawable/floorblank", null, getPackageName());
        Drawable graph_background = getResources().getDrawable(imageResource);
        graph.setBackground(graph_background);



        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Source");
        categories.add("Entry");
        categories.add("Staricase1");
        categories.add("Staricase2");
        categories.add("Exit");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(1);



        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories1 = new ArrayList<String>();
        categories1.add("Destination");
        categories1.add("Maggi");
        categories1.add("Haldiram Chiwda");
        categories1.add("Britannia Biscuit");
        categories1.add("Lays");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter1);

    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        String item = spinner.getSelectedItem().toString();
        String item1 = spinner1.getSelectedItem().toString();
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();

        int imageResource = getResources().getIdentifier("@drawable/floorbritania", null, getPackageName());
        Drawable graph_background = getResources().getDrawable(imageResource);


        int imageResource1 = getResources().getIdentifier("@drawable/floormaggi", null, getPackageName());
        Drawable graph_background1 = getResources().getDrawable(imageResource1);

        int imageResource2 = getResources().getIdentifier("@drawable/floorlays", null, getPackageName());
        Drawable graph_background2 = getResources().getDrawable(imageResource2);

        int imageResource3 = getResources().getIdentifier("@drawable/floorhaldirams", null, getPackageName());
        Drawable graph_background3 = getResources().getDrawable(imageResource3);


        if(item.contentEquals("Source") || item1.contentEquals("Destination")){
            //email is empty
            Toast.makeText(this, "Select Proper Source and Destination", Toast.LENGTH_SHORT).show();
            //stop the function
            return;
        }

        else{
            Toast.makeText(this, "Selected: "+item+" to "+item1, Toast.LENGTH_SHORT).show();
        }



        switch(item+"&"+item1){

            case "Staricase1&Britannia Biscuit":
                graph.setBackground(graph_background);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.55, 8.2), new DataPoint(0.8 ,5.7), //st1 to britannia
                });
                series.setDrawDataPoints(true);
                series.setDataPointsRadius(10);
                graph.addSeries(series);
                break;

            case "Staricase1&Maggi":
                graph.setBackground(graph_background1);
                LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.35, 4.7), new DataPoint(0.55 ,8.2), //st1 to britannia
                });
                series1.setDrawDataPoints(true);
                series1.setDataPointsRadius(10);
                graph.addSeries(series1);
                break;

            case "Staricase1&Lays":
                graph.setBackground(graph_background2);
                LineGraphSeries<DataPoint> series10 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.6, 8.2), new DataPoint(0.72 ,5.7),
                        new DataPoint(0.72 ,5.7),new DataPoint(0.73 ,2.5),//st1 to britannia
                });
                series10.setDrawDataPoints(true);
                series10.setDataPointsRadius(10);
                graph.addSeries(series10);
                break;

            case "Staricase1&Haldiram Chiwda":
                graph.setBackground(graph_background3);
                LineGraphSeries<DataPoint> series15 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.36, 8.2), new DataPoint(0.55 ,8.2), //st2 to maggi
                });
                series15.setDrawDataPoints(true);
                series15.setDataPointsRadius(10);
                graph.addSeries(series15);
                break;


            case "Staricase2&Maggi":
                graph.setBackground(graph_background1);
                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.20, 2.5), new DataPoint(0.35 ,4), //st2 to maggi
                });
                series2.setDrawDataPoints(true);
                series2.setDataPointsRadius(10);
                graph.addSeries(series2);
                break;


            case "Staricase2&Lays":
                graph.setBackground(graph_background2);
                LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.20, 2.5), new DataPoint(0.6 ,2.25), //st2 to Lays
                });
                series3.setDrawDataPoints(true);
                series3.setDataPointsRadius(10);
                graph.addSeries(series3);
                break;

            case "Staricase2&Britannia Biscuit":
                graph.setBackground(graph_background);
                LineGraphSeries<DataPoint> series8 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.20, 2.5), new DataPoint(0.7 ,2.5), //st2 to Lays
                        new DataPoint(0.7, 2.5), new DataPoint(0.71 ,5), //brit to Lays

                });
                series8.setDrawDataPoints(true);
                series8.setDataPointsRadius(10);
                graph.addSeries(series8);
                break;

            case "Staricase2&Haldiram Chiwda":
                graph.setBackground(graph_background3);
                LineGraphSeries<DataPoint> series9 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.20, 2.5), new DataPoint(0.35 ,2.5), //st2 to Lays
                        new DataPoint(0.35, 2.5), new DataPoint(0.36 ,8.2), //brit to Lays

                });
                series9.setDrawDataPoints(true);
                series9.setDataPointsRadius(10);
                graph.addSeries(series9);
                break;

            case "Entry&Lays":
                graph.setBackground(graph_background2);
                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0.55, 0.9), new DataPoint(0.6 ,2),    //entry to lays
                });
                series4.setDrawDataPoints(true);
                series4.setDataPointsRadius(10);
                graph.addSeries(series4);
                break;

            case "Entry&Britannia Biscuit":
                graph.setBackground(graph_background);
                LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0.55, 0.9), new DataPoint(0.8 ,5),    //entry to britannia
                });
                series5.setDrawDataPoints(true);
                series5.setDataPointsRadius(10);
                graph.addSeries(series5);
                break;

            case "Entry&Haldiram Chiwda":
                graph.setBackground(graph_background3);
                LineGraphSeries<DataPoint> series6 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0.31, 8.2), new DataPoint(0.50 ,0.7),  //entry to haldiram
                });
                series6.setDrawDataPoints(true);
                series6.setDataPointsRadius(10);
                graph.addSeries(series6);
                break;

            case "Entry&Maggi":
                graph.setBackground(graph_background1);
                LineGraphSeries<DataPoint> series7 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0.35, 4), new DataPoint(0.50 ,0.7)    //entry to maggi
                });
                series7.setDrawDataPoints(true);
                series7.setDataPointsRadius(10);
                graph.addSeries(series7);
                break;

            case "Exit&Lays":
                graph.setBackground(graph_background2);
                LineGraphSeries<DataPoint> series11 = new LineGraphSeries<>(new DataPoint[] {
                       new DataPoint(0.65, 2.5), new DataPoint(0.85 ,3.5),
                });
                series11.setDrawDataPoints(true);
                series11.setDataPointsRadius(10);
                graph.addSeries(series11);
                break;

            case "Exit&Britannia Biscuit":
                graph.setBackground(graph_background);
                LineGraphSeries<DataPoint> series12 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.7, 5), new DataPoint(0.85 ,3.5),
                });
                series12.setDrawDataPoints(true);
                series12.setDataPointsRadius(10);
                graph.addSeries(series12);
                break;

            case "Exit&Maggi":
                graph.setBackground(graph_background1);
                LineGraphSeries<DataPoint> series13 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.35, 4), new DataPoint(0.35 ,2.6),
                        new DataPoint(0.35, 2.6), new DataPoint(0.65 ,2.6),
                        new DataPoint(0.65, 2.6), new DataPoint(0.85 ,3.5),
                });
                series13.setDrawDataPoints(true);
                series13.setDataPointsRadius(10);
                graph.addSeries(series13);
                break;

            case "Exit&Haldiram Chiwda":
                graph.setBackground(graph_background3);
                LineGraphSeries<DataPoint> series14 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0.35, 8.2), new DataPoint(0.35 ,2.6),
                        new DataPoint(0.35, 2.6), new DataPoint(0.65 ,2.6),
                        new DataPoint(0.65, 2.6), new DataPoint(0.85 ,3.5),
                });
                series14.setDrawDataPoints(true);
                series14.setDataPointsRadius(10);
                graph.addSeries(series14);
                break;



        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


