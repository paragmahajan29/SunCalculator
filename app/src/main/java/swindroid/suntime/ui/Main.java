package swindroid.suntime.ui;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ShareActionProvider;

import swindroid.suntime.R;

public class Main extends FragmentActivity implements
        ActionBar.OnNavigationListener,ListFragment.Callbacks {

    /** The Bundle key to hold current dropdown position. */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private ShareActionProvider myShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (savedInstanceState == null)
        {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = getIntent().getExtras();
            // ArrayList<String> listDetails = arguments.getStringArrayList("detailList");
            SunTimeFragment fragment = new SunTimeFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(getActionBarThemedContextCompat(),
                        android.R.layout.simple_list_item_1, // android built-in
                        // layout
                        android.R.id.text1, // android built-in id used by
                        // layout
                        new String[] { getString(R.string.suntime),
                                getString(R.string.title_activity_date_range) }), this);
    }

    /**
     * Backward-compatible version of {@link ActionBar#getThemedContext()} that
     * simply returns the {@link android.app.Activity} if
     * <code>getThemedContext</code> is unavailable.
     */
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
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
                .getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        myShareActionProvider = (ShareActionProvider) item.getActionProvider();
        myShareActionProvider
                .setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        myShareActionProvider.setShareIntent(createShareIntent());
        return true;
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

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "City Name:Perth,Sunrise Time:07:06,Sunset Time:17:21,05/29/2013");
        return shareIntent;
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, replace container with new
        // fragment
        Fragment fragment;
        if (position == 0) {
            fragment = new SunTimeFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
        } else {
            fragment = new ListFragment();
        }

        // section to call the
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, fragment).commit();
        return true;
    }

    @Override
    public void onItemSelected(String id) {

    }
}
