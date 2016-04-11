package swindroid.suntime.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import swindroid.suntime.R;
import swindroid.suntime.calc.AstronomicalCalendar;
import swindroid.suntime.calc.FileReader;
import swindroid.suntime.calc.GeoLocation;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private int year, month, day;
    private ArrayList<Location> locations;
    private Location currLoc;
    private FileReader fileReader;
    private DatePicker dpFrom;
    private DatePicker dpTo;
    private Spinner spinner;
    private TableLayout times;
    // TODO: Rename and change types of parameters
    private Callbacks mCallbacks = sDummyCallbacks;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private OnFragmentInteractionListener mListener;

    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.date_range, container, false);
        dpFrom = (DatePicker) view.findViewById(R.id.startDatePicker);
        dpTo = (DatePicker) view.findViewById(R.id.endDatePicker);
        spinner = (Spinner) view.findViewById(R.id.locationTV);
        times = (TableLayout) view.findViewById(R.id.times);
        Button button = (Button) view.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        fileReader = new FileReader(view.getContext(), -1);
        setSpinner();
        initializeUI();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    private void initializeUI() {
        currLoc = new Location("Whyalla", -33.03333, 137.58333, "Australia/Adelaide");
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        dpFrom.init(year, month, day, dateFromChangeHandler);
        dpTo.init(year, month, day, dateToChangeHandler);// setup initial values and reg. handler
        updateFromTime(year, month, day);
        updateToTime(year, month, day);
        setSpinner();

    }

    private void setSpinner() {

        locations = new ArrayList<Location>();
        locations = fileReader.getLocations();
        String[] str = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            str[i] = locations.get(i).getName();
        }
        // Spinner spinner = (Spinner)this.getView().findViewById(R.id.locSpinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, str);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String selectedval = parent.getItemAtPosition(pos).toString();
                Location loc = locations.get(pos);
                updatePlace(loc);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }
        });

    }

    private void updateFromTime(int year, int monthOfYear, int dayOfMonth) {
        TimeZone tz = TimeZone.getDefault();
        tz.setDefault(TimeZone.getTimeZone(currLoc.getLocation()));
        GeoLocation geolocation = new GeoLocation(currLoc.getName(), currLoc.getLatitude(), currLoc.getLongitude(), tz);
        AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
        ac.getCalendar().set(year, monthOfYear, dayOfMonth);
        Date srise = ac.getSunrise();
        Date sset = ac.getSunset();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    }

    private void updateToTime(int year, int monthOfYear, int dayOfMonth) {
        TimeZone tz = TimeZone.getDefault();
        tz.setDefault(TimeZone.getTimeZone(currLoc.getLocation()));
        GeoLocation geolocation = new GeoLocation(currLoc.getName(), currLoc.getLatitude(), currLoc.getLongitude(), tz);
        AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
        ac.getCalendar().set(year, monthOfYear, dayOfMonth);
        Date srise = ac.getSunrise();
        Date sset = ac.getSunset();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    }

    public void updatePlace(Location location) {
        TimeZone tz = TimeZone.getDefault();
        //tz.getTimeZone(location.getTimeZone());
        //TimeZone tz = TimeZone.getDefault();
        tz.setDefault(TimeZone.getTimeZone(location.getLocation()));
        GeoLocation geolocation = new GeoLocation(location.getName(), location.getLatitude(), location.getLongitude(), tz);
        AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
        ac.getCalendar().set(year, day, month);

        Date srise = ac.getSunrise();
        Date sset = ac.getSunset();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String dateString = sdf.format(date1);

    }

    DatePicker.OnDateChangedListener dateFromChangeHandler = new DatePicker.OnDateChangedListener() {
        public void onDateChanged(DatePicker dp, int year, int monthOfYear, int dayOfMonth) {
            updateFromTime(year, monthOfYear, dayOfMonth);
        }
    };

    DatePicker.OnDateChangedListener dateToChangeHandler = new DatePicker.OnDateChangedListener() {
        public void onDateChanged(DatePicker dp, int year, int monthOfYear, int dayOfMonth) {
            updateToTime(year, monthOfYear, dayOfMonth);
        }
    };


    private static List<Date> getDates(Date start, Date end) throws ParseException {
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);

        while (calendar.getTime().before(end)) {
            Date resultado = calendar.getTime();
            dates.add(resultado);
            calendar.add(Calendar.DATE, 1);
        }

        return dates;


    }

    public void getList() throws ParseException {
        //DatePicker dpFrom = (DatePicker) findViewById(R.id.fromDatePicker);
        //DatePicker dpTo = (DatePicker) findViewById(R.id.toDatePicker);

        ArrayList<String> listOfTimes = new ArrayList<String>();
        long dateTime = dpFrom.getCalendarView().getDate();
        Date date = new Date(dateTime);
        long dateTime2 = dpTo.getCalendarView().getDate();
        Date date2 = new Date(dateTime2);
        List<Date> dates = getDates(date, date2);
        for (Date dateValue : dates) {
            System.out.println(dateValue);
            TimeZone tz = TimeZone.getDefault();
            tz.setDefault(TimeZone.getTimeZone(currLoc.getLocation()));
            GeoLocation geolocation = new GeoLocation(currLoc.getName(), currLoc.getLatitude(), currLoc.getLongitude(), tz);
            AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
            //year, monthOfYear, dayOfMonth

            Calendar c = Calendar.getInstance();
            c.setTime(dateValue);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            int monthOfYear = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            String times = getTimes(year, monthOfYear, dayOfMonth);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String ToDate = dateFormat.format(dateValue);
            String value = ToDate + "," + times;
            Log.d("SUNRISE Unformatted", value);
            listOfTimes.add(value);


        }


        Bundle databundle = new Bundle();
        databundle.putStringArrayList("detailList", listOfTimes);
        Intent i = new Intent();
        i.setClass(getActivity().getApplicationContext(), DateRange.class);
        i.putExtras(databundle);
        startActivity(i);


    }

    private String getTimes(int year, int monthOfYear, int dayOfMonth) {
        TimeZone tz = TimeZone.getDefault();
        tz.setDefault(TimeZone.getTimeZone(currLoc.getLocation()));
        GeoLocation geolocation = new GeoLocation(currLoc.getName(), currLoc.getLatitude(), currLoc.getLongitude(), tz);
        AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
        ac.getCalendar().set(year, monthOfYear, dayOfMonth);
        Date srise = ac.getSunrise();
        Date sset = ac.getSunset();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String value = sdf.format(srise) + "," + sdf.format(sset);
        return value;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        // public void onFragmentInteraction(Uri uri);
    }

}
