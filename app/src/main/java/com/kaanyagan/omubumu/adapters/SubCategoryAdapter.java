package com.kaanyagan.omubumu.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kaanyagan.omubumu.R;
import com.kaanyagan.omubumu.models.SubCategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<SubCategoryModel> subCategoryModel;
    private Context context;
    private SubCategoryAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(SubCategoryAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public SubCategoryAdapter(Context context , List<SubCategoryModel> subCategoryModel){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.subCategoryModel = subCategoryModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_sub_category_lv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // bind the data
        holder.sub_category_id = subCategoryModel.get(position).getSubCategoryId();
        holder.sub_category_name = subCategoryModel.get(position).getSubCategoryName();
        Picasso.get().load(subCategoryModel.get(position).getSubCategoryImg()).into(holder.sub_category_image);
    }

    @Override
    public int getItemCount() {
        return subCategoryModel.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        String sub_category_id,sub_category_name;
        ImageView sub_category_image;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            sub_category_image = itemView.findViewById(R.id.sub_category_iv);
            // handle onClick

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }


}
