package com.example.shopbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.shopbanhang.R;
import com.example.shopbanhang.adapter.LapTopAdapter;
import com.example.shopbanhang.models.object.SanPham;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LapTopActivity extends AppCompatActivity {

    Toolbar toolbarLapTop;
    RecyclerView recyclerViewLapTop;
    ApiShopBanHang apiShopBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    LapTopAdapter adapterLT;
    List<SanPham> lapTopList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_top);

        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        loai = getIntent().getIntExtra("loai",2);
        anhXa();
        actionBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerViewLapTop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == lapTopList.size()-1){
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
                lapTopList.add(null);
                adapterLT.notifyItemInserted(lapTopList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lapTopList.remove(lapTopList.size()-1);
                adapterLT.notifyItemRemoved(lapTopList.size());
                page = page + 1;
                getData(page);
                adapterLT.notifyDataSetChanged();
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
                                if (adapterLT == null) {
                                    lapTopList = sanPhamMoiModels.getListSanPhams();
                                    adapterLT = new LapTopAdapter(getApplicationContext(), lapTopList);
                                    recyclerViewLapTop.setAdapter(adapterLT);
                                } else {
                                    int vitri = lapTopList.size() - 1;
                                    int soluongadd = sanPhamMoiModels.getListSanPhams().size();
                                    for (int i = 0; i < soluongadd; i++) {
                                        lapTopList.add(sanPhamMoiModels.getListSanPhams().get(i));
                                    }
                                    adapterLT.notifyItemRangeInserted(vitri, soluongadd);
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
        setSupportActionBar(toolbarLapTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLapTop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhXa() {
        toolbarLapTop = findViewById(R.id.toolbarLapTop);
        recyclerViewLapTop = findViewById(R.id.recyclerViewLapTop);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewLapTop.setLayoutManager(linearLayoutManager);
        recyclerViewLapTop.setHasFixedSize(true);
        lapTopList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}