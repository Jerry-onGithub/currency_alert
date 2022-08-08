package com.app.currencyalert.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("btc/metrics")
    Call<String> getData();
    
    //can add more here

}

