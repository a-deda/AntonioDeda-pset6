package nl.adeda.reisplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Antonio on 25-3-2017.
 */

public class MyTripsAdapter extends ArrayAdapter<ReisData> {

    public MyTripsAdapter(Context context, ArrayList<ReisData> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get data for position
        ReisData reisData = getItem(position);

        // Inflate view if not already used
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_my_trips, parent, false);
        }

        // Get TextViews
        TextView fromStation = (TextView) convertView.findViewById(R.id.vertrekStation);
        TextView toStation = (TextView) convertView.findViewById(R.id.aankomstStation);

        // Set text to TextViews
        fromStation.setText(reisData.getDeparture());
        toStation.setText(reisData.getArrival());


        return convertView;
    }

}
