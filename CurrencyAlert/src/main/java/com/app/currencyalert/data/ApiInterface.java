package com.app.currencyalert.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("btc/metrics")
    Call<String> getData();

/*    @GET("getUniversity")
    Call<String> getUniversityDetail(@Query("name") String name);

    @GET("getProgramsListByField")
    Call<String> getProgramsAndFieldsList();*/

}

