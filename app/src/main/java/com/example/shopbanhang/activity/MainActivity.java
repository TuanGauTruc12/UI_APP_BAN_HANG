package com.example.shopbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.R;
import com.example.shopbanhang.adapter.LoaiSPAdapter;
import com.example.shopbanhang.adapter.SanPhamAdapter;
import com.example.shopbanhang.models.object.LoaiSanPham;
import com.example.shopbanhang.models.object.QuangCao;
import com.example.shopbanhang.models.object.SanPham;
import com.example.shopbanhang.retrofit.ApiShopBanHang;
import com.example.shopbanhang.retrofit.RetrofitClient;
import com.example.shopbanhang.retrofit.Server;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Toolbar tlbrTrangChinh;
    private ViewFlipper vflpProduct;
    private RecyclerView rcclvManHinhChinh;
    private ListView lvTrangChu;
    private DrawerLayout drwloTrangChinh;
    LoaiSPAdapter loaiSPAdapter;
    List<LoaiSanPham> mangloaisanpham;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopBanHang apiShopBanHang;
    List<SanPham> mangsanphammoi;
    SanPhamAdapter sanPhamMoiAdapter;
    //Cart
    TextView menu_soluongmain;
    //QuangCao
    List<QuangCao> quangCaoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiShopBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiShopBanHang.class);

        anhXa();
        actionBar();
        getAdvertisement();
        getLoaiSanPham();
        getSanPhamMoi();
        getSuKienClickListView();
    }


    private void getSuKienClickListView() {
        lvTrangChu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        finish();
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai", 2);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), LapTopActivity.class);
                        laptop.putExtra("loai", 3);
                        startActivity(laptop);
                        break;
                    case 3:
                        Intent dongho = new Intent(getApplicationContext(), DongHoActivity.class);
                        dongho.putExtra("loai", 4);
                        startActivity(dongho);
                        break;
                    case 4:
                        Intent lienhe = new Intent(getApplicationContext(), LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                    case 5:
                        Intent thongtin = new Intent(getApplicationContext(), ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                }
            }
        });
    }

    private void getSanPhamMoi() {
        compositeDisposable.add(apiShopBanHang.getSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModels -> {
                            if (sanPhamMoiModels.isSuccess()) {
                                mangsanphammoi = sanPhamMoiModels.getListSanPhams();
                                sanPhamMoiAdapter = new SanPhamAdapter(getApplicationContext(), mangsanphammoi);
                                rcclvManHinhChinh.setAdapter(sanPhamMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server ", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiShopBanHang.getLoaiSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSPmodel -> {
                            if (loaiSPmodel.isSuccess()) {
                                mangloaisanpham = loaiSPmodel.getListLoaiSanPham();
                                loaiSPAdapter = new LoaiSPAdapter(getApplicationContext(), mangloaisanpham);
                                lvTrangChu.setAdapter(loaiSPAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server ", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void anhXa() {
        tlbrTrangChinh = findViewById(R.id.tlbrTrangChinh);
        vflpProduct = findViewById(R.id.vflpProduct);
        rcclvManHinhChinh = findViewById(R.id.rcclvManHinhChinh);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rcclvManHinhChinh.setLayoutManager(layoutManager);
        rcclvManHinhChinh.setHasFixedSize(true);
        rcclvManHinhChinh.setAdapter(sanPhamMoiAdapter);
        lvTrangChu = findViewById(R.id.lvTrangChu);
        drwloTrangChinh = findViewById(R.id.drwloTrangChinh);
        mangloaisanpham = new ArrayList<>();
        mangsanphammoi = new ArrayList<>();
        quangCaoList = new ArrayList<>();
    }

    private void actionBar() {
        setSupportActionBar(tlbrTrangChinh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tlbrTrangChinh.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        tlbrTrangChinh.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drwloTrangChinh.openDrawer(GravityCompat.START);
            }
        });
    }

    private void ActionViewFlipper() {
        for (int i = 0; i < quangCaoList.size(); i++) {
            ImageView hinhQuangCao = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(quangCaoList.get(i).getHinhAnhSanPham()).into(hinhQuangCao);
            hinhQuangCao.setScaleType(ImageView.ScaleType.FIT_XY);
            vflpProduct.addView(hinhQuangCao);
        }

        vflpProduct.setFlipInterval(5000);
        vflpProduct.setAutoStart(true);
        Animation animation_Slide_In = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_Slide_Out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        vflpProduct.setInAnimation(animation_Slide_In);
        vflpProduct.setOutAnimation(animation_Slide_Out);
    }

    private void getAdvertisement() {
        compositeDisposable.add(apiShopBanHang.getQuangCao()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        quangCaoModels -> {
                            if (quangCaoModels.isSuccess()) {
                                quangCaoList = quangCaoModels.getListQuangCaos();
                                ActionViewFlipper();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server ", Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    //set menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        //Lấy menu gio hang
        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        //so luong gio hang
        FrameLayout frameLayout = (FrameLayout) menuItem.getActionView();
        menu_soluongmain = (TextView) frameLayout.findViewById(R.id.menu_soluongmain);
        updateCart();
        //click cart
        View actionView = menuItem.getActionView();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }


    //Cập nhật giỏ hàng
    private void updateCart() {
        if (Server.listGioHang == null) {
            Server.listGioHang = new ArrayList<>();
        } else {
            int totalItem = 0;
            for (int i = 0; i < Server.listGioHang.size(); i++) {
                totalItem = totalItem + Server.listGioHang.get(i).getSoLuongSanPham();
            }
            menu_soluongmain.setText(String.valueOf(totalItem));
        }
    }

    //set event click cho menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart: {
                Intent intent = new Intent(MainActivity.this, GioHangActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.menu_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();//Cập nhật lại số lượng giỏ hàng
    }
}