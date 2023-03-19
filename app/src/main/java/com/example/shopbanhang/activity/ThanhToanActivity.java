package com.example.shopbanhang.activity;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.shopbanhang.R;
import com.example.shopbanhang.models.object.GioHang;
import com.example.shopbanhang.models.object.SanPham;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {

    Toolbar toolbarThanhToan;
    EditText edtSDT,edtEmail, edtLocation;
    AppCompatButton btnThanhToan;
    TextView txtTongTienThanhToan;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopBanHang apiShopBanHang;
    long tongtien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        anhXa();
        countSanPham();
        initView();
    }

    private void countSanPham() {
        totalItem = 0;
        for (int i = 0; i < Server.listGioHang.size(); i++) {
            totalItem = totalItem + Server.listGioHang.get(i).getSoLuongSanPham();
        }
    }

    private void initView() {
        //set acrionBar
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Tien thanh toan
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien", 0);
        txtTongTienThanhToan.setText(decimalFormat.format(tongtien));
        edtEmail.setText(Server.user.getEmail());
        edtSDT.setText(Server.user.getMobile());
        //btnThanhToan
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dữ liệu gửi về Server
                setDonHang(v);
            }
        });
    }

    private void setDonHang(View view) {
        String diachi = edtLocation.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSDT.getText().toString().trim();
        String iduser = String.valueOf(Server.user.getId());
        //ArrayList<GioHang> gioHangs = new ArrayList<>();
        String chitiet = new Gson().toJson(Server.listGioHang);;
        if (TextUtils.isEmpty(sdt)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(diachi)) {
            Toast.makeText(getApplicationContext(), "Ban chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
        } else {
            //gửi data lên server
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
            builder.setIcon(R.drawable.ic_warning);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có muốn thanh toán không");
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    compositeDisposable.add(apiShopBanHang.donHang(email, sdt, tongtien, iduser, diachi, totalItem, chitiet)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModels -> {
                                        if (userModels.isSuccess()) {
                                            Toast.makeText(getApplicationContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ThanhToanActivity.this, MainActivity.class);
                                            Server.listGioHang.clear();
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), userModels.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private void anhXa() {
        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        edtEmail = findViewById(R.id.edtEmailThanhToan);
        edtSDT = findViewById(R.id.edtSDTThanhToan);
        edtLocation = findViewById(R.id.edtLocation);
        txtTongTienThanhToan = findViewById(R.id.txtTongTienThanhToan);
        btnThanhToan = findViewById(R.id.btnThanhToan);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}