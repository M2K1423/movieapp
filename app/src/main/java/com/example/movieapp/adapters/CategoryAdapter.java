package com.example.movieapp.adapters;//package com.example.movieapp.adaptors;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.movieapp.Domian.CategoryItem;
//import com.example.movieapp.R;
//
//import java.util.List;
//
//public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
//    private List<CategoryItem> items;
//    private Context context;
//
//    public CategoryAdapter(List<CategoryItem> items) {
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        return new ViewHolder(LayoutInflater.from(context)
//                .inflate(R.layout.viewholder_category, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.categoryNameTxt.setText(items.get(position).getTitle());
//
//        Glide.with(context)
//                .load(items.get(position).getImageUrl())
//                .into(holder.categoryPic);
//
//        holder.itemView.setOnClickListener(v -> {
//            // TODO: Handle category click
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView categoryNameTxt;
//        ImageView categoryPic;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            categoryNameTxt = itemView.findViewById(R.id.categoryNameTxt);
//            categoryPic = itemView.findViewById(R.id.categoryPic);
//        }
//    }
//}