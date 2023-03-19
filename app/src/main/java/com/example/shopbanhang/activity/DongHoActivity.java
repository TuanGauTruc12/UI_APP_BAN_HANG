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
import com.example.shopbanhang.adapter.DongHoAdapter;
import com.example.shopbanhang.models.object.SanPham;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DongHoActivity extends AppCompatActivity {

    Toolbar toolbarDongHo;
    RecyclerView recyclerViewDongHo;
    ApiShopBanHang apiShopBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    DongHoAdapter adapterDH;
    List<SanPham> dongHoList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_ho);

        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        loai = getIntent().getIntExtra("loai",3);
        anhXa();
        actionBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerViewDongHo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == dongHoList.size()-1){
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
                dongHoList.add(null);
                adapterDH.notifyItemInserted(dongHoList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dongHoList.remove(dongHoList.size()-1);
                adapterDH.notifyItemRemoved(dongHoList.size());
                page = page + 1;
                getData(page);
                adapterDH.notifyDataSetChanged();
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
                                if (adapterDH == null) {
                                    dongHoList = sanPhamMoiModels.getListSanPhams();
                                    adapterDH = new DongHoAdapter(getApplicationContext(), dongHoList);
                                    recyclerViewDongHo.setAdapter(adapterDH);
                                } else {
                                    int vitri = dongHoList.size() - 1;
                                    int soluongadd = sanPhamMoiModels.getListSanPhams().size();
                                    for (int i = 0; i < soluongadd; i++) {
                                        dongHoList.add(sanPhamMoiModels.getListSanPhams().get(i));
                                    }
                                    adapterDH.notifyItemRangeInserted(vitri, soluongadd);
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
        setSupportActionBar(toolbarDongHo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDongHo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhXa() {
        toolbarDongHo = findViewById(R.id.toolbarDongHo);
        recyclerViewDongHo = findViewById(R.id.recyclerViewDongHo);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewDongHo.setLayoutManager(linearLayoutManager);
        recyclerViewDongHo.setHasFixedSize(true);
        dongHoList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}