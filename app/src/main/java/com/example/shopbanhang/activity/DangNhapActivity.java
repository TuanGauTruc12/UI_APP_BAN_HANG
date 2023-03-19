package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.R;
import com.example.shopbanhang.models.object.User;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {

    AppCompatButton btnDangKy;
    EditText edtEmailLogin, edtPassLogin;
    AppCompatButton btnDangNhap;
    ApiShopBanHang apiShopBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        if (Server.isConnected(this)) {
            anhXa();
            getControl();
        } else {
            Toast.makeText(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối INTERNET", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getControl() {
        //Đăng ký
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });

        //Dang nhap
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailLogin.getText().toString().trim();
                String pass = edtPassLogin.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    dangNhap(email, pass);
                }
            }
        });
    }


    private void anhXa() {
        Paper.init(this);
        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        btnDangKy = findViewById(R.id.btnDangKy);
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPassLogin = findViewById(R.id.edtPassLogin);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        Intent intent = getIntent();
        String logout = intent.getStringExtra("logOut");

        if (logout == null) {
            if (Paper.book().read("email") != null && Paper.book().read("pass") != null) {
                edtEmailLogin.setText(Paper.book().read("email"));
                edtPassLogin.setText(Paper.book().read("pass"));
                if (Paper.book().read("isLogin") != null) {
                    boolean flag = Paper.book().read("isLogin");
                    if (flag) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dangNhap(Paper.book().read("email"), Paper.book().read("pass"));
                            }
                        }, 1000);
                    }
                }
            }
        } else {
            Paper.book().delete("email");
            Paper.book().delete("pass");
            edtEmailLogin.setText("");
            edtPassLogin.setText("");
        }
    }

    private void dangNhap(String email, String pass) {
        Paper.book().write("email", email);
        Paper.book().write("pass", pass);
        compositeDisposable.add(apiShopBanHang.dangnhap(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModels -> {
                            if (userModels.isSuccess()) {
                                    isLogin = true;
                                    Paper.book().write("isLogin", isLogin);
                                    Server.user = userModels.getUsers().get(0);
                                    Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String logout = intent.getStringExtra("logOut");

        if (logout == null) {
            if (Server.user.getEmail() != null && Server.user.getPass() != null) {
                edtEmailLogin.setText(Server.user.getEmail());
                edtPassLogin.setText(Server.user.getPass());
            }
        }
    }
}