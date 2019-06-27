package br.ufrn.dimap.sysaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufrn.dimap.sysaster.R;
import br.ufrn.dimap.sysaster.adapter.LocationAdapter;
import br.ufrn.dimap.sysaster.model.Location;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mDisasters;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Location> disastersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        disastersList = new ArrayList<>();

        mDisasters = findViewById(R.id.rv_disasters);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mDisasters.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new LocationAdapter(disastersList, this);
        mDisasters.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("DisasterNotification"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String encodedImage = "";
            if(intent.getExtras().getString("image") != null && !intent.getExtras().getString("image").equals("") ){
                encodedImage = intent.getExtras().getString("image");
            }

            Double lat = Double.valueOf(intent.getExtras().getString("lat"));
            Double lon = Double.valueOf(intent.getExtras().getString("lon"));

            String topic = intent.getExtras().getString("topic");

            Log.i("lat,lon: ", lat + " " + lon);
            Log.i("Image-after", encodedImage);

            //encodedImage = encodedImage.substring(2, encodedImage.length() - 1);

            disastersList.add(new Location(topic, encodedImage, lat, lon, new Date()));
            mAdapter.notifyDataSetChanged();
        }
    };
}
