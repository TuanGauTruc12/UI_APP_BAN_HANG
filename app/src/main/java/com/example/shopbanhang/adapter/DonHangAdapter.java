package com.example.shopbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.R;
import com.example.shopbanhang.models.object.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHoder> {

    List<DonHang> donHangList;
    Context context;

    public DonHangAdapter(List<DonHang> donHangList, Context context) {
        this.donHangList = donHangList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
        DonHang donHang = donHangList.get(position);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerViewChiTietDonHang.getContext(), LinearLayoutManager.VERTICAL,false);
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        ChiTietDonHangAdapter chiTietDonHang = new ChiTietDonHangAdapter(donHang.getItem(),context);
        holder.txtItemDonHang.setText("Đơn hàng: " + donHang.getId());
        holder.recyclerViewChiTietDonHang.setLayoutManager(layoutManager);
        holder.recyclerViewChiTietDonHang.setAdapter(chiTietDonHang);
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder{

        TextView txtItemDonHang;
        RecyclerView recyclerViewChiTietDonHang;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);

            recyclerViewChiTietDonHang = itemView.findViewById(R.id.recyclerViewChiTietDonHang);
            txtItemDonHang = itemView.findViewById(R.id.txtItemDonHang);
        }
    }
}
