package com.kaanyagan.omubumu.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaanyagan.omubumu.R;
import com.kaanyagan.omubumu.models.QuestionModel;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<QuestionModel> questionModel;
    private Context context;
    private QuestionAdapter.OnItemClickListener mListener;

    public QuestionAdapter(Context context , List<QuestionModel> questionModel){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.questionModel = questionModel;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(QuestionAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_question_lv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // bind the data
        holder.question_id = questionModel.get(position).getQuestionId();
        holder.question_sub_category_id = questionModel.get(position).getQuestionSubCategoryId();
        holder.question_row_id = questionModel.get(position).getQuestionRowId();
        holder.question_choose_id = questionModel.get(position).getQuestionChooseId();
        holder.question_a = questionModel.get(position).getQuestionA();
        holder.question_A_tv.setText(questionModel.get(position).getQuestionA());
        holder.img_checked.setBackgroundResource(R.drawable.checked);

        if (position==0){
            holder.questionA_ll.setBackgroundResource(R.drawable.question_item_1);
        }
        if(position==1){
            holder.questionA_ll.setBackgroundResource(R.drawable.question_item_2);
        }
    }

    @Override
    public int getItemCount() {
        return questionModel.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        String question_id,question_sub_category_id,question_choose_id,question_row_id,question_a,question_a_count;
        TextView question_A_tv;
        LinearLayout ll;
        ConstraintLayout questionA_ll;
        ImageView img_checked;

        public ViewHolder(View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.question_ll);
            questionA_ll = itemView.findViewById(R.id.questionA_ll);
            question_A_tv = itemView.findViewById(R.id.questionA_tv);
            img_checked = itemView.findViewById(R.id.img_checked);
            //img_checked = itemView.findViewById(R.id.img_checked);

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
