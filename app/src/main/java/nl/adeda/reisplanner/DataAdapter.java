package nl.adeda.reisplanner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Antonio on 25-3-2017.
 */

public class DataAdapter extends ArrayAdapter<ReisDirections> {

    public DataAdapter(Context context, ArrayList<ReisDirections> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get data for position
        ReisDirections reisDirections = getItem(position);

        // Inflate view if not already used
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get TextViews
        TextView tijd = (TextView) convertView.findViewById(R.id.vertrektijd);
        TextView station = (TextView) convertView.findViewById(R.id.station);
        TextView spoor = (TextView) convertView.findViewById(R.id.spoor);


        tijd.setText(reisDirections.getTime());
        station.setText(reisDirections.getStation());
        spoor.setText(reisDirections.getPlatform());

        return convertView;
    }

}
