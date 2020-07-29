package usdaFood.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.internal.zao;

import org.json.JSONException;

import java.io.IOException;

import usdaFood.usda.USDAClient;
import usdaFood.usda.USDAClientBuilder;
import usdaFood.network.MozillaNetworkClient;


public class USDApp extends AppCompatActivity {
    private USDAClient usdaClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public USDAClient getUSDAClient() {
        usdaClient = new USDAClientBuilder()
                .addTokenAPI("uf0OYD9cvS9Ww5ZXdpppajGYtBfQg9cBd72LVRJP").build();
              //  .addNetworkOperations(new MozillaNetworkClient()).build();
        return usdaClient;
    }

    //	System.out.println( usdaClient.searchFoodReport("Milk"));
    //usdaClient.searchFood("Apple");
}
