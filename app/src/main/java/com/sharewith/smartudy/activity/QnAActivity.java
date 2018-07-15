package com.sharewith.smartudy.activity;

import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.sharewith.smartudy.fragment.QnAListFragment;
import com.sharewith.smartudy.fragment.WriteDrawFragment;
import com.sharewith.smartudy.fragment.WriteFragment;
import com.sharewith.smartudy.fragment.WritePictureFragment;
import com.sharewith.smartudy.fragment.WriteRecordFragment;
import com.sharewith.smartudy.fragment.WriteShotFragment;
import com.sharewith.smartudy.fragment.WriteTextFragment;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.CustomViewPager;
import com.sharewith.smartudy.utils.WriteFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class QnAActivity extends AppCompatActivity implements WriteDrawFragment.OnDrawFragmentListener,
        WriteRecordFragment.OnRecordFragmentListener,WritePictureFragment.OnPictureFragmentListener,
        WriteShotFragment.OnShotFragmentListener,WriteTextFragment.OnTextFragmentListener,WriteFragment.WriteFragmentListener,QnAListFragment.OnFragmentInteractionListener {

    private TabLayout mTabLayout;
    private CustomViewPager mViewPager;
    private Toolbar mToolbar;
    private WriteFragmentPagerAdapter mPagerAdapter;
    private WriteFragment mWriteFragment;
    private QnAListFragment mQnAListFragment;
    private List<Fragment> datas;

    private void setMember(){
        datas = new ArrayList<Fragment>();
        mTabLayout = findViewById(R.id.write_tablayout);
        mToolbar = findViewById(R.id.write_tooblar);
        mWriteFragment = new WriteFragment();
        mQnAListFragment = QnAListFragment.newInstance(null,null);
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
                    mViewPager.setCurrentItem(Tabcount-1,true);
                }else if(position == 0){
                    mViewPager.setCurrentItem(0,true);
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
    }

    @Override
    public void OnShotFragment() {

    }

    @Override
    public void OnDrawFragment() {

    }

    @Override
    public void OnTextFragment() {

    }

    @Override
    public void OnRecordFragment() {

    }

    @Override
    public void OnPictureFragment() {

    }

    @Override //글쓰기 프래그먼트에서 발생한 이벤트를 처리
    public void OnWriteFragment() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
