package swindroid.suntime.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import swindroid.suntime.R;
import swindroid.suntime.calc.AstronomicalCalendar;
import swindroid.suntime.calc.GeoLocation;

public class SunTimeFragment extends Fragment implements OnItemSelectedListener {

    //interfaca
	View rootView;
	String name = "";
	double latitude = 0;
	double longitude = 0;
	String timezone = "";
	Map<String, Location> locationList;
	

	public SunTimeFragment() {
	} // default empty constructor

	/** Called as part of the Fragment lifecycle -- create process */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// remember rootView as we will need it later to get components
		rootView = inflater.inflate(R.layout.suntime_fragment, container, false);

		InputStream inputStream = getResources().openRawResource(
				R.raw.au_locations);
		locationList = getWords(inputStream);
		String[] cities = getCityNames();// get the all city names
		Spinner spinner = (Spinner) rootView.findViewById(R.id.locationTV);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				inflater.getContext(), android.R.layout.simple_spinner_item,
				cities);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
        initializeUI();
		return rootView;
	}

	private void initializeUI() {
		DatePicker dp = (DatePicker) rootView.findViewById(R.id.datePicker);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		dp.init(year, month, day, dateChangeHandler); // setup initial values
														// and reg. handler
		updateTime(year, month, day);

	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
	
		String selected= parent.getItemAtPosition(pos).toString();
		Log.i("Spinner",""+selected);	
		
		Location l = locationList.get(selected);
		name = l.getName();
		latitude = l.getLatitude();
		longitude = l.getLongitude();
		timezone = l.getLocation();
		Log.i("Received", "" + name);
		Log.i("Received", "" + latitude);
		Log.i("Received", "" + longitude);
		Log.i("Received", "" + timezone);
		initializeUI();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	private void updateTime(int year, int monthOfYear, int dayOfMonth) {
		TimeZone tz = TimeZone.getTimeZone(timezone);
		GeoLocation geolocation = new GeoLocation(name, latitude, longitude,
				tz);
		AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
		ac.getCalendar().set(year, monthOfYear, dayOfMonth);
		Date srise = ac.getSunrise();
		Date sset = ac.getSunset();

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		TextView sunriseTV = (TextView) rootView
				.findViewById(R.id.sunriseTimeTV);
		TextView sunsetTV = (TextView) rootView.findViewById(R.id.sunsetTimeTV);
		Log.d("SUNRISE Unformatted", srise + "");

		sunriseTV.setText(sdf.format(srise));
		sunsetTV.setText(sdf.format(sset));
	}

	OnDateChangedListener dateChangeHandler = new OnDateChangedListener() {
		public void onDateChanged(DatePicker dp, int year, int monthOfYear,
				int dayOfMonth) {
			updateTime(year, monthOfYear, dayOfMonth);
		}
	};



	

	private String[] getCityNames() {
		String[] cities = new String[locationList.size()];
		cities = locationList.keySet().toArray(cities);
		return cities;
	}

	/** Return words over 2 characters long */
	private Map<String, Location> getWords(InputStream res) {
		Map<String, Location> locList = new HashMap<String, Location>();
		BufferedReader br = new BufferedReader(new InputStreamReader(res));
		String name;
		double latitude;
		double longitude;
		String location;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] wordsOnLine = line.split("\n");
				for (String w : wordsOnLine) {
					StringTokenizer st = new StringTokenizer(w, ",");
					name = st.nextToken();
					latitude = Double.parseDouble(st.nextToken());
					longitude = Double.parseDouble(st.nextToken());
					location = st.nextToken();
					Location loc = new Location(name, latitude, longitude,
							location);
					locList.put(name, loc);
				}
			}
		} catch (IOException iox) {
			locList = null; // this is evil
		}
		return locList;
	}
	
	
}