package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class ChangePasswordActivity extends AppCompatActivity {

    Toolbar toolbarChangePassword;
    Button btnChangePassword;
    EditText edtChangePassword,edtPasswordOld;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopBanHang apiShopBanHang;
    TextView txtErrorPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
        actionBar();
        //phamtuan1initControl();
    }
/*
    private void initControl() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changePass = edtChangePassword.getText().toString().trim();
                String oldPass = edtPasswordOld.getText().toString().trim();
                String id = String.valueOf(Server.user.getId());
                if (changePass.isEmpty() || oldPass.isEmpty()) {
                    txtErrorPassword.setVisibility(View.VISIBLE);
                    txtErrorPassword.setText("Mật khẩu không được để trống");
                } else if (changePass.length() < 8 || oldPass.length() < 8) {
                    txtErrorPassword.setVisibility(View.VISIBLE);
                    txtErrorPassword.setText("Mật khẩu phải đủ 8 ký tự trở lên.");
                } else {
                    if (oldPass.equals(Server.user.getPass())) {
                        txtErrorPassword.setText("");
                        txtErrorPassword.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                        builder.setIcon(R.drawable.ic_warning);
                        builder.setTitle("Cảnh báo");
                        builder.setMessage("Bạn có muốn đổi mật khẩu không?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                compositeDisposable.add(apiShopBanHang.changePassword(id, changePass)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                userModels -> {
                                                    if (userModels.isSuccess()) {
                                                        Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
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
                    } else {
                        txtErrorPassword.setVisibility(View.VISIBLE);
                        txtErrorPassword.setText("Mật khẩu không khớp");
                    }
                }
            }
        });
    }

*/
    private void actionBar() {
        setSupportActionBar(toolbarChangePassword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChangePassword.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        toolbarChangePassword = findViewById(R.id.toolbarChangePassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        edtChangePassword = findViewById(R.id.edtChangePassword);
        txtErrorPassword = findViewById(R.id.txtErrorPassword);
        edtPasswordOld = findViewById(R.id.edtPasswordOld);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}