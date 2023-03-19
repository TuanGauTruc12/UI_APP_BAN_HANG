package com.example.shopbanhang.models.model;

import com.example.shopbanhang.models.object.LoaiSanPham;
import com.example.shopbanhang.models.object.ThongBao;

import java.util.List;

public class LoaiSPmodel extends ThongBao {
    List<LoaiSanPham> listLoaiSanPham;
    public List<LoaiSanPham> getListLoaiSanPham() {
        return listLoaiSanPham;
    }
}
