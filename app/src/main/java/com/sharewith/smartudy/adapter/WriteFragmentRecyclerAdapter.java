package com.sharewith.smartudy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.BitmapImageProcess;
import com.sharewith.smartudy.dto.WriteFragComponent;

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
    private ArrayList<WriteFragComponent> datas;
    private boolean mFlag; //텍스트 입력 딱 한번만 하게 하기 위해서

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    public WriteFragmentRecyclerAdapter(Context context) {
        mContext = context;
        mFlag = false;
        datas = new ArrayList<WriteFragComponent>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch(viewType){
            case STATE_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_write_text,parent,false);
                return new EditTextHolder(view);
            case STATE_SHOT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_write_image,parent,false);
                return new ImageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = datas.get(position).getType();
        String str = datas.get(position).getString();
        switch(type){
            case STATE_TEXT:
                break;
            case STATE_SHOT:
                setImageView( ((ImageViewHolder)holder).imageView,str);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //static -> 상위클래스의 멤버변수나 객체를 사용하지 않겠라는 것을 표시
    public static class EditTextHolder extends RecyclerView.ViewHolder{
        public EditText editText;
        public EditTextHolder(View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.write_fragment_contents);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.write_fragment_imageview);
        }
    }

    public void addView(int current_state,String string) {
        if(current_state == STATE_TEXT){
            if(!mFlag) mFlag= true;
            else return;
        }
        datas.add(new WriteFragComponent(current_state,datas.size(),string));
        notifyDataSetChanged();
    }

    private void setImageView(ImageView imageView, String ImagePath){ //저장된 사진을 이미지뷰에 세팅
        Bitmap bitmap;
        int width = mContext.getResources().getDimensionPixelSize(R.dimen.Component_Write_Image_Width);
        int height = mContext.getResources().getDimensionPixelSize(R.dimen.Component_Write_Image_Width);
        BitmapImageProcess proc = new BitmapImageProcess(ImagePath,width,height);
        bitmap = proc.getResizeImage(); //메모리 효율을 위해 이미지 크기 조정
        bitmap = proc.setOrientation(bitmap); //이미지 회전
        imageView.setImageBitmap(bitmap);
    }

}

