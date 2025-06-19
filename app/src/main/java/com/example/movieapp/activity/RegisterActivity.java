package com.example.movieapp.activity;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.example.movieapp.Utils.AppCredentials;
import com.example.movieapp.Utils.UserApi;
import com.example.movieapp.response.RegisterResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword , etEmail, etOtp;
    private Button btnSendOtp;
    private Button btnRegister;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_register_username);
        etPassword = findViewById(R.id.et_register_password);
        etEmail = findViewById(R.id.et_register_email);
        etOtp = findViewById(R.id.et_register_otp);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginLink = findViewById(R.id.tv_login_link);

        // Đăng ký
        btnRegister.setOnClickListener(v -> performRegister());
        btnSendOtp.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                requestOtp(email);
            } else {
                etEmail.setError("Vui lòng nhập email");
            }
        });
        // Chuyển sang LoginActivity
        tvLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
    private void performRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String otp = etOtp.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email không được để trống");
            return;
        }
        if (otp.isEmpty()) {
            etOtp.setError("Vui lòng nhập mã OTP");
            return;
        }
        if (username.isEmpty()) {
            etUsername.setError("Tên đăng nhập không được để trống");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Mật khẩu không được để trống");
            return;
        }

        // Gửi request lên API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCredentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi userApi = retrofit.create(UserApi.class);

        Map<String, String> registerData = new HashMap<>();
        registerData.put("username", username);
        registerData.put("password", password);
        registerData.put("email", email);
        registerData.put("otp", otp);

        Call<RegisterResponse> call = userApi.register(registerData);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse res = response.body();
                    if (res != null && res.isSuccess()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        String error = res != null ? res.getError() : "Đăng ký thất bại";
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();

                        // Gợi ý hiển thị lỗi tại field cụ thể
                        if (error.toLowerCase().contains("email")) {
                            etEmail.setError(error);
                        } else if (error.toLowerCase().contains("tên đăng nhập") || error.toLowerCase().contains("username")) {
                            etUsername.setError(error);
                        }
                    }
                } else if (response.code() == 409) {
                    // Lỗi trùng email hoặc username từ server
                    Toast.makeText(RegisterActivity.this, "Email hoặc tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();

                    // Gán lỗi giả định vào cả 2 trường để user kiểm tra lại
                    etUsername.setError("Tên đăng nhập có thể đã tồn tại");
                    etEmail.setError("Email có thể đã được sử dụng");
                } else {
                    Toast.makeText(RegisterActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Kết nối thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("OTP_REQUEST_ERROR", "Gửi OTP thất bại", t);
            }
        });
    }
    private void requestOtp(String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCredentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi userApi = retrofit.create(UserApi.class);

        Map<String, String> body = new HashMap<>();
        body.put("email", email); // gửi đúng key như BE đang xử lý

        Call<Map<String, Object>> call = userApi.sendOtp(body);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Mã OTP đã được gửi đến email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Gửi OTP thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}