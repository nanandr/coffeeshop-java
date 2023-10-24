package com.nandanarafiardika.mycoffeeshopdrinklist.rest;

import com.nandanarafiardika.mycoffeeshopdrinklist.model.CoffeeResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("restapi.php")
    Call<CoffeeResponse> getCoffee(@Query("function") String function);

    @GET("restapi.php")
    Call<CoffeeResponse> searchCoffee(@Query("function") String function,
                                      @Query("keyword") String keyword);

    @Multipart
    @POST("restapi.php")
    Call<CoffeeResponse> postCoffee(@Query("function") String function,
                                    @Part("name") RequestBody name,
                                    @Part("price") RequestBody price,
                                    @Part("detail") RequestBody detail,
                                    @Part MultipartBody.Part photoThumbnail,
                                    @Part MultipartBody.Part photoPoster);

    @DELETE("restapi.php")
    Call<CoffeeResponse> deleteCoffee(@Query("function") String function,
                                      @Query("id") String id);
}
