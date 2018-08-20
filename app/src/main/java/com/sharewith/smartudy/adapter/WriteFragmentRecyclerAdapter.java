package com.sharewith.smartudy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.sharewith.smartudy.dto.WriteRecyclerDto;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.BitmapImageProcess;
import com.sharewith.smartudy.dto.WriteRecyclerDto;
import com.sharewith.smartudy.utils.RecordSeekbar;

import java.util.ArrayList;

/**
 * Created by Simjae on 2018-07-20.
 */

public class WriteFragmentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public static final int STATE_TITLE = 0;
    public static final int STATE_TEXT = 1;
    public static final int STATE_PICTURE = 2;
    public static final int STATE_RECORD = 3;
    public static final int STATE_DRAW = 4;
    private EditTextHolder mTextHolder;
    //상수에 static final을 사용하는 이유
    //https://djkeh.github.io/articles/Why-should-final-member-variables-be-conventionally-static-in-Java-kor/
    private ArrayList<WriteRecyclerDto> datas;
    private boolean mFlag; //텍스트 입력 딱 한번만 하게 하기 위해서

    public ArrayList<WriteRecyclerDto> getDatas() { //path는 이미 설정되어있으므로 본문만 읽어옴.
        for(WriteRecyclerDto c : datas){
            if(c.getType() == STATE_TEXT)
                c.setStr(mTextHolder.editText.getText().toString());
        }
        return datas;
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    public WriteFragmentRecyclerAdapter(Context context) {
        mContext = context;
        mFlag = false;
        datas = new ArrayList<WriteRecyclerDto>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch(viewType){
            case STATE_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_write_text,parent,false);
                return mTextHolder = new EditTextHolder(view);
            case STATE_PICTURE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_write_image,parent,false);
                return new ImageViewHolder(view);
            case STATE_RECORD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_write_record,parent,false);
                return new RecordViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = datas.get(position).getType();
        String str = datas.get(position).getStr();
        switch(type){
            case STATE_TEXT:
                break;
            case STATE_PICTURE:
                setImageView( ((ImageViewHolder)holder).imageView,str);
                break;
            case STATE_RECORD:
                setRecordView( ((RecordViewHolder)holder).seekbar,((RecordViewHolder)holder).playbutton,str);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //static -> 상위클래스의 인스턴스가 생성되어 있지 않아도 독자적으로 클래스 생성 가능.
    public static class EditTextHolder extends RecyclerView.ViewHolder{
        public static EditText editText;
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
    public static class RecordViewHolder extends RecyclerView.ViewHolder{
        public SeekBar seekbar;
        public ImageView playbutton;
        public RecordViewHolder(View itemView) {
            super(itemView);
            seekbar = itemView.findViewById(R.id.seekbar_write_audio);
            playbutton = itemView.findViewById(R.id.play_button);
        }
    }

    public void addView(int current_state,String string) {
        if(current_state == STATE_TEXT) {
            if (!mFlag) mFlag = true;
            else return;
        }
        datas.add(new WriteRecyclerDto(current_state,string));
        notifyDataSetChanged();
    }

    private void setRecordView(final SeekBar seekbar,ImageView playbutton,final String uri){
            playbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecordSeekbar player = new RecordSeekbar(mContext, Uri.parse(uri),seekbar);
                    player.execute(); //자동으로 음악파일에 맞춰 시크바 갱신
                }
            });
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

