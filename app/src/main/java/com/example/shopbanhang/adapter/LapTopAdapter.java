package com.example.shopbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class LapTopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPham> array;
    private static final int view_Data = 0;
    private static final int view_Load = 1;

    public LapTopAdapter(Context context, List<SanPham> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == view_Data){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham,parent,false);
            return new MyViewHoder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load,parent,false);
            return new LoaddingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHoder){
            MyViewHoder myViewHoder = (MyViewHoder) holder;
            SanPham laptop = array.get(position);
            //set màu chữ
            myViewHoder.txtTenLapTop.setTextColor(Color.parseColor("#14EDBB"));
            myViewHoder.txtGiaLapTop.setTextColor(Color.parseColor("#14EDBB"));
            myViewHoder.txtMoTaLapTop.setTextColor(Color.parseColor("#14EDBB"));

            myViewHoder.txtTenLapTop.setText(laptop.getTenSanPham());
            DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
            myViewHoder.txtGiaLapTop.setText("Giá: "+decimalFormat.format(Double.parseDouble(laptop.getGiaSanPham()))+"Đ");
            myViewHoder.txtMoTaLapTop.setText(laptop.getMoTaSanPham());
            Glide.with(context).load(laptop.getHinhAnhSanPham()).into(myViewHoder.imgLapTop);


            myViewHoder.setItemClickListerner(new ItemClickListerner() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if (!isLongClick) {
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet", laptop);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }else {
            LoaddingViewHolder loaddingViewHolder = (LoaddingViewHolder) holder;
            loaddingViewHolder.progressBarLoad.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? view_Load:view_Data;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class LoaddingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBarLoad;

        public LoaddingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBarLoad = itemView.findViewById(R.id.progressBarLoad);
        }
    }

    public class MyViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTenLapTop,txtGiaLapTop, txtMoTaLapTop;
        ImageView imgLapTop;

        private ItemClickListerner itemClickListerner;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            txtTenLapTop = itemView.findViewById(R.id.txtTenSP);
            txtGiaLapTop = itemView.findViewById(R.id.txtGiaSP);
            txtMoTaLapTop = itemView.findViewById(R.id.txtMoTaSP);
            imgLapTop = itemView.findViewById(R.id.imgSP);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListerner(ItemClickListerner itemClickListerner) {
            this.itemClickListerner = itemClickListerner;
        }

        @Override
        public void onClick(View view) {
            itemClickListerner.onClick(view,getAdapterPosition(),false);
        }
    }
}
