package com.roadwatch.gcarroll.irishroadwatchlive;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roadwatch.gcarroll.irishroadwatchlive.incident.Incident;

public class MainMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String LOG_TAG = MainMapActivity.class.getSimpleName();
    private GoogleMap mMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be used. This is where we
     * can add markers or lines, add listeners or move the camera. If Google Play services is not installed on the device,
     * the user will be prompted to install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Setup map features and starting position and zoom level
        mMap.setTrafficEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(53.510138, -7.865643)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6.7f));

        // Download markers from api
        updateMapData();
        // TODO Show loading

        // Get location and set default pin
        final LatLng office = new LatLng(54.266077, -8.453736);
        mMap.addMarker(new MarkerOptions().position(office).title("You are here!"));
    }

    private void updateMapData() {
        // Using Volley
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://selectunes.eu/api/test";

        // Request a string response from the url
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                final Gson gson = new Gson();
                final Type listType = new TypeToken<List<Incident>>() {
                }.getType();
                final List<Incident> incidents = gson.fromJson(response, listType);
                Log.v("Volley", "Incidents " + incidents.size());
                // Add a marker for users location
                for (final Incident incident : incidents) {
                    determineIcon(incident.getIncidentTypeID());
                    mMap.addMarker(new MarkerOptions().anchor(0, 1).title(incident.getTitle())
                            .snippet("Updated at: " + getDateFormatString(incident.getUpdatedAt()))
                            .icon(BitmapDescriptorFactory.fromResource(determineIcon(incident.getIncidentTypeID())))
                            .position(new LatLng(incident.getLatitude(), incident.getLongitude())).title(incident.getReport()));
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(final Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(final Marker marker) {

                            final LinearLayout info = new LinearLayout(getApplicationContext());
                            info.setOrientation(LinearLayout.VERTICAL);

                            final TextView title = new TextView(getApplicationContext());
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            // title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            final TextView snippet = new TextView(getApplicationContext());
                            snippet.setTextColor(Color.BLACK);
                            snippet.setGravity(Gravity.BOTTOM);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });
                }
            }

            private String getDateFormatString(final String updatedDate) {
                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
                Date date = new Date();
                try {
                    date = format.parse(updatedDate);
                } catch (final Exception e) {
                    Log.v(LOG_TAG, "Exception while trying to parse updated Date: %s" + e);
                    return updatedDate;
                }
                return date.toString();
            }

            private int determineIcon(final Integer incidentTypeID) {

                switch (incidentTypeID) {
                    case 1:
                        return R.drawable.map_control_icon_warn;
                    case 2:
                        return R.drawable.map_control_icon_car;
                    case 3:
                        return R.drawable.map_control_icon_work;
                    case 4:
                        return R.drawable.map_control_icon_flood;
                }
                return R.drawable.map_control_icon_warn;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.v("Volley", "That didn't work!");
            }
        });

        // Add request to request queue
        queue.add(stringRequest);

    }

}
