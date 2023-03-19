package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.shopbanhang.R;
import com.example.shopbanhang.adapter.GioHangAdapter;
import com.example.shopbanhang.models.object.TinhTong;
import com.example.shopbanhang.retrofit.Server;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {

    TextView txtGioHang, txtTongTien;
    Toolbar toolbarGioHang;
    RecyclerView recyclerViewGioHang;
    Button btnMuaHang,btnTiepTucMuaHang;
    GioHangAdapter gioHangAdapter;
    long tongTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        anhXa();
        initControl();
       TienThanhToan();
    }


    private void TienThanhToan(){
        tongTien = 0;
        for (int i = 0; i < Server.listGioHang.size(); i++) {
            tongTien += (Server.listGioHang.get(i).getGiaSanPham() * Server.listGioHang.get(i).getSoLuongSanPham());
        }
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongTien));
    }

    private void initControl() {
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerViewGioHang.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGioHang.setLayoutManager(layoutManager);
        if (Server.listGioHang.size() == 0) {
            txtGioHang.setVisibility(View.VISIBLE);
            txtGioHang.setText("Giỏ hàng trống");
        } else {
            gioHangAdapter = new GioHangAdapter(getApplicationContext(), Server.listGioHang, txtGioHang);
            recyclerViewGioHang.setAdapter(gioHangAdapter);
        }
        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Server.listGioHang.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setTitle("Thông báo");
                    builder.setMessage("Giỏ hàng đang trống không thể mua hàng. Vui long kiểm tra lại !!!");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    Intent intent = new Intent(GioHangActivity.this, ThanhToanActivity.class);
                    intent.putExtra("tongtien", tongTien);
                    startActivity(intent);
                }
            }
        });

        if (Server.listGioHang.size() == 0) {
            btnTiepTucMuaHang.setVisibility(View.GONE);
        } else {
            btnTiepTucMuaHang.setVisibility(View.VISIBLE);
            btnTiepTucMuaHang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GioHangActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);;
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)

    public void eventTinhTien(TinhTong event){
        if(event != null){
            TienThanhToan();
        }
    }

    private void anhXa() {
        txtGioHang = findViewById(R.id.txtGioHang);
        txtTongTien = findViewById(R.id.txtTongTien);
        toolbarGioHang = findViewById(R.id.toolbarGioHang);
        recyclerViewGioHang = findViewById(R.id.recyclerViewGioHang);
        btnMuaHang = findViewById(R.id.btnMuaHang);
        btnTiepTucMuaHang = findViewById(R.id.btnTiepTucMuaHang);
    }
}