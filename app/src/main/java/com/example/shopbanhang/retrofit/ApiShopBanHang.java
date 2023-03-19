package com.example.shopbanhang.retrofit;

import com.example.shopbanhang.models.model.DonHangModels;
import com.example.shopbanhang.models.model.LoaiSPmodel;
import com.example.shopbanhang.models.model.QuangCaoModels;
import com.example.shopbanhang.models.model.SanPhamMoiModels;
import com.example.shopbanhang.models.model.UserModels;
import com.example.shopbanhang.models.object.GioHang;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiShopBanHang {
    //Lấy loại sản phẩm về
    @GET("getLoaiSanPham")
    Observable<LoaiSPmodel> getLoaiSanPham();

    ///Lấy sản phẩm về
    @GET("getSanPham")
    Observable<SanPhamMoiModels> getSanPham();

    //chi tiết sản phẩm
    @POST("chitiet")
    @FormUrlEncoded
    Observable<SanPhamMoiModels> chiTietSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );

    //Đăng ký
    @POST("dangKy")
    @FormUrlEncoded
    Observable<UserModels> dangky(
            @Field("email") String email,
            @Field("username") String username,
            @Field("pass") String pass,
            @Field("mobile") String mobile
    );

    //Đăng nhập
    @POST("dangNhap")
    @FormUrlEncoded
    Observable<UserModels> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );

    //Khôi phục mật khẩu
    @POST("reset")
    @FormUrlEncoded
    Observable<UserModels> quenMatKhau(
            @Field("email") String email
    );

    //Thanh toán
    @POST("donhang")
    @FormUrlEncoded
    Observable<UserModels> donHang(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongTien") long tongTien,
            @Field("id") String id,
            @Field("diaChi") String diaChi,
            @Field("soLuong") int soLuong,
            @Field("gioHangs") String gioHangs
    );

    //Lịch sử mua hàng
    @POST("xemdonhang")
    @FormUrlEncoded
    Observable<DonHangModels> xemDonHang(
            @Field("id") int id
    );

    //Đổi só điện thoại
    @POST("doiSoDienThoai")
    @FormUrlEncoded
    Observable<UserModels> changePhone(
            @Field("mobile") String phoneNumber,
            @Field("id") String id
    );

    //post image user lên server
    @POST("doiAnh")
    @FormUrlEncoded
    Observable<UserModels> setIamgeUser(
            @Field("id") String id,
            @Field("image") String image
    );

    //get quảng cáo
    @GET("getQuangCao")
    Observable<QuangCaoModels> getQuangCao();
}