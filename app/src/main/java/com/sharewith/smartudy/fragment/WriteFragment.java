package com.sharewith.smartudy.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.sharewith.smartudy.fragment.WriteDrawFragment;
import com.sharewith.smartudy.fragment.WritePictureFragment;
import com.sharewith.smartudy.fragment.WriteRecordFragment;
import com.sharewith.smartudy.fragment.WriteShotFragment;
import com.sharewith.smartudy.fragment.WriteTextFragment;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.WriteFragmentPagerAdapter;

import java.util.List;

public class WriteFragment extends Fragment{

    private WriteTextFragment mFrag_text;
    private WriteShotFragment mFrag_shot;
    private WritePictureFragment mFrag_picture;
    private WriteRecordFragment mFrag_record;
    private WriteDrawFragment mFrag_draw;
    private RelativeLayout mBottom1;
    private RelativeLayout mBottom2;
    private RelativeLayout mBottom3;
    private RelativeLayout mBottom4;
    private RelativeLayout mBottom5;
    private LinearLayout mBottomBar;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private WriteFragmentListener mListener;

    @Override
    public void onAttach(Context context) {
        try{
            mListener = (WriteFragmentListener)context;
            //만약 Fragment와 연결된 액티비티(context)에서 WriteFragmentListener를 구현하지 않았다면 여기서 예외 발생
        }catch(ClassCastException e){
            Log.d("Log",context+"'s Listener is not yet implemented!!");
        }
        super.onAttach(context);
    }

    public static interface WriteFragmentListener{
         void OnWriteFragment();
    }

    private void setMember(View view){
        fm = getActivity().getSupportFragmentManager();
        mFrag_text = new WriteTextFragment();
        mFrag_shot = new WriteShotFragment();
        mFrag_picture = new WritePictureFragment();
        mFrag_record = new WriteRecordFragment();
        mFrag_draw = new WriteDrawFragment();
        mBottom1 = view.findViewById(R.id.bottom_content1);
        mBottom2 = view.findViewById(R.id.bottom_content2);
        mBottom3 = view.findViewById(R.id.bottom_content3);
        mBottom4 = view.findViewById(R.id.bottom_content4);
        mBottom5 = view.findViewById(R.id.bottom_content5);
    }

    private void initFragment(){ //맨처음에 보여질 프래그먼트를 지정함.
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.write_content_frame,mFrag_text);
        ft.addToBackStack(null);
        ft.commit();
    }

    View.OnClickListener BottomBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction ft = fm.beginTransaction();
            switch(v.getId()){
                case R.id.bottom_content1:
                    ft.replace(R.id.write_content_frame,mFrag_text);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;
                case R.id.bottom_content2:
                    ft.replace(R.id.write_content_frame,mFrag_shot);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;
                case R.id.bottom_content3:
                    ft.replace(R.id.write_content_frame,mFrag_picture);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;
                case R.id.bottom_content4:
                    ft.replace(R.id.write_content_frame,mFrag_record);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;
                case R.id.bottom_content5:
                    ft.replace(R.id.write_content_frame,mFrag_draw);
                    ft.addToBackStack(null);
                    ft.commit();
                    break;
            }
        }
    };

    private void setBottomListener(){
        mBottom1.setOnClickListener(BottomBarListener);
        mBottom2.setOnClickListener(BottomBarListener);
        mBottom3.setOnClickListener(BottomBarListener);
        mBottom4.setOnClickListener(BottomBarListener);
        mBottom5.setOnClickListener(BottomBarListener);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write,container,false);
        setMember(view);
        setBottomListener();
        initFragment();
       super.onCreateView(inflater, container, savedInstanceState);
       return view;
    }


}
