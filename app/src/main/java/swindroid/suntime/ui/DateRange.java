package swindroid.suntime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import swindroid.suntime.R;

public class DateRange extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        //getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
        {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = getIntent().getExtras();
            // ArrayList<String> listDetails = arguments.getStringArrayList("detailList");
            TimeTableFragment fragment = new TimeTableFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.detail_container, fragment).commit();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpTo(this, new Intent(this, Main.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
