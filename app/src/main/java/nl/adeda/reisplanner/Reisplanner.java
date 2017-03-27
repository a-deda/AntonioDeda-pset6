package nl.adeda.reisplanner;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Antonio on 21-3-2017.
 */

public class Reisplanner extends Fragment implements View.OnClickListener {

    ReisData data;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Reisplanner");

        // OnClickListener for 'Plannen' button
        Button button = (Button) view.findViewById(R.id.planButton);
        button.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.content_reisplanner, container, false);
    }

    @Override
    public void onClick(View v) {
        searchTrip();
    }

    private void searchTrip() {

        // Get views
        EditText departure = (EditText) getView().findViewById(R.id.departureText);
        EditText arrival = (EditText) getView().findViewById(R.id.arrivalText);

        // Make warnings invisible
        TextView departureWarning = (TextView) getView().findViewById(R.id.departureWarning);
        TextView arrivalWarning = (TextView) getView().findViewById(R.id.arrivalWarning);
        departureWarning.setVisibility(View.INVISIBLE);
        arrivalWarning.setVisibility(View.INVISIBLE);

        // Check if empty
        String departureStation = departure.getText().toString();
        String arrivalStation = arrival.getText().toString();

        if (departureStation.equals("") | arrivalStation.equals("")) {
            if (departureStation.equals("")) {
                departureWarning.setVisibility(View.VISIBLE);
            }

            if (arrivalStation.equals("")) {
                arrivalWarning.setVisibility(View.VISIBLE);
            }

            return;
        }

        // Put departure & arrival into model class
        data = new ReisData();
        data.setDeparture(departureStation);
        data.setArrival(arrivalStation);

        // Start AsyncTask
        GetInfo asyncTask = new GetInfo(this, data, 0);
        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        asyncTask.execute(data);
    }

    public void startFragment(Fragment nextFragment) {

        // Go to Reisadvies fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
