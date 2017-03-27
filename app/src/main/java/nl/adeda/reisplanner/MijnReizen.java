package nl.adeda.reisplanner;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * Created by Antonio on 21-3-2017.
 */

public class MijnReizen extends Fragment {

    private DatabaseReference mDatabase;
    ArrayList<ReisData> reisDataList;
    ReisData data;
    MyTripsAdapter myTripsAdapter;
    Context context;

    ValueEventListener dataListener;

    ProgressBar progressBar;
    ListView listView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Mijn reizen");
        context = getContext();

        progressBar = (ProgressBar) view.findViewById(R.id.myTripsProgressBar);

        // Make list for ReisData objects
        reisDataList = new ArrayList<ReisData>();

        // Set adapter on ListView
        listView = (ListView) view.findViewById(R.id.mijnReizenList);
        myTripsAdapter = new MyTripsAdapter(getContext(), reisDataList);
        listView.setAdapter(myTripsAdapter);

        // Reference to Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getListFromFirebase();

        mDatabase.removeEventListener(dataListener);

        // onClick listeners
        setClickListeners();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.content_mijnreizen, container, false);
    }

    private void getListFromFirebase() {
        // Get depatures & arrivals from Firebase
        dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Make progressBar visible
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);

                // Put objects from database into java object list
                for (DataSnapshot ds : dataSnapshot.child("allData").getChildren()) {
                    data = ds.getValue(ReisData.class);
                    reisDataList.add(data);
                }

                // Make progressBar invisible
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                // Notify adapter of changes
                myTripsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting data failed, log a message
                Log.w("Error:", databaseError.toException());
            }
        };
        mDatabase.addListenerForSingleValueEvent(dataListener);
    }

    private void setClickListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Click opens 'Reisadvies'
                ReisData clickedItem = (ReisData) listView.getItemAtPosition(position);
                GetInfo asyncTask = new GetInfo(MijnReizen.this, clickedItem, 1);
                asyncTask.execute(clickedItem);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                ValueEventListener deleteListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Delete entry from Firebase
                        mDatabase.child("allData").child(reisDataList.get(position).getKey()).removeValue();

                        // Delete entry from ListView
                        Toast.makeText(context, "Reis verwijderd.", Toast.LENGTH_SHORT).show();
                        reisDataList.remove(position);
                        myTripsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting data failed, log a message
                        Log.w("Error:", databaseError.toException());
                    }
                };
                mDatabase.addListenerForSingleValueEvent(deleteListener);

                return true;
            }

        });
    }

    public void startFragment(Fragment nextFragment) {
        // Go to Reisadvies fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
