package com.roadwatch.gcarroll.irishroadwatchlive;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roadwatch.gcarroll.irishroadwatchlive.incident.Incident;

public class MainMapActivity extends AppCompatActivity implements OnMapReadyCallback {

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
        final Type listType = new TypeToken<List<Incident>>() {}.getType();
        final List<Incident> incidents = gson.fromJson(response, listType);
        Log.v("Volley", "Number of Incidents " + incidents.size());
        // Add a marker for users location
          for (Incident incident: incidents) {
              mMap.addMarker(new MarkerOptions().position(new LatLng(incident.getLatitude(), incident.getLongitude())).title
                      (incident.getReport()));
          }
        // Add markers for all incidents
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
