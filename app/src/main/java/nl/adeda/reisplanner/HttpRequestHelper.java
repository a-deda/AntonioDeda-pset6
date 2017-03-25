package nl.adeda.reisplanner;

import android.net.Credentials;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

public class HttpRequestHelper extends AppCompatActivity {

    protected static synchronized String downloadFromServer(ReisData... params) throws MalformedURLException {
        String result = "";
        String departureTag = params[0].getDeparture();
        String arrivalTag = params[0].getArrival();

        String urlString = "http://webservices.ns.nl/ns-api-treinplanner?" +
                "fromStation=" + departureTag + "&toStation=" + arrivalTag +
                "&previousAdvices=0&nextAdvices=0";

        URL url = new URL(urlString);

        HttpURLConnection connect;

        if (url != null) {
            try {
                connect = (HttpURLConnection) url.openConnection();

                // Authorize HTTP connection
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication
                                ("a.deda00@gmail.com", "lsxr380QkrqlWgs2ZlS61HLMwzYe1XgHts1y-ych6ux32Od3u2XjzA".toCharArray());
                    }
                });

                connect.setRequestMethod("GET");

                Integer responseCode = connect.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    return result;
    }
}
