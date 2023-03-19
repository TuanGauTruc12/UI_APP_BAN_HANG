package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shopbanhang.R;

public class ThongTinActivity extends AppCompatActivity {

    Button btnLichSuMuaHang, btnThongTinCaNhan, btnDangXuat;
    Toolbar tlbrThongTin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);

        anhXa();
        actionBar();
        event();

    }

    private void actionBar() {
        setSupportActionBar(tlbrThongTin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tlbrThongTin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void event() {
        btnLichSuMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongTinActivity.this, XemDonHangActivity.class);
                startActivity(intent);
            }
        });

        btnThongTinCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongTinActivity.this, MyInformationActivity.class);
                startActivity(intent);
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setIcon(R.drawable.ic_information);
                builder.setTitle("Thông báo");
                builder.setIcon(R.drawable.ic_information);
                builder.setMessage("Bạn có muốn đăng xuất không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ThongTinActivity.this, DangNhapActivity.class);
                        intent.putExtra("logOut", "1");
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    private void anhXa() {
        btnLichSuMuaHang = findViewById(R.id.btnLichSuMuaHang);
        tlbrThongTin = findViewById(R.id.tlbrThongTin);
        btnThongTinCaNhan = findViewById(R.id.btnThongTinCaNhan);
        btnDangXuat = findViewById(R.id.btnDangXuat);
    }
}