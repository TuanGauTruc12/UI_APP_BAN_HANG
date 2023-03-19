package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.R;
import com.example.shopbanhang.models.object.GioHang;
import com.example.shopbanhang.models.object.SanPham;
import com.example.shopbanhang.retrofit.Server;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {

    TextView txtTenSanPham,txtGiaSanPham,txtMoTaChiTietSanPham;
    ImageView imgChiTietSanPham;
    Button btnThemVaoGioHang;
    Spinner spinnerSoLuong;
    Toolbar toolbarChiTietSanPham;
    SanPham chitiet;
    TextView menu_soluong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);

        anhXa();
        actionBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if(Server.listGioHang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinnerSoLuong.getSelectedItem().toString());
            for (int i = 0; i < Server.listGioHang.size(); i++) {
                if (Server.listGioHang.get(i).getIdSanPham() == chitiet.getId()) {
                    Server.listGioHang.get(i).setSoLuongSanPham(soluong + Server.listGioHang.get(i).getSoLuongSanPham());
                    long gia = Long.parseLong(chitiet.getGiaSanPham()) * Server.listGioHang.get(i).getSoLuongSanPham();
                    Server.listGioHang.get(i).setGiaSanPham(gia);
                    flag = true;
                }
            }
                if(flag == false){
                    long gia = Long.parseLong(chitiet.getGiaSanPham())*soluong;
                    GioHang gioHang = new GioHang();
                    gioHang.setGiaSanPham(gia);
                    gioHang.setSoLuongSanPham(soluong);
                    gioHang.setIdSanPham(chitiet.getId());
                    gioHang.setTenSanPham(chitiet.getTenSanPham());
                    gioHang.setHinhSanPham(chitiet.getHinhAnhSanPham());
                    Server.listGioHang.add(gioHang);
                }
        }else{
            int soluong = Integer.parseInt(spinnerSoLuong.getSelectedItem().toString());
            long gia = Long.parseLong(chitiet.getGiaSanPham()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiaSanPham(gia);
            gioHang.setSoLuongSanPham(soluong);
            gioHang.setIdSanPham(chitiet.getId());
            gioHang.setTenSanPham(chitiet.getTenSanPham());
            gioHang.setHinhSanPham(chitiet.getHinhAnhSanPham());
            Server.listGioHang.add(gioHang);
        }

        int toltalItem = 0;
        for (int i = 0; i < Server.listGioHang.size(); i++) {
            toltalItem = toltalItem + Server.listGioHang.get(i).getSoLuongSanPham();
        }
        menu_soluong.setText(String.valueOf(toltalItem));
    }

    private void initData() {
        chitiet = (SanPham) getIntent().getSerializableExtra("chitiet");
        txtTenSanPham.setText(chitiet.getTenSanPham());
        txtMoTaChiTietSanPham.setText(chitiet.getMoTaSanPham());
        Glide.with(getApplicationContext()).load(chitiet.getHinhAnhSanPham()).into(imgChiTietSanPham);
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        txtGiaSanPham.setText("Giá: "+decimalFormat.format(Double.parseDouble(chitiet.getGiaSanPham()))+"Đ");
        Integer[] soluong = new  Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterSoLuongSP = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, soluong);
        spinnerSoLuong.setAdapter(adapterSoLuongSP);
    }

    private void anhXa() {
        txtTenSanPham = findViewById(R.id.txtTenSanPham);
        txtGiaSanPham = findViewById(R.id.txtGiaSanPham);
        txtMoTaChiTietSanPham = findViewById(R.id.txtMoTaChiTietSanPham);
        imgChiTietSanPham = findViewById(R.id.imgChiTietSanPham);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);
        spinnerSoLuong = findViewById(R.id.spinnerSoLuong);
        toolbarChiTietSanPham = findViewById(R.id.toolbarChiTietSanPham);
        txtTenSanPham = findViewById(R.id.txtTenSanPham);
        menu_soluong = findViewById(R.id.menu_soluong);

        FrameLayout frameGioHang = findViewById(R.id.frameGioHang);
        frameGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });


        if(Server.listGioHang != null) {
            int toltalItem = 0;
            for (int i = 0; i < Server.listGioHang.size(); i++) {
                toltalItem = toltalItem + Server.listGioHang.get(i).getSoLuongSanPham();
            }
            menu_soluong.setText(String.valueOf(toltalItem));
        }
    }

    private void actionBar() {
        setSupportActionBar(toolbarChiTietSanPham);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChiTietSanPham.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Server.listGioHang != null) {
            int toltalItem = 0;
            for (int i = 0; i < Server.listGioHang.size(); i++) {
                toltalItem = toltalItem + Server.listGioHang.get(i).getSoLuongSanPham();
            }
            menu_soluong.setText(String.valueOf(toltalItem));
        }
    }
}