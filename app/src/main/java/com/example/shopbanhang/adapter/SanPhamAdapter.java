package com.example.shopbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.Interface_click.ItemClickListerner;
import com.example.shopbanhang.R;
import com.example.shopbanhang.activity.ChiTietActivity;
import com.example.shopbanhang.models.object.SanPham;
import java.text.DecimalFormat;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.MyViewHolder> {
    Context context;
    List<SanPham> listSanPhamMoi;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi, parent, false);
        return new MyViewHolder(item);
    }

    public SanPhamAdapter(Context context, List<SanPham> listSanPhamMoi) {
        this.context = context;
        this.listSanPhamMoi = listSanPhamMoi;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPham sanPhamMoi = listSanPhamMoi.get(position);
        holder.txtTen.setText(sanPhamMoi.getTenSanPham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiaSanPham())) + "Đ");
        Glide.with(context).load(sanPhamMoi.getHinhAnhSanPham()).into(holder.imgHinh);

        holder.setItemClickListerner(new ItemClickListerner() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSanPhamMoi.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTen, txtGia;
        ImageView imgHinh;
        private ItemClickListerner itemClickListerner;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTen = itemView.findViewById(R.id.txtTenSPNew);
            txtGia = itemView.findViewById(R.id.txtGiaSPNew);
            imgHinh = itemView.findViewById(R.id.imgSanPhamNew);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListerner(ItemClickListerner itemClickListerner) {
            this.itemClickListerner = itemClickListerner;
        }

        @Override
        public void onClick(View view) {
            itemClickListerner.onClick(view, getAdapterPosition(),false);
        }
    }

}
