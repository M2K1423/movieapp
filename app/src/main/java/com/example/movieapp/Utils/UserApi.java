package com.example.movieapp.Utils;

import com.example.movieapp.response.RegisterResponse;
import com.example.movieapp.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import java.util.Map;

public interface UserApi {

    @POST("/api/users/register")
    @Headers("Content-Type: application/json")
    Call<RegisterResponse> register(@Body Map<String, String> registerData);

    @POST("/api/users/login")
    @Headers("Content-Type: application/json")
    Call<LoginResponse> login(@Body Map<String, String> loginData);
}
