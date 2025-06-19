package com.example.movieapp.Utils;

import com.example.movieapp.response.RegisterResponse;
import com.example.movieapp.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.Map;

public interface UserApi {

    // Đăng ký người dùng
    @POST("/api/users/register")
    @Headers("Content-Type: application/json")
    Call<RegisterResponse> register(@Body Map<String, String> registerData);

    @POST("/api/verify/send-otp")
    @Headers("Content-Type: application/json")
    Call<Map<String, Object>> sendOtp(@Body Map<String, String> body);
    // Đăng nhập người dùng
    @POST("/api/users/login")
    @Headers("Content-Type: application/json")
    Call<LoginResponse> login(@Body Map<String, String> loginData);
}