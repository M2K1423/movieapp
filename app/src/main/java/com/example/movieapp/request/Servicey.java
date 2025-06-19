package com.example.movieapp.request;

import android.util.Log;

import com.example.movieapp.Utils.AppCredentials;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;
import com.example.movieapp.Utils.WatchedMovieApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Servicey {
    private static final String TAG = "Servicey";

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d(TAG, message));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    Response response = chain.proceed(request);

                    if (!response.isSuccessful()) {
                        String responseBody = response.peekBody(Long.MAX_VALUE).string();
                        Log.e(TAG, "API Error - Code: " + response.code() +
                                ", URL: " + request.url() +
                                ", Body: " + responseBody);

                    }

                    return response;
                })
                .build();
    }

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Credentials.BASE_URL)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit watchedRetrofit = new Retrofit.Builder()
            .baseUrl(AppCredentials.BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static Retrofit retrofit = retrofitBuilder.build();

    private static MovieApi movieApi = retrofit.create(MovieApi.class);

    private static WatchedMovieApi watchedMovieApi = watchedRetrofit.create(WatchedMovieApi.class);


    public static WatchedMovieApi getWatchedMovieApi() {
        return watchedMovieApi;
    }
    public static MovieApi getMovieApi() {
        System.out.println("moview api" + movieApi);
        return movieApi;
    }
}