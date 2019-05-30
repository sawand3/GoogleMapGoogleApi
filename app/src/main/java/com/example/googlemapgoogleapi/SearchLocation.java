package com.example.googlemapgoogleapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class SearchLocation extends FragmentActivity implements OnMapReadyCallback {
    ImageButton search;
    EditText editText;
    GoogleMap mgoogleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    LocationManager locationManager;
    private GoogleApiClient client;
    private String latitude = "", longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        search = findViewById(R.id.searchLocation);
        editText = findViewById(R.id.editTextSearchLocation);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(SearchLocation.this);
        GetLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mgoogleMap = googleMap;
        if (!latitude.equalsIgnoreCase("")){
            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
        }
        enableMyLocationIfPermitted();
    }

    //auto set location when update
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mgoogleMap != null) {
            mgoogleMap.setMyLocationEnabled(true);
        }
    }

    //get current location
    private void GetLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } else if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 500, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                    latitude = String.valueOf(location.getLatitude());
//                    longitude = String.valueOf(location.getLongitude());

                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
}
