package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shopbanhang.R;
import com.example.shopbanhang.adapter.SearchAdapter;
import com.example.shopbanhang.models.object.SanPham;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    SearchView search_SanPham;
    RecyclerView recyclerViewTimKiem;
    SearchAdapter searchSanPhamAdapter;
    List<SanPham> sanpham;
    LinearLayoutManager linearLayoutManager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopBanHang apiShopBanHang;
    TextView txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        anhXa();
        getSanPham();
        setSearch();
    }

    private void setSearch() {
        search_SanPham.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSanPhamAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSanPhamAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void anhXa() {
        search_SanPham = findViewById(R.id.search_SanPham);
        recyclerViewTimKiem = findViewById(R.id.recyclerViewTimKiem);
        sanpham = new ArrayList<>();
        txtSearch = findViewById(R.id.txtSearch);
        linearLayoutManager = new LinearLayoutManager(this);
        searchSanPhamAdapter = new SearchAdapter(getApplicationContext(), sanpham, txtSearch, recyclerViewTimKiem);
        recyclerViewTimKiem.setLayoutManager(linearLayoutManager);
        recyclerViewTimKiem.setAdapter(searchSanPhamAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewTimKiem.addItemDecoration(itemDecoration);
    }

    private void getSanPham() {
        compositeDisposable.add(apiShopBanHang.getSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModels -> {
                            if (sanPhamMoiModels.isSuccess()) {
                                sanpham = sanPhamMoiModels.getListSanPhams();
                                searchSanPhamAdapter = new SearchAdapter(getApplicationContext(), sanpham, txtSearch, recyclerViewTimKiem);
                                recyclerViewTimKiem.setAdapter(searchSanPhamAdapter);
                            }
                        }));
    }


    @Override
    public void onBackPressed() {
        if(!search_SanPham.isIconified()){
            search_SanPham.setIconified(true);
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}