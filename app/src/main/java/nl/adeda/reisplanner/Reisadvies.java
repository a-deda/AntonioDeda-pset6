package nl.adeda.reisplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.w3c.dom.Text;

/**
 * Created by Antonio on 23-3-2017.
 */

public class Reisadvies extends Fragment {

    private DatabaseReference mDatabase;
    ReisData reisData;
    FloatingActionButton fab;
    SharedPreferences savedFragmentState;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Reisadvies");

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        /*savedFragmentState = this.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        // Restore view
        if (savedInstanceState != null && savedFragmentState == null) {
            reisData = (ReisData) savedInstanceState.getSerializable("data");
            fab.setEnabled(savedInstanceState.getBoolean("fabEnabled"));
            if (!fab.isEnabled()) {
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().
                        getColor(android.R.color.darker_gray)));
            }
        } else if (savedFragmentState != null) {
            Gson gson = new Gson();
            String json = savedFragmentState.getString("reisDataObj", "");
            reisData = gson.fromJson(json, ReisData.class);
            savedFragmentState = null;
        }
        else {
            */// Get bundle containing 'reisData'
            Bundle bundle = this.getArguments();
            reisData = (ReisData) bundle.getSerializable("data");
        //}

        // No changes indicates a lack of trip information, hide appropriate views
        if (reisData.getChanges() == null) {
            hideViews(view);
            return;
        }

        // Initialize TextViews
        TextView aantalOverstappen = (TextView) view.findViewById(R.id.overstappen);
        final TextView reistijd = (TextView) view.findViewById(R.id.reistijd);

        // Set text
        aantalOverstappen.setText(reisData.getChanges());
        reistijd.setText(reisData.getTravelTime());

        // Set ArrayAdapter on ArrayList<Directions>
        DataAdapter dataAdapter = new DataAdapter(getContext(), reisData.getDirections());
        ListView listView = (ListView) view.findViewById(R.id.reisList);
        listView.setAdapter(dataAdapter);

        setFabOnClickListener();

    }

    private void setFabOnClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Add 'Vertrek' & 'Aankomst' from 'ReisData' to Firebase.
                // Put new object in database
                mDatabase = FirebaseDatabase.getInstance().getReference();
                reisData.setKey(mDatabase.child("allData").push().getKey());
                mDatabase.child("allData").child(reisData.getKey()).setValue(reisData);

                fab.setEnabled(false);
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));

            }
        });
    }

    private void hideViews(View view) {
        TextView geenOpties = (TextView) view.findViewById(R.id.noOptionsText);
        TextView reistijdText = (TextView) view.findViewById(R.id.reistijdText);
        TextView overstappenText = (TextView) view.findViewById(R.id.overstappenText);
        TextView tijdText = (TextView) view.findViewById(R.id.tijdText);
        TextView stationText = (TextView) view.findViewById(R.id.stationText);
        TextView spoorText = (TextView) view.findViewById(R.id.spoorText);

        reistijdText.setVisibility(View.GONE);
        overstappenText.setVisibility(View.GONE);
        tijdText.setVisibility(View.GONE);
        stationText.setVisibility(View.GONE);
        spoorText.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        geenOpties.setVisibility(View.VISIBLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.content_reisadvies, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("fabEnabled", fab.isEnabled());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        savedFragmentState = this.getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedFragmentState.edit();

        // Convert object to JSON
        Gson gson = new Gson();
        String json = gson.toJson(reisData);
        editor.putString("reisDataObj", json);
        editor.commit();

        super.onDestroy();
    }
}