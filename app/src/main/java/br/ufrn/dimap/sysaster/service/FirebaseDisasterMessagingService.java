package br.ufrn.dimap.sysaster.service;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufrn.dimap.sysaster.util.Util;


public class FirebaseDisasterMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FBDisasterMsgService";
    private LocalBroadcastManager broadcastManager;

    public FirebaseDisasterMessagingService() {
        this.broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            final Intent intent = new Intent("DisasterNotification");
            intent.putExtra("lat", remoteMessage.getData().get("latitude"));
            intent.putExtra("lon", remoteMessage.getData().get("longitude"));
            intent.putExtra("topic", remoteMessage.getData().get("topic"));

            String id = remoteMessage.getData().get("id");

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.GET, Util.DETECTIONS_URL + "/" + id, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String clipped_image = response.getString("clipped_image");
                                Log.i("IMG-VOLLEY: ", clipped_image);
                                intent.putExtra("image", clipped_image);
                                broadcastManager.sendBroadcast(intent);
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
            requestQueue.add(jsonobj);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
