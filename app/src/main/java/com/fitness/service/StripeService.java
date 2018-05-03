
package com.fitness.service;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * A Retrofit service used to communicate with a server.
 */
public interface StripeService {

    @GET("/hello")
    Call<ResponseBody> hello();

    @POST("/charge")
    Call<ResponseBody> charge(@Body Map<String, Object> params);

    @POST("/refund")
    Call<ResponseBody> refund(@Body Map<String, Object> params);
}
