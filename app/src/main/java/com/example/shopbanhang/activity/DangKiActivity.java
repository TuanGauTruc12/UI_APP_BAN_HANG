package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopbanhang.R;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {

    private EditText edtEmail, edtPass, edtRePass, edtMobile, edtUserName;
    private AppCompatButton btnDangKy;
    private ApiShopBanHang apiShopBanHang;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);

        compositeDisposable = new CompositeDisposable();
        anhXa();
        getControl();

    }

    private void getControl() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });
    }

    private void dangKy() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String repass = edtRePass.getText().toString().trim();
        String username = edtUserName.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(username)){
                Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên người dùng", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(mobile)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(repass)){
                Toast.makeText(getApplicationContext(), "Bạn chưa nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
        } else if(pass.length() < 8) {
            Toast.makeText(getApplicationContext(), "Mật khẩu phải từ 8 ký tự trở lên", Toast.LENGTH_SHORT).show();
        } else {
            if (pass.equals(repass)) {
                dangKy(email, username, pass, mobile);
            }else{
                Toast.makeText(getApplicationContext(), "Mật khẩu không khớp. Vui long kiểm tra lại mật khẩu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dangKy(String email, String username, String pass, String mobile) {
        compositeDisposable.add(apiShopBanHang.dangky(email, username , pass, mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModels -> {
                            if(userModels.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DangKiActivity.this, DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),userModels.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                ));
    }

    private void anhXa() {
        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        edtMobile = findViewById(R.id.edtMobile);
        edtRePass = findViewById(R.id.edtRePass);
        btnDangKy = findViewById(R.id.btnDangKy);
        edtUserName = findViewById(R.id.edtUserName);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}