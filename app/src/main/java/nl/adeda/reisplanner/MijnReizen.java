package nl.adeda.reisplanner;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Antonio on 21-3-2017.
 */

public class MijnReizen extends Fragment {

    private DatabaseReference mDatabase;
    ArrayList<ReisData> reisDataList;
    ReisData data;

    ProgressBar progressBar;
    ListView listView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Mijn reizen");

        progressBar = (ProgressBar) view.findViewById(R.id.myTripsProgressBar);
        listView = (ListView) view.findViewById(R.id.mijnReizenList);

        // Reference to Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getListFromFirebase();

    }

    private void getListFromFirebase() {
        // TODO: Get depature & arrival from Firebase
        ValueEventListener dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Make progressBar visible
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);

                // Make list for ReisData objects
                reisDataList = new ArrayList<ReisData>();

                // Put objects from database into java object list
                for (DataSnapshot ds : dataSnapshot.child("allData").getChildren()) {
                    data = ds.getValue(ReisData.class);
                    reisDataList.add(data);
                }
                // TODO: Execute displayList() after ValueEventListener has finished
                displayList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting data failed, log a message
                Log.w("Error:", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(dataListener);
    }

    private void displayList() {
        // Make progressBar invisible & ListView visible
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);

        // TODO: Display departure & arrival in ListView [DONE]
        // Set ArrayAdapter on ArrayList<Directions>
        MyTripsAdapter myTripsAdapter = new MyTripsAdapter(getContext(), reisDataList);

        listView.setAdapter(myTripsAdapter);

        // TODO: When ListView item is clicked, launch AsyncTask
            /* Start AsyncTask
            Reisplanner reisPlanner = new Reisplanner();
            GetInfo asyncTask = new GetInfo(reisPlanner, data);
            asyncTask.execute(data);*/
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.content_mijnreizen, container, false);
    }
}
