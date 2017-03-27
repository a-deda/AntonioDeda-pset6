package nl.adeda.reisplanner;

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

import org.w3c.dom.Text;

/**
 * Created by Antonio on 23-3-2017.
 */

public class Reisadvies extends Fragment {

    private DatabaseReference mDatabase;
    ReisData reisData;
    FloatingActionButton fab;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Reisadvies");

        // Get bundle containing 'reisData'
        Bundle bundle = this.getArguments();
        reisData = (ReisData) bundle.getSerializable("data");

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        // No changes indicates a lack of trip information
        if (reisData.getChanges() == null) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.content_reisadvies, container, false);
    }
}