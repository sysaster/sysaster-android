package br.ufrn.dimap.sysaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.ufrn.dimap.sysaster.R;
import br.ufrn.dimap.sysaster.activity.LocationActivity;
import br.ufrn.dimap.sysaster.model.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private static final String PATTERN_DATE = "E, dd MMM yyyy - HH:mm";
    private List<Location> locations;
    private Context context;
    private SimpleDateFormat simpleDateFormat;

    public LocationAdapter(List<Location> locations, Context context) {
        this.locations = locations;
        this.context = context;
        this.simpleDateFormat = new SimpleDateFormat(PATTERN_DATE);
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

        //holder.disasterImage(); TODO
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

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            disasterImage = itemView.findViewById(R.id.disaster_icon);
            dateTime = itemView.findViewById(R.id.tv_datetime);
            longitude = itemView.findViewById(R.id.tv_longitude);
            latitude = itemView.findViewById(R.id.tv_latitude);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LocationActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
