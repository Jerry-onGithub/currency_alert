package com.app.currencyalert.jsonResponse;

import android.util.Log;

import com.app.currencyalert.Btc;
import com.app.currencyalert.data.ApiClient;
import com.app.currencyalert.data.ApiInterface;
import com.app.currencyalert.interfaces.CustomCallback;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class GetData {

    private ApiInterface apiService;

    public void getResponse(CustomCallback customCallback){
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.getData();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        try {
                            JSONObject obj = new JSONObject(jsonResponse);
                            if (!obj.optString("status").isEmpty()) {
                                //String val = obj.getString("status");
                                JSONObject timestamp = obj.getJSONObject("status");
                                String time = timestamp.getString("timestamp");
                                //System.out.println("timestamp >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+time);

                                JSONObject data = obj.getJSONObject("data").getJSONObject("market_data");
                                String currency = data.getString("price_usd");
                                //System.out.println("currency >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+currency);

                                Btc btc = new Btc(time, currency);
                                customCallback.onSuccess(btc);
                            } else {
                                Log.i("RESULT", "EMPTY");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }else {
                        Log.i("RESULT", "EMPTY");
                    }
                } else{
                    System.out.println("EMPTY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
