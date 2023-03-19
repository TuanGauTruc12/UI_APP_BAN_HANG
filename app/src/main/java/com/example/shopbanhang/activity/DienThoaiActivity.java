package com.example.shopbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import com.example.shopbanhang.R;
import com.example.shopbanhang.adapter.DienThoaiAdapter;
import com.example.shopbanhang.models.object.SanPham;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import java.util.*;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {

    Toolbar toolbarDienThoai;
    RecyclerView recyclerViewDienThoai;
    ApiShopBanHang apiShopBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    DienThoaiAdapter adapterDT;
    List<SanPham> dienThoaiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);

        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        loai = getIntent().getIntExtra("loai",1);
        anhXa();
        actionBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerViewDienThoai.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == dienThoaiList.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dienThoaiList.add(null);
                adapterDT.notifyItemInserted(dienThoaiList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dienThoaiList.remove(dienThoaiList.size()-1);
                adapterDT.notifyItemRemoved(dienThoaiList.size());
                page = page + 1;
                getData(page);
                adapterDT.notifyDataSetChanged();
                isLoading= false;
            }
        },2000);
    }

    private void getData(int page) {
         compositeDisposable.add(apiShopBanHang.chiTietSanPham(page,loai)
         .subscribeOn(Schedulers.io())
         .observeOn(AndroidSchedulers.mainThread())
         .subscribe(
                 sanPhamMoiModels -> {
                         if (sanPhamMoiModels.isSuccess()) {
                             if (adapterDT == null) {
                                 dienThoaiList = sanPhamMoiModels.getListSanPhams();
                                 adapterDT = new DienThoaiAdapter(getApplicationContext(), dienThoaiList);
                                 recyclerViewDienThoai.setAdapter(adapterDT);
                             } else {
                                 int vitri = dienThoaiList.size() - 1;
                                 int soluongadd = sanPhamMoiModels.getListSanPhams().size();
                                 for (int i = 0; i < soluongadd; i++) {
                                     dienThoaiList.add(sanPhamMoiModels.getListSanPhams().get(i));
                                 }
                                 adapterDT.notifyItemRangeInserted(vitri, soluongadd);
                             }
                         } else {
                             Toast.makeText(getApplicationContext(), "Hết dữ liệu", Toast.LENGTH_SHORT).show();
                         }
                 },
                 throwable -> {
                     Toast.makeText(getApplicationContext(), "Không kết nối được với server: " ,Toast.LENGTH_SHORT).show();
                 }
         ));
    }

    private void actionBar() {
        setSupportActionBar(toolbarDienThoai);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDienThoai.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhXa() {
        toolbarDienThoai = findViewById(R.id.toolbarDienThoai);
        recyclerViewDienThoai = findViewById(R.id.recyclerViewDienThoai);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewDienThoai.setLayoutManager(linearLayoutManager);
        recyclerViewDienThoai.setHasFixedSize(true);
        dienThoaiList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}