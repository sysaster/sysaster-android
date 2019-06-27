package br.ufrn.dimap.sysaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ufrn.dimap.sysaster.R;
import br.ufrn.dimap.sysaster.activity.LocationActivity;
import br.ufrn.dimap.sysaster.model.Location;

import static br.ufrn.dimap.sysaster.util.Util.PATTERN_DATE_TIME;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Location> locations;
    private Context context;
    private SimpleDateFormat simpleDateFormat;

    public LocationAdapter(List<Location> locations, Context context) {
        this.locations = locations;
        this.context = context;
        this.simpleDateFormat = new SimpleDateFormat(PATTERN_DATE_TIME);
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.disaster_item, parent, false);
        LocationViewHolder lvh = new LocationViewHolder(ll);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.latitude.setText("Latitude: " + String.valueOf(locations.get(position).getLatitude()));
        holder.longitude.setText("Longitude: " + String.valueOf(locations.get(position).getLongitude()));
        holder.dateTime.setText(simpleDateFormat.format(locations.get(position).getDate()));

        if(!locations.get(position).getUrlImage().equals("")){
            Log.i("Image-loc", String.valueOf(locations.get(position).getUrlImage().length()));
            String encodedImage = locations.get(position).getUrlImage();
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Log.i("Image-size", String.valueOf(decodedString.length));
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.disasterImage.setBackground(new BitmapDrawable(context.getResources(), decodedByte));
        }
        holder.topic = locations.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTime;
        private TextView latitude;
        private TextView longitude;
        private ImageView disasterImage;
        private String topic;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            disasterImage = itemView.findViewById(R.id.disaster_icon);
            dateTime = itemView.findViewById(R.id.tv_datetime);
            longitude = itemView.findViewById(R.id.tv_longitude);
            latitude = itemView.findViewById(R.id.tv_latitude);
            topic = "";

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LocationActivity.class);

                    Log.i("LOCATION: ", latitude.getText().toString().substring(10) + "," + longitude.getText().toString().substring(11));

                    intent.putExtra("lat", latitude.getText().toString().substring(10));
                    intent.putExtra("lon", longitude.getText().toString().substring(11));
                    intent.putExtra("topic", topic);

                    Bitmap bitmap = ((BitmapDrawable) disasterImage.getBackground()).getBitmap();
                    intent.putExtra("image", bitmap);

                    context.startActivity(intent);
                }
            });
        }
    }
}
