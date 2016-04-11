
package swindroid.suntime.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import swindroid.suntime.R;

/**
 * Displays the current location, including suburb -- does NOT work in emulator
 */
public class LocationFragment extends FragmentActivity implements LocationListener, ActionBar.OnNavigationListener {
    GoogleMap googleMap;
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private ShareActionProvider myShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_fragment);


        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
        }

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private Context getActionBarThemedContextCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getActionBar().getThemedContext();
        } else {
            return this;
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
                .getSelectedNavigationIndex());
    }

    @Override
    public void onLocationChanged(Location location) {

        TextView tvLocation = (TextView) findViewById(R.id.tv_location);

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // Setting latitude and longitude in the TextView tv_location
        tvLocation.setText("Latitude:" + latitude + ", Longitude:" + longitude);

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
           /* MenuItem item = menu.findItem(R.id.menu_item_share);
            myShareActionProvider = (ShareActionProvider) item.getActionProvider();
            myShareActionProvider
                    .setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
            myShareActionProvider.setShareIntent(createShareIntent());
            return true;*/
        // Set up the dropdown list navigation in the action bar.
        return true;

    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "City Name:Perth,Sunrise Time:07:06,Sunset Time:17:21,05/29/2013");
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewLocation:
                // app icon in action bar clicked; go home
                Log.i("Menu", "Reached here");
                Intent intent = new Intent(this, LocationFragment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.viewDaterange:
                // app icon in action bar clicked; go home
                Intent intent2 = new Intent(this, DateRange.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {

        Fragment fragment;
        if (position == 0) {
            fragment = new SunTimeFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
        } else {
            fragment = new SunTimeFragment();
        }

        // section to call the
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, fragment).commit();
        return true;
    }

}
