package com.sharewith.smartudy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.dao.Write_DBhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simjae on 2018-07-16.
 */

public class QnAListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<NotePadDto> datas;
    Context context;
    private List<Integer> mImagedatas;
    private RecyclerView mImageRecycler;
    private QnAImageAdapter mImageAdapter;
    private static final int VIEW_TYPE_QUESTION = 0;
    private static final int VIEW_TYPE_ANSWER = 1;
    private Write_DBhelper DBhelper;

    public void addNotePad(NotePadDto notepad){
        if(notepad != null) {
            datas.add(notepad);
            notifyDataSetChanged();
        }

    }

    public QnAListAdapter(Context context, List<NotePadDto> datas) {
        this.datas = datas;
        this.context = context;
        DBhelper = new Write_DBhelper(context);
    }

    public void setImageDatas(){
        if(mImagedatas == null) mImagedatas = new ArrayList<Integer>();
        mImagedatas.add(R.drawable.five);
        mImagedatas.add(R.drawable.six);
        mImagedatas.add(R.drawable.one);
    }

    public void changeImageDatas(){
        mImagedatas.clear();
        mImagedatas.add(R.drawable.one);
        mImagedatas.add(R.drawable.two);
        mImagedatas.add(R.drawable.three);
    }

    private void setRecycler(View view){
        mImageRecycler = view.findViewById(R.id.qna_image_recycler_view);
        mImageRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL,false));
        mImageAdapter = new QnAImageAdapter(mImagedatas);
        mImageRecycler.setAdapter(mImageAdapter);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_QUESTION : VIEW_TYPE_ANSWER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //처음에 한번만 뷰홀더를 만들기 위해서 호출됨.
        //parent는 리싸이클러뷰를 의미
        if(viewType == VIEW_TYPE_QUESTION){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qna_recycler_question,parent,false);
            setImageDatas();
            setRecycler(view);
            return new QuestionViewHolder(view);
        }else if(viewType == VIEW_TYPE_ANSWER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qna_recycler_answer,parent,false);
            setImageDatas();
            setRecycler(view);
            return new AnswerViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        QuestionViewHolder qholder=null;
        AnswerViewHolder aholder=null;
        NotePadDto notepad;
        if(position ==0)
            qholder = (QuestionViewHolder)viewHolder;
        else
            aholder = (AnswerViewHolder)viewHolder;

        List<NotePadDto> notepads = DBhelper.selectAllNotePad();

        if(notepads.size() != 0) {
            notepad = notepads.get(position);
            switch (position) {
                case 0:
                    qholder.mQuestionTitle.setText(notepad.getTitle());
                    qholder.mContentText.setText(notepad.getContents());
                    break;
                default:
                    aholder.mAnswerTitle.setText(notepad.getTitle());
                    aholder.mContentText.setText(notepad.getContents());
                    changeImageDatas();
                    ((QnAImageAdapter) aholder.mImageRecycler.getAdapter()).setImageDatas(mImagedatas);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /*뷰 홀더를 static으로 선언함으로써 outer class의 멤버에 접근하지 않겠다고 명시함.
    * non-static으로 뷰홀더를 선언시 outer class인 어댑터를 생성한다음 뷰홀더를 생성해야 하므로
    * outer class의 참조가 계속 유지되어 메모리 누수 발생 가능성이 있음.
    * */
     static class QuestionViewHolder extends RecyclerView.ViewHolder{
             private TextView mQuestionTitle;
             private ImageView mCoinImage;
             private TextView mCoinText;
             private TextView mUserName;
             private Button mLevel;
             private TextView mUserInfo;
             private ImageView mClockImage;
             private TextView mTimeText;
             private RecyclerView mImageRecycler;
             private EditText mContentText;
             private TextView mHashTag;
             private Button mComplainButton;

             QuestionViewHolder(View item) {
                 super(item);
                 mQuestionTitle = item.findViewById(R.id.qna_question_title);
                 mCoinImage = item.findViewById(R.id.qna_coin_image);
                 mCoinText = item.findViewById(R.id.qna_coin_text);
                 mUserName = item.findViewById(R.id.qna_user_name);
                 mLevel = item.findViewById(R.id.qna_user_level);
                 mUserInfo = item.findViewById(R.id.qna_user_info);
                 mClockImage = item.findViewById(R.id.qna_clock);
                 mTimeText = item.findViewById(R.id.qna_time);
                 mImageRecycler =  item.findViewById(R.id.qna_image_recycler_view);
                 mContentText = item.findViewById(R.id.qna_content_text);
                 mHashTag = item.findViewById(R.id.qna_hashtag);
                 mComplainButton = item.findViewById(R.id.qna_complain);
             }
    }

     static class AnswerViewHolder extends RecyclerView.ViewHolder{
        private TextView mAnswerTitle;
        private TextView mUserName;
        private Button mLevel;
        private TextView mUserInfo;
        private RecyclerView mImageRecycler;
        private EditText mContentText;
        private Button mComplainButton;

         AnswerViewHolder(View item) {
            super(item);
            mAnswerTitle = item.findViewById(R.id.qna_answer_title);
            mUserName = item.findViewById(R.id.qna_user_name);
            mLevel = item.findViewById(R.id.qna_user_level);
            mUserInfo = item.findViewById(R.id.qna_user_info);
            mImageRecycler =  item.findViewById(R.id.qna_image_recycler_view);
            mContentText = item.findViewById(R.id.qna_content_text);
            mComplainButton = item.findViewById(R.id.qna_complain);
        }
    }
}
