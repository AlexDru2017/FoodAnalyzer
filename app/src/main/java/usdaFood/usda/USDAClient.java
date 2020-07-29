package usdaFood.usda;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import usdaFood.network.MozillaNetworkClient;


public class USDAClient {
    private String tokenApi;
    //private NetworkOperations networkOperations;

    public USDAClient(String tokenApi/*, NetworkOperations networkOperations*/) {
        this.tokenApi = tokenApi;
        //this.networkOperations = networkOperations;
    }

    /**
     * <p>A search request sends keyword queries and returns lists of foods which
     * contain one or more of the keywords in the food description, scientific name,
     * or commerical name fields.</p>
     * <p>param keyword  Search terms.</p>
     * <p>param pageSize maximum rows to return.</p>
     *
     * @param args Represents all parameters
     * @return String with query results in json format
     * @throws IOException
     */
    @SuppressLint("LongLogTag")
    public JSONObject searchFood(String... args) throws IOException, JSONException, ExecutionException, InterruptedException {
        String keyword = "";
        String max = "5";
        if (args != null) {
            if (args.length == 1) {
                keyword = args[0];
            }
            String requestString = createRequestString(new String[]{USDAUrl.searchUrl, "query=", keyword,
                    "&pageSize=", max, "&api_key=", tokenApi});
            AsyncTask<String, String, Object> answer = new MozillaNetworkClient().execute(requestString, "Food");
            Log.d("USDA SearchFood query = ", requestString);
            JSONObject jsonObject = (JSONObject) answer.get();
            return jsonObject;
        }
        return null;
    }

    /**
     * <p>Food Report is a list of nutrients and their values in various portions for a specific food</p>
     *
     * <p> ndbno NDB no.</p>
     * <p>type  Report type: [b]asic or [f]ull or [s]tats.</p>
     *
     * @param args Represents all parameters
     * @return
     * @throws IOException
     */
    @SuppressLint("LongLogTag")
    public JSONArray searchFoodReport(String... args) throws IOException, JSONException {

        String ndbno = "0";

        if (args != null) {
            if (args.length == 1) {
                ndbno = args[0];
            }
            //https://api.nal.usda.gov/fdc/v1/foods?fdcIds=522053&api_key=uf0OYD9cvS9Ww5ZXdpppajGYtBfQg9cBd72LVRJP
            String requestString = createRequestString(
                    new String[]{USDAUrl.foodReportUrl, "&fdcIds=", ndbno, "&api_key=", tokenApi});
            //Log.d("USDA searchFoodReport query = ", requestString);
            AsyncTask<String, String, Object> answer = new MozillaNetworkClient().execute(requestString, "Report");
            try {
                Log.d("USDA searchFoodReport answer", answer.get().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(answer.get().toString());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return jsonArray;
            // return networkOperations.sendGet(requestString);
        }
        return null;
    }

    private String createRequestString(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String s : strings) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }
}
