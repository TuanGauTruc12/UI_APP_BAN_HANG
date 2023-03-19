package com.example.shopbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.shopbanhang.Interface_click.IImageClickListenner;
import com.example.shopbanhang.R;
import com.example.shopbanhang.models.object.GioHang;
import com.example.shopbanhang.models.object.TinhTong;
import com.example.shopbanhang.retrofit.Server;
import org.greenrobot.eventbus.EventBus;
import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHoder> {
    private Context context;
    private List<GioHang> listGioHang;
    private TextView txtGioHang;

    public GioHangAdapter(Context context, List<GioHang> listGioHang, TextView txtGioHang) {
        this.context = context;
        this.listGioHang = listGioHang;
        this.txtGioHang = txtGioHang;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
        GioHang gioHang = listGioHang.get(position);
        holder.txtGioHangTenSP.setText(gioHang.getTenSanPham());
        holder.txtSoLuongGioHang.setText(gioHang.getSoLuongSanPham() + " ");
        Glide.with(context).load(gioHang.getHinhSanPham()).into(holder.imgGioHang);

        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        holder.txtGioHangGiaSP.setText(decimalFormat.format(gioHang.getGiaSanPham()));
        long tonggia = gioHang.getGiaSanPham() * gioHang.getSoLuongSanPham();
        holder.txtGioHangTongGia.setText(decimalFormat.format(tonggia));

        holder.setImageClickListerner(new IImageClickListenner() {
            @Override
            public void onImageClick(View view, int position, int giatri) {
                if(giatri == 1){
                    //Tru
                    if(listGioHang.get(position).getSoLuongSanPham()  > 1 ){
                        int soLuongTru = listGioHang.get(position).getSoLuongSanPham() - 1;
                        listGioHang.get(position).setSoLuongSanPham(soLuongTru);
                        holder.txtSoLuongGioHang.setText(listGioHang.get(position).getSoLuongSanPham() + " ");
                        long tonggia = listGioHang.get(position).getSoLuongSanPham() * listGioHang.get(position).getGiaSanPham();
                        holder.txtGioHangTongGia.setText(decimalFormat.format(tonggia));
                        EventBus.getDefault().postSticky(new TinhTong());
                    }
                    else if(listGioHang.get(position).getSoLuongSanPham()==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setIcon(R.drawable.ic_information);
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không? ");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Server.listGioHang.remove(position);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTong());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }else if(listGioHang.size() == 0){
                        txtGioHang.setVisibility(View.VISIBLE);
                    }
                }else if(giatri == 2){
                    //Cong
                    if(listGioHang.get(position).getSoLuongSanPham() < 10) {
                        int soLuongCong = listGioHang.get(position).getSoLuongSanPham() + 1;
                        listGioHang.get(position).setSoLuongSanPham(soLuongCong);
                        holder.txtSoLuongGioHang.setText(listGioHang.get(position).getSoLuongSanPham() + " ");
                        long tonggia = listGioHang.get(position).getSoLuongSanPham() * listGioHang.get(position).getGiaSanPham();
                        holder.txtGioHangTongGia.setText(decimalFormat.format(tonggia));
                        EventBus.getDefault().postSticky(new TinhTong());
                    }else if(listGioHang.get(position).getSoLuongSanPham()==10){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setMessage("Số lượng vượt quá giới hạn. Mời bạn kiểm tra lại!");
                        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGioHang.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgGioHang, item_Sl_Tru, item_Sl_Cong;
        TextView txtGioHangTenSP, txtGioHangGiaSP, txtSoLuongGioHang, txtGioHangTongGia;
        IImageClickListenner imageClickListerner;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            imgGioHang = itemView.findViewById(R.id.imgGioHang);
            txtGioHangTenSP = itemView.findViewById(R.id.txtGioHangTenSP);
            txtGioHangGiaSP = itemView.findViewById(R.id.txtGioHangGiaSP);
            txtSoLuongGioHang = itemView.findViewById(R.id.txtSoLuongGioHang);
            txtGioHangTongGia = itemView.findViewById(R.id.txtGioHangTongGia);
            item_Sl_Cong = itemView.findViewById(R.id.item_Sl_Cong);
            item_Sl_Tru = itemView.findViewById(R.id.item_Sl_Tru);

            item_Sl_Cong.setOnClickListener(this);
            item_Sl_Tru.setOnClickListener(this);
        }

        public void setImageClickListerner(IImageClickListenner imageClickListerner) {
            this.imageClickListerner = imageClickListerner;
        }

        @Override
        public void onClick(View v) {
            if (v == item_Sl_Tru) {
                imageClickListerner.onImageClick(v, getAdapterPosition(), 1);
            } else if (v == item_Sl_Cong) {
                imageClickListerner.onImageClick(v, getAdapterPosition(), 2);
            }
        }
    }
}
