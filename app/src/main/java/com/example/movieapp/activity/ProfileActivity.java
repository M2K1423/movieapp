package com.example.movieapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private Uri imageUri;
    private Button viewHistoryBtn;
    private Button editProfileBtn;

    private Button logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView = findViewById(R.id.profile_image);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        viewHistoryBtn = findViewById(R.id.view_history_button);
        viewHistoryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this,MovieWatchingActivity.class);
            startActivity(intent);
        });
        editProfileBtn = findViewById(R.id.edit_profile_button);
        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
            startActivity(intent);
        });
        logoutBtn = findViewById(R.id.logout_button);
        logoutBtn.setOnClickListener(v -> {
            // Đăng xuất Firebase
            FirebaseAuth.getInstance().signOut();

            // Xoá session lưu userId (nếu đăng nhập bằng tài khoản thường)
            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Toast.makeText(ProfileActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

            // Mở lại trang Login
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Mở thư viện khi click vào ảnh
        profileImageView.setOnClickListener(v -> openImageChooser());


    }
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Nhận kết quả ảnh được chọn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri); // Gán ảnh vừa chọn vào ImageView
        }
    }

}
