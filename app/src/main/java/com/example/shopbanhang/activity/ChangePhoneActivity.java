package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.R;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePhoneActivity extends AppCompatActivity {

    Toolbar toolbarChangePhone;
    Button btnChangePhone;
    EditText edtChangePhone;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopBanHang apiShopBanHang;
    TextView txtErrorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);

        initView();
        actionBar();
        initControl();
    }

    private void initControl() {
        btnChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changePhone = edtChangePhone.getText().toString();
                String id = String.valueOf(Server.user.getId());
                if (TextUtils.isEmpty(changePhone)) {
                    txtErrorPhone.setVisibility(View.VISIBLE);
                    txtErrorPhone.setText("Bạn chưa nhập số điện thoại.");
                }
                else if(changePhone.length() <10 || changePhone.length() >= 11) {
                    txtErrorPhone.setVisibility(View.VISIBLE);
                    txtErrorPhone.setText("Số điện thoại phải đủ 10 số !!!");
                } else {
                    compositeDisposable.add(apiShopBanHang.changePhone(changePhone, id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModels -> {
                                        if (userModels.isSuccess()) {
                                            Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), userModels.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView() {
        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        toolbarChangePhone = findViewById(R.id.toolbarChangePhone);
        btnChangePhone = findViewById(R.id.btnChangePhone);
        edtChangePhone = findViewById(R.id.edtChangePhone);
        txtErrorPhone = findViewById(R.id.txtErrorPhone);
    }

    private void actionBar() {
        setSupportActionBar(toolbarChangePhone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChangePhone.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}