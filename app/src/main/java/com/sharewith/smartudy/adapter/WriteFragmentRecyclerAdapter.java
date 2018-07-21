package com.sharewith.smartudy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.smartudy.R;

import java.util.ArrayList;

/**
 * Created by Simjae on 2018-07-20.
 */

public class WriteFragmentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public static final int STATE_TEXT = 0;
    public static final int STATE_SHOT = 1;
    public static final int STATE_ALBUM = 2;
    public static final int STATE_VOICE = 3;
    public static final int STATE_DRAW = 4;
    //상수에 static final을 사용하는 이유
    //https://djkeh.github.io/articles/Why-should-final-member-variables-be-conventionally-static-in-Java-kor/
    private ArrayList<Integer> mtypes;
    private boolean mFlag; //텍스트 입력 딱 한번만 하게 하기 위해서
    @Override
    public int getItemViewType(int position) {
        return mtypes.get(position);
    }

    public WriteFragmentRecyclerAdapter(Context context) {
        mContext = context;
        mFlag = false;
        mtypes = new ArrayList<Integer>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch(viewType){
            case STATE_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_write_text,parent,false);
                return new TextViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TextViewHolder){
        }
    }

    @Override
    public int getItemCount() {
        return mtypes.size();
    }
    //static -> 상위클래스의 멤버변수나 객체를 사용하지 않겠라는 것을 표시
    public static class TextViewHolder extends RecyclerView.ViewHolder{
        public EditText editText;
        public TextViewHolder(View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.write_fragment_contents);
        }
    }

    public void addView(int current_state) {
        if(current_state == STATE_TEXT && mFlag) return;
        else mFlag = true;

        mtypes.add(current_state);
        notifyDataSetChanged();
    }

        public Context getmContext() {
        return mContext;
    }

        public void setmContext(Context mContext) {
            this.mContext = mContext;
        }

        public ArrayList<Integer> getmtypes() {
            return mtypes;
        }

        public void setmtypes(ArrayList<Integer> mtypes) {
            this.mtypes = mtypes;
        }
}
