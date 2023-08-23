package com.kaanyagan.omubumu.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kaanyagan.omubumu.models.MainCategoryModel;
import com.kaanyagan.omubumu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<MainCategoryModel> mainCategoryModel;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MainCategoryAdapter(Context context , List<MainCategoryModel> mainCategoryModel){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mainCategoryModel = mainCategoryModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_main_category_lv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // bind the data
        holder.main_category_id = mainCategoryModel.get(position).getMainCategoryId();
        holder.main_category_name = mainCategoryModel.get(position).getMainCategoryName();
        Picasso.get().load(mainCategoryModel.get(position).getMainCategoryImg()).into(holder.main_category_image);

    }

    @Override
    public int getItemCount() {
        return mainCategoryModel.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        String main_category_id,main_category_name;
        ImageView main_category_image;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            main_category_image = itemView.findViewById(R.id.main_category_iv);
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
