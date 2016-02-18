package com.roadwatch.gcarroll.irishroadwatchlive;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

public class MainActivity extends AppCompatActivity
    implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  private final String LOG_TAG = MainActivity.class.getSimpleName();

  private GoogleMap mMap;

  private GoogleApiClient mGoogleApiClient;

  private Location mLastLocation;

  private DrawerLayout mDrawerLayout;

  private ListView mDrawerList;

  private ActionBarDrawerToggle mDrawerToggle;

  private CharSequence mDrawerTitle;

  private CharSequence mTitle;

  private String[] mMenuTitles;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    // Create an instance of GoogleAPIClient.
    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    mTitle = mDrawerTitle = getTitle();
    mMenuTitles = getResources().getStringArray(R.array.menu_array);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    // Set the adapter for the list view
    // set up the drawer's list view with items and click listener
    mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mMenuTitles));
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    // enable ActionBar app icon to behave as action to toggle nav drawer
    // getActionBar().setDisplayHomeAsUpEnabled(true);
    // getActionBar().setHomeButtonEnabled(true);

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    final Toolbar toolbarImg = new Toolbar(this);
    toolbarImg.setNavigationIcon(R.drawable.ic_drawer);

    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbarImg, R.string.drawer_open,
        R.string.drawer_close) {
      public void onDrawerClosed(final View view) {
        // getActionBar().setTitle(mTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      public void onDrawerOpened(final View drawerView) {
        // getActionBar().setTitle(mDrawerTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    if (savedInstanceState == null) {
      selectItem(0);
    }
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
    mMap.setTrafficEnabled(false);
    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(53.510138, -7.865643)));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(6.7f));

    // Download markers from api
    updateMapData();
    // TODO Show loading

    // TODO Fix location finding
    // Get location and set default pin
    if (ActivityCompat.checkSelfPermission(this,
      Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
      && ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      // ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      // public void onRequestPermissionsResult(int requestCode, String[] permissions,
      // int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if (mLastLocation != null) {
      mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
          .title("You are " + "here!"));
    }
    // final LatLng office = new LatLng(54.266077, -8.453736);
    // mMap.addMarker(new MarkerOptions().position(office).title("You are here!"));
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
        Log.v("Volley", "Incidents " + incidents.size());
        // Add a marker for users location
        for (final Incident incident : incidents) {
          determineIcon(incident.getIncidentTypeID());
          mMap.addMarker(new MarkerOptions().anchor(0, 1).title(incident.getTitle())
              .snippet("Updated: " + getDateFormatString(incident.getUpdatedAt()))
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
        final SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final SimpleDateFormat outputFormat = new SimpleDateFormat("E, MMM dd yyyy '@' HH:mm:ss");
        Date date = new Date();
        try {
          date = incomingFormat.parse(updatedDate);
          return outputFormat.format(date);
        }
        catch (final Exception e) {
          Log.v(LOG_TAG, "Exception while trying to parse updated Date: %s" + e);
          return updatedDate;
        }
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

  @Override
  public void onConnected(final Bundle bundle) {

  }

  @Override
  public void onConnectionSuspended(final int i) {

  }

  @Override
  public void onConnectionFailed(final ConnectionResult connectionResult) {

  }

  private void selectItem(final int position) {
    // TODO perform navigation
  }

  public class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
      selectItem(position);
    }
  }

}
