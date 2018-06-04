package com.example.pc.trader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainRoomActivity extends AppCompatActivity implements LocationListener {

    //private TextView tvEmail;

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    LocationManager locationManager = null;
    LocationProvider provider = null;
    TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_room);
        //tvEmail = findViewById(R.id.tvEmailProfile);
        //tvEmail.setText(getIntent().getExtras().getString("Email"));
        dl = (DrawerLayout)findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this,dl,R.string.Aberto,R.string.Fechado);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.myprofile){
                    Toast.makeText(MainRoomActivity.this,"Meu perfil",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.settings){
                    Toast.makeText(MainRoomActivity.this,"Configurações",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.editprofile){
                    Toast.makeText(MainRoomActivity.this,"Editar perfil",Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        text2 = (TextView) findViewById(R.id.textView2);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }
    //Codigo que executa a consulta da localização após pedirmos permissão pela primeira vez
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                    provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setCostAllowed(false);

                    String providerName = locationManager.getBestProvider(criteria, true);

                    locationManager.requestLocationUpdates(providerName,1000,0,this);
                    Location mylocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    text2.setText(String.valueOf(mylocation.getLongitude()));

                    // If no suitable provider is found, null is returned.
                    if (providerName != null) {

                    }


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // This verification should be done during onStart() because the system calls
        // this method when the user returns to the activity, which ensures the desired
        // location provider is enabled each time the activity resumes from the stopped state.
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            enableLocationSettings();
        }
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        @SuppressLint("MissingPermission") Location mylocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d("CHANGED", "LOCATION UPDATED" + String.valueOf(mylocation.getLongitude()));
        text2.setText(String.valueOf(mylocation.getLongitude()));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return
               abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
