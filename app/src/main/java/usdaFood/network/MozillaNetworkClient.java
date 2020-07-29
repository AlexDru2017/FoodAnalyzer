package usdaFood.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MozillaNetworkClient extends AsyncTask<String, String, Object> implements NetworkOperations {
    public JSONObject sendGet(String url) throws IOException, JSONException {

        URL urlObj = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/50.0");
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }

            input.close();
            JSONObject answer = new JSONObject(response.toString());
            return answer;
        }
        return null;
    }

    public JSONArray sendGetReport(String url) throws IOException, JSONException {

        URL urlObj = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/50.0");
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }

            input.close();

            return new JSONArray(response.toString());
        }
        return null;
    }

    @Override
    protected Object doInBackground(String... strings) {
        try {
            switch (strings[1]) {
                case ("Food"):
                    return sendGet(strings[0]);
                case ("Report"):
                    return sendGetReport(strings[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
