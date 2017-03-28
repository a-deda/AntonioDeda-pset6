package nl.adeda.reisplanner;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Antonio on 23-3-2017.
 */

public class GetInfo extends AsyncTask<ReisData, Integer, String> {

    Reisplanner reisPlanner;
    MijnReizen mijnReizen;
    ReisData data;
    int callerId;

    // Constructor for Reisplanner
    public GetInfo(Reisplanner main, ReisData data, int callerId) {
        this.data = data;
        reisPlanner = main;
        this.callerId = callerId;
    }

    // Constructor for MijnReizen
    public GetInfo(MijnReizen main, ReisData data, int callerId) {
        this.data = data;
        mijnReizen = main;
        this.callerId = callerId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(ReisData... params) {
        try {
            // Return XML as string with route options
            return HttpRequestHelper.downloadFromServer(params);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Make Document from String (result)
        DocumentBuilder builder;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        Document doc = null;

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(result)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        startReisAdvies(doc);
    }

    private void startReisAdvies(Document doc) {
        ReisData parsedData;
        Bundle bundle = null;

        // Parse XML
        if (doc != null) {
            parsedData = XMLParser.parse(doc, data);

            bundle = new Bundle();
            bundle.putSerializable("data", parsedData);
        }

        // Start Reisadvies with parsedData
        Fragment fragment = new Reisadvies();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        if (callerId == 0) { // Called by Reisadvies
            reisPlanner.startFragment(fragment);
        } else if (callerId == 1) { // Called by MijnReizen
            mijnReizen.startFragment(fragment);
        }

    }


}
