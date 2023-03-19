package com.example.shopbanhang.models.model;

import com.example.shopbanhang.models.object.DonHang;
import com.example.shopbanhang.models.object.ThongBao;

import java.util.List;

public class DonHangModels extends ThongBao {
    List<DonHang> donHangs;

    public List<DonHang> getDonHangs() {
        return donHangs;
    }

    public void setDonHangs(List<DonHang> donHangs) {
        this.donHangs = donHangs;
    }
}
