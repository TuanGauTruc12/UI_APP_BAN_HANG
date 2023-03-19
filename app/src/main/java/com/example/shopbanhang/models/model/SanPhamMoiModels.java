package com.example.shopbanhang.models.model;

import com.example.shopbanhang.models.object.SanPham;
import com.example.shopbanhang.models.object.ThongBao;

import java.util.List;

public class SanPhamMoiModels extends ThongBao {
    private List<SanPham> listSanPhams;
    public List<SanPham> getListSanPhams() {
        return listSanPhams;
    }
}
