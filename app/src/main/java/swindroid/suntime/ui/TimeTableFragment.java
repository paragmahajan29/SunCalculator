package swindroid.suntime.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import swindroid.suntime.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeTableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TimeTableFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> details ;
    private TableLayout table;

    private OnFragmentInteractionListener mListener;


    /* a getter which gets the city latitude and longitude*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeTableFragment newInstance(String param1, String param2) {
        TimeTableFragment fragment = new TimeTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public TimeTableFragment() {
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

        View view =  inflater.inflate(R.layout.fragment_time_table, container, false);
        details = new ArrayList<String>();
        details = getArguments().getStringArrayList("detailList");
        table = (TableLayout)view.findViewById(R.id.tableList);
        IntialtizeUI();
        return view;
    }
    public void IntialtizeUI()
    {
        table.setStretchAllColumns(true);
        table.bringToFront();
        TableRow th =  new TableRow(getActivity().getApplicationContext());
        TextView h1 = new TextView(getActivity().getApplicationContext());
        h1.setText("Date");
        TextView h2 = new TextView(getActivity().getApplicationContext());
        h2.setText("Sun Rise");
        TextView h3 = new TextView(getActivity().getApplicationContext());
        h3.setText("Sun Set");
        th.addView(h1);
        th.addView(h2);
        th.addView(h3);
        table.addView(th);
        for(int i = 0; i < details.size(); i++){
            String[] value= details.get(i).split(",");
            TableRow tr =  new TableRow(getActivity().getApplicationContext());
            TextView c1 = new TextView(getActivity().getApplicationContext());
            c1.setText(value[0]);
            TextView c2 = new TextView(getActivity().getApplicationContext());
            c2.setText(value[1]);
            TextView c3 = new TextView(getActivity().getApplicationContext());
            c3.setText(value[2]);
            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            table.addView(tr);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
          //  mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
          //  mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        // public void onFragmentInteraction(Uri uri);
    }


}
