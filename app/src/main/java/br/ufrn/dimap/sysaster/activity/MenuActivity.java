package br.ufrn.dimap.sysaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

    public void teste(View view) {
        disastersList.add(new Location((long) 1, "", 4545465.4545, 4543545.4455, new Date()));
        mAdapter.notifyDataSetChanged();
    }
}
