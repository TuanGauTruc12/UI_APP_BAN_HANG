package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shopbanhang.R;
import com.example.shopbanhang.adapter.DonHangAdapter;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonHangActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopBanHang apiShopBanHang;
    RecyclerView recyclerViewDonHang;
    Toolbar tlbrDonHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don_hang);

        anhXa();
        toolbar();
        data();
    }

    private void toolbar() {
        setSupportActionBar(tlbrDonHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tlbrDonHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void data() {
        int id = Server.user.getId();
        compositeDisposable.add(apiShopBanHang.xemDonHang(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModels -> {
                            if(donHangModels.isSuccess()) {
                                DonHangAdapter donHangAdapter = new DonHangAdapter(donHangModels.getDonHangs(), getApplicationContext());
                                recyclerViewDonHang.setAdapter(donHangAdapter);
                            }else{
                                Toast.makeText(this, donHangModels.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void anhXa() {
        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        recyclerViewDonHang = findViewById(R.id.recyclerViewDonHang);
        tlbrDonHang = findViewById(R.id.tlbrDonHang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewDonHang.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}