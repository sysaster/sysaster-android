package br.ufrn.dimap.sysaster.activity;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufrn.dimap.sysaster.R;
import br.ufrn.dimap.sysaster.adapter.ImageAdapter;
import br.ufrn.dimap.sysaster.util.Util;

import static br.ufrn.dimap.sysaster.util.Util.PATTERN_DATE;
import static br.ufrn.dimap.sysaster.util.Util.PATTERN_DATE_TIME;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CardView mCardView;
    private Double latitude, longitude;
    private String topic;

    private RecyclerView mImages;
    private RecyclerView.Adapter mAdapter;

    private TextView tvCounter, tvDate, tvLatitude, tvLongitude, tvTopic;
    private SimpleDateFormat simpleDateFormat;
    private ImageView disasterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mCardView = findViewById(R.id.map_cardview);
        mCardView.setVisibility(View.GONE);

        simpleDateFormat = new SimpleDateFormat(PATTERN_DATE);

        Intent intent = getIntent();
        latitude = Double.valueOf(intent.getStringExtra("lat"));
        longitude = Double.valueOf(intent.getStringExtra("lon"));
        topic = intent.getStringExtra("topic");
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("image");

        tvTopic = findViewById(R.id.tv_topic_location);
        tvCounter = findViewById(R.id.tv_people_location);
        tvDate = findViewById(R.id.tv_datetime_location);
        tvLatitude = findViewById(R.id.tv_latitude_location);
        tvLongitude = findViewById(R.id.tv_longitude_location);
        disasterImage = findViewById(R.id.map_icon_disaster);

        tvDate.setText("Date: " + simpleDateFormat.format(new Date()));
        tvLatitude.setText("Latitude: " + latitude);
        tvLongitude.setText("Longitude: " + longitude);
        tvTopic.setText("Topic: " + topic);
        disasterImage.setBackground(new BitmapDrawable(getResources(), bitmap));

        List<String> data = new ArrayList<>();
        mImages = findViewById(R.id.rv_images);
        mImages.setVisibility(View.GONE);
        mImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ImageAdapter(data, this);
        mImages.setAdapter(mAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArray = new JsonArrayRequest(Request.Method.GET,
                Util.DETECTIONS_URL + "?topic.code=" + topic + "&latitude=" + latitude + "&longitude=" + longitude, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> data = new ArrayList<>();
                            for(int i = 0; i < response.length(); ++i){
                                JSONObject o = response.getJSONObject(i);
                                data.add( o.getString("clipped_image") );
                            }
                            tvCounter.setText(String.valueOf(data.size()));
                            mAdapter = new ImageAdapter(data, getApplicationContext());
                            mImages.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.i("Error: ", error.getMessage());
                        Toast.makeText(getApplicationContext(), "It was not possible to subscribe!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArray);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(location).title("Disaster"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .tilt(45)
                .zoom(17)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition((cameraPosition)));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mCardView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                mCardView.setVisibility(View.GONE);
            }
        });

    }

    public void showImages(View view) {
        if(mImages.getVisibility() == View.GONE){
            mImages.setVisibility(View.VISIBLE);
        }else{
            mImages.setVisibility(View.GONE);
        }
    }
}
