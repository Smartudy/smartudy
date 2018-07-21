package com.sharewith.smartudy.activity;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.fragment.QnAListFragment;
import com.sharewith.smartudy.fragment.WriteFragment;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.CustomViewPager;
import com.sharewith.smartudy.adapter.WriteFragmentPagerAdapter;
import com.sharewith.smartudy.dao.Write_DBhelper;

import java.util.ArrayList;
import java.util.List;

public class QnAActivity extends AppCompatActivity implements WriteFragment.WriteFrag_To_QnAActivity{

    private TabLayout mTabLayout;
    private CustomViewPager mViewPager;
    private Toolbar mToolbar;
    private WriteFragmentPagerAdapter mPagerAdapter;
    private WriteFragment mWriteFragment;
    private QnAListFragment mQnAListFragment;
    private List<Fragment> datas;
    private LinearLayout mLinear;
    private AppBarLayout mAppBar;
    private Write_DBhelper DBhelper;
    private void setMember(){
        DBhelper = new Write_DBhelper(getApplicationContext());
        datas = new ArrayList<Fragment>();
        mAppBar = findViewById(R.id.activity_qna_appbar);
        mTabLayout = findViewById(R.id.write_tablayout);
        mToolbar = findViewById(R.id.write_tooblar);
        mWriteFragment = new WriteFragment();
        mQnAListFragment = QnAListFragment.newInstance(null,null);
        mLinear = findViewById(R.id.qna_linear);

    }

    private void setTabLayout(){
        mTabLayout.addTab(mTabLayout.newTab().setText("질문"));
        mTabLayout.addTab(mTabLayout.newTab().setText("답변1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("답변2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("답변 작성중"));
    }

    private void setActionBar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setDisplayShowTitleEnabled(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.parseColor("#3e8cff"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

    private void setViewPager(){
        mViewPager = findViewById(R.id.write_viewpager);
        mViewPager.setPagingEnabled(false); //뷰페이저 수평 스크롤 금지
        datas.add(mQnAListFragment);
        datas.add(mWriteFragment);
        mPagerAdapter = new WriteFragmentPagerAdapter(getSupportFragmentManager(),datas,mTabLayout.getTabCount());
        mViewPager.setAdapter(mPagerAdapter);
    }


    private void setListener(){
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //탭 선택시 뷰페이저에게 알리기 위한 용도의 리스너
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                int Tabcount = mTabLayout.getTabCount();
                if(position==Tabcount-1){
                    mLinear.setVisibility(View.GONE);
                    mViewPager.setCurrentItem(Tabcount-1,true);
                }else{
                    mLinear.setVisibility(View.VISIBLE);
                    mViewPager.setCurrentItem(0,true);
                    mQnAListFragment.scroll(position);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        setMember();
        setTabLayout();
        setActionBar();
        setViewPager();
        setListener();
        DBhelper.deleteAll();
    }


    @Override
    public void addNotePad(NotePadDto notepad) {
        mQnAListFragment.addNotePad(notepad);
        //WriteFragment에서 글 입력시 데이터 변화를 QnAListFragment에 있는 리싸이클러뷰의 어댑터에 알려야함.
        //WriteFragment->QnAActivity->QnAListFragment 순서로 통신하게끔 중간에 액티비티가 껴있음.
        //프래그먼트들끼리 바로 통신 불가능, 중간에 액티비티가 무조건 껴야함.
    }

//    @Override
//    public void setTransParentBackground() {
//    }
//
//    @Override
//    public void UnsetTransParentBackground() {
//    }
}
