package br.ufrn.dimap.sysaster.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.HashMap;

import br.ufrn.dimap.sysaster.R;
import br.ufrn.dimap.sysaster.util.Util;

public class TopicRegisterActivity extends AppCompatActivity {

    private Button mSubscribe;
    private EditText mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_register);

        mSubscribe = findViewById(R.id.bt_subscribe);
        mCode = findViewById(R.id.et_topic);

        mSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mCode.getText().toString();
                makeRequest(code);
            }
        });
    }

    private void makeRequest(String code){

        final HashMap<String, String> data = new HashMap<>();

        data.put("topic",code);
        Log.i("TOPIC", code);

        FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(TopicRegisterActivity.this, "Token not found!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Get new Instance ID token
                    data.put("token",task.getResult().getToken());
                    //data.put("token","dhsajkhdjsad");
                    Log.i("TOKEN", data.get("token"));

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, Util.SUBSCRIBE_URL, new JSONObject(data),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(TopicRegisterActivity.this, "Subscribed with success!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Log.i("Error: ", error.getMessage());
                                    Toast.makeText(TopicRegisterActivity.this, "It was not possible to subscribe!", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    requestQueue.add(jsonobj);

                }
            });


    }
}
