package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.shopbanhang.R;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.io.IOException;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyInformationActivity extends AppCompatActivity {

    private CircleImageView imgUser;
    private TextView txtNameUser,txtPhone, txtEmail;
    private LinearLayout layoutPhone, layoutChangePassword;
    private Toolbar toolbarMyInformation;
    ApiShopBanHang apiShopBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);

        initView();
        actionBar();
        initControl();
    }

    private void initControl() {
        Glide.with(getApplicationContext()).load(Server.user.getImage()).into(imgUser);
        layoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInformationActivity.this, ChangePhoneActivity.class);
                startActivity(intent);
            }
        });

        layoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInformationActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        openImage();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.with(getApplicationContext())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });

        try {
            Uri imageUser = Uri.parse(Server.user.getImage());
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUser);
            Glide.with(getApplicationContext()).load(bitmap).into(imgUser);
        } catch (IOException e) {
            e.printStackTrace();
        }

        txtNameUser.setText(Server.user.getUsername());
        txtEmail.setText(Server.user.getEmail());
        txtPhone.setText(Server.user.getMobile());

    }

    private void openImage() {
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    imgUser.setImageBitmap(bitmap);
                    Server.user.setImage(uri.toString());
                    //post lên server
                    String id = String.valueOf(Server.user.getId());
                    compositeDisposable.add(apiShopBanHang.setIamgeUser(id, uri.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModels -> {
                                        if (userModels.isSuccess()){

                                        }else{
                                            Toast.makeText(getApplicationContext(), userModels.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getApplicationContext())
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }


    private void actionBar() {
        setSupportActionBar(toolbarMyInformation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarMyInformation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);
        imgUser = findViewById(R.id.imgUser);
        txtNameUser = findViewById(R.id.txtNameUser);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        layoutPhone = findViewById(R.id.layoutPhone);
        layoutChangePassword = findViewById(R.id.layoutChangePassword);
        toolbarMyInformation = findViewById(R.id.toolbarMyInformation);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}