package com.example.shopbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.R;
import com.example.shopbanhang.models.object.Item;

import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.MyViewHolder> {

    List<Item> itemList;
    Context context;

    public ChiTietDonHangAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chi_tiet_don_hang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtDonHangTenSP.setText("Tên sản phẩm: " + item.getTenSanPham());
        holder.txtDonHangSoLuongSP.setText("Số lượng: " + item.getSoLuongSanPham());
        Glide.with(context).load(item.getHinhSanPham()).into(holder.imgItemChiTietDonHang);
        holder.txtDonHangGiaSP.setText("Giá: " + item.getGiaSanPham());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgItemChiTietDonHang;
        TextView txtDonHangTenSP, txtDonHangSoLuongSP, txtDonHangGiaSP;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItemChiTietDonHang = itemView.findViewById(R.id.imgItemChiTietDonHang);
            txtDonHangTenSP = itemView.findViewById(R.id.txtDonHangTenSP);
            txtDonHangSoLuongSP = itemView.findViewById(R.id.txtDonHangSoLuongSP);
            txtDonHangGiaSP = itemView.findViewById(R.id.txtDonHangGiaSP);
        }
    }
}
