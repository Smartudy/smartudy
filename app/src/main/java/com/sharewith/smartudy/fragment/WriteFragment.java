package com.sharewith.smartudy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.sharewith.smartudy.activity.QnAActivity;
import com.sharewith.smartudy.adapter.WriteFragmentRecyclerAdapter;
import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.dao.Write_DBhelper;

import java.util.ArrayList;

public class WriteFragment extends Fragment{

    private CoordinatorLayout mFragmentWrite;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private WriteFragmentListener mQnAActivity;
    private FloatingActionButton main_fab;
    private Animation fab_open_anim,fab_close_anim,fab_rotate_forward,fab_rotate_backward;
    private ImageView bottom1,bottom2,bottom3,bottom4,bottom5;
    private Write_DBhelper mDBhelper;
    private RecyclerView mRecycler;
    private EditText mTitle;
    private boolean is_fabopen;

    public static interface WriteFrag_To_QnAActivity{
        public void addNotePad(NotePadDto notepad);
        //WriteFragment의 + 버튼 눌렀을때 질문&답변창에 업데이트 하기 위해서
    }

    @Override
    public void onAttach(Context context) {
        try{
            mQnAActivity = (WriteFragmentListener)context;
            //만약 Fragment와 연결된 액티비티(context)에서 WriteFragmentListener를 구현하지 않았다면 여기서 예외 발생
        }catch(ClassCastException e){
            Log.d("Log",context+"'s Listener is not yet implemented!!");
        }
        super.onAttach(context);
    }

    public static interface WriteFragmentListener{
//         void setTransParentBackground();
//         void UnsetTransParentBackground();
    }

    private void setMember(View view){
        mTitle = view.findViewById(R.id.write_title);
        mRecycler = view.findViewById(R.id.write_fragment_recycler);
        mRecycler.setAdapter(new WriteFragmentRecyclerAdapter(getContext()));
        mDBhelper = new Write_DBhelper(view.getContext());
//        mFragmentWrite = view.findViewById(R.id.fragment_write);
//        mFragmentWrite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(is_fabopen){
//                    animation();
//                }
//            }
//        });
        fm = getActivity().getSupportFragmentManager();
        main_fab = view.findViewById(R.id.main_write_fab);
        fab_open_anim = AnimationUtils.loadAnimation(getContext(),R.anim.fab_open);
        fab_close_anim = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        fab_rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);
        fab_rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        bottom1 = view.findViewById(R.id.write_fragment_bottom1);
        bottom2 = view.findViewById(R.id.write_fragment_bottom2);
        bottom3 = view.findViewById(R.id.write_fragment_bottom3);
        bottom4 = view.findViewById(R.id.write_fragment_bottom4);
        bottom5 = view.findViewById(R.id.write_fragment_bottom5);
        is_fabopen = true;
    }

    private void setListener(){
        main_fab.setOnClickListener(onClickListener);
        bottom1.setOnClickListener(onClickListener);
        bottom2.setOnClickListener(onClickListener);
        bottom3.setOnClickListener(onClickListener);
        bottom4.setOnClickListener(onClickListener);
        bottom5.setOnClickListener(onClickListener);
    }
//    private void setFAB(View view){
//        mfab1.setOnClickListener(onClickListener);
//        mfab2.setOnClickListener(onClickListener);
//        mfab3.setOnClickListener(onClickListener);
//        mfab4.setOnClickListener(onClickListener);
//        background.setBackgroundColor(Color.parseColor("#994C4C4C"));
//        animation();
//    }

    private String getText(){
        String contents = "";
        for(int i=0; i<mRecycler.getChildCount(); i++){
            RecyclerView.ViewHolder viewHolder = mRecycler.findViewHolderForAdapterPosition(i);
            if(viewHolder instanceof WriteFragmentRecyclerAdapter.TextViewHolder)
                contents += ((WriteFragmentRecyclerAdapter.TextViewHolder) viewHolder).editText.getText().toString();
        }
        return contents;
    }

    private void setNotePad(NotePadDto notepad){ //디비에 업데이트 할때 사용됨.
        String title = mTitle.getText().toString();
        String contents = getText();
        notepad.setTitle(title);
        notepad.setContents(contents);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.main_write_fab:
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(),0);
                    //키보드 내리기
                    NotePadDto notepad = new NotePadDto();
                    setNotePad(notepad); //WriteFragment에 있는 모든 정보를 긁어와서 notepad객체에 설정
                    //디비 업데이트
                    mDBhelper.insertNotePad(notepad);
                    ((QnAActivity)getActivity()).addNotePad(notepad);
                    //QnAListFragment's 리싸이클러뷰 어댑터 갱신
                    //WriteFragment->QnAActivity->QnAListFragment순서로 전달됨.
                    break;
                case R.id.write_fragment_bottom1:
                    ((WriteFragmentRecyclerAdapter)mRecycler.getAdapter()).addView(WriteFragmentRecyclerAdapter.STATE_TEXT);
                    mRecycler.smoothScrollToPosition(mRecycler.getChildCount());
                    break;
            }
        }
    };

//    public void animation(){
//            if(is_fabopen){//닫으려고 할때
//                main_fab.startAnimation(fab_rotate_backward);
//                mfab1.startAnimation(fab_close_anim);
//                mfab2.startAnimation(fab_close_anim);
//                mfab3.startAnimation(fab_close_anim);
//                mfab4.startAnimation(fab_close_anim);
//                mfab1.setClickable(false);mfab2.setClickable(false);
//                mfab3.setClickable(false);mfab4.setClickable(false);
//                background.setVisibility(View.GONE);
//                is_fabopen = false;
//            }else{//열려고 할때
//                main_fab.startAnimation(fab_rotate_forward);
//                mfab1.startAnimation(fab_open_anim);
//                mfab2.startAnimation(fab_open_anim);
//                mfab3.startAnimation(fab_open_anim);
//                mfab4.startAnimation(fab_open_anim);
//                mfab1.setClickable(true);mfab2.setClickable(true);
//                mfab3.setClickable(true);mfab4.setClickable(true);
//                background.setVisibility(View.VISIBLE);
//                is_fabopen = true;
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write,container,false);
        setMember(view);
        setListener();
       super.onCreateView(inflater, container, savedInstanceState);
       return view;
    }


}
