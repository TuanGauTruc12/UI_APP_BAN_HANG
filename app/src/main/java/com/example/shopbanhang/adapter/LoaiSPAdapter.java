package com.example.shopbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.R;
import com.example.shopbanhang.models.object.LoaiSanPham;

import java.util.List;

public class LoaiSPAdapter extends BaseAdapter {

    List<LoaiSanPham> listLoaiSP;
    Context context;

    public LoaiSPAdapter( Context context,List<LoaiSanPham> listLoaiSP) {
        this.listLoaiSP = listLoaiSP;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listLoaiSP.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder{
        TextView tenLoaiSP;
        ImageView hinhloaiSanPham;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {//Nếu chưa run lân nào thi tạo mới layout
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//Lấy service và layout ra màn hình
            view = inflater.inflate(R.layout.item_list_loai_sp, null);
            viewHolder.tenLoaiSP = view.findViewById(R.id.txtTenLoaiSP);
            viewHolder.hinhloaiSanPham = view.findViewById(R.id.imgLoaiSanPham);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tenLoaiSP.setText(listLoaiSP.get(i).getTenLoaiSanPham());
        Glide.with(context).load(listLoaiSP.get(i).getHinhAnh()).into(viewHolder.hinhloaiSanPham);
        return view;
    }
}
