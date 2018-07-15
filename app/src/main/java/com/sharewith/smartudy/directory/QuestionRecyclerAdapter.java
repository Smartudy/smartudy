package com.sharewith.smartudy.directory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharewith.smartudy.utils.Question;
import com.sharewith.smartudy.smartudy.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by cheba on 2018-07-09.
 */

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder> {

    private List<Question> questionList;
    private int itemLayout;

    public QuestionRecyclerAdapter(List<Question> list, int itemLayout){
        this.questionList=list;
        this.itemLayout=itemLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Question question = questionList.get(position);
        viewHolder.q_title.setText(question.getQ_title());
        viewHolder.q_content.setText(question.getQ_content());
        viewHolder.q_hashtag.setText(question.getQ_hashtag());
        viewHolder.q_price.setText(Integer.toString(question.getQ_price()));
        // TODO: 실제로 남은 시간 계산해서 넣어야함
        viewHolder.q_remaining_time.setText("4h 23m");
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView q_title, q_content, q_hashtag, q_price, q_remaining_time;
        public ViewHolder(View itemView){
            super(itemView);

            q_title=(TextView)itemView.findViewById(R.id.textview_question_title);
            q_content=(TextView)itemView.findViewById(R.id.textview_question_content);
            q_hashtag=(TextView)itemView.findViewById(R.id.textview_question_hashtag);
            q_price=(TextView)itemView.findViewById(R.id.textview_question_price);
            q_remaining_time=(TextView)itemView.findViewById(R.id.textview_question_remaining_time);
        }
    }
}
