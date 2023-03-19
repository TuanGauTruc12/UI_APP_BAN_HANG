package com.example.shopbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopbanhang.Interface_click.ItemClickListerner;
import com.example.shopbanhang.R;
import com.example.shopbanhang.activity.ChiTietActivity;
import com.example.shopbanhang.models.object.SanPham;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable{

    private Context context;
    private List<SanPham> listSanPham;
    private List<SanPham> listSanPhamOld;
    private TextView txtSearch;
    private RecyclerView view;

    public SearchAdapter(Context context, List<SanPham> listSanPham, TextView txtSearch, RecyclerView view) {
        this.listSanPham = listSanPham;
        this.listSanPhamOld = listSanPham;
        this.context = context;
        this.txtSearch = txtSearch;
        this.view = view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str_Search = constraint.toString();
                if(str_Search.isEmpty()) {
                    listSanPham = listSanPhamOld;
                }else {
                    List<SanPham> list = new ArrayList<>();
                    for (SanPham sanPhamMoi : listSanPhamOld) {
                        if (sanPhamMoi.getTenSanPham().toLowerCase().contains(str_Search.toLowerCase())) {
                            list.add(sanPhamMoi);
                        }
                    }
                    listSanPham = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listSanPham;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listSanPham = (List<SanPham>) results.values;
                if(listSanPham.size() == 0) {
                    txtSearch.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                    return;
                }
                view.setVisibility(View.VISIBLE);
                txtSearch.setVisibility(View.GONE);
                notifyDataSetChanged();

            }
        };
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
        SanPham sanPham = listSanPham.get(position);
        if(sanPham == null){
            Toast.makeText(context, "Khong tim thay", Toast.LENGTH_SHORT).show();
        }
        holder.txtTenSearch.setText(sanPham.getTenSanPham());
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        holder.txtGiaSearch.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPham.getGiaSanPham()))+"Đ");
        Glide.with(context).load(sanPham.getHinhAnhSanPham()).into(holder.imgSanPhamSearch);

        holder.setItemClickListerner(new ItemClickListerner() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", sanPham);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listSanPham!=null){
            return listSanPham.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imgSanPhamSearch;
        private TextView txtTenSearch, txtGiaSearch;
        private ItemClickListerner itemClickListerner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPhamSearch = itemView.findViewById(R.id.imgSanPhamSearch);
            txtTenSearch = itemView.findViewById(R.id.txtTenSearch);
            txtGiaSearch = itemView.findViewById(R.id.txtGiaSearch);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListerner(ItemClickListerner itemClickListerner) {
            this.itemClickListerner = itemClickListerner;
        }
        @Override
        public void onClick(View v) {
            itemClickListerner.onClick(v,getAdapterPosition(),false);
        }
    }
}
