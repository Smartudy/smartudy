package com.sharewith.smartudy.activity;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sharewith.smartudy.Interface.AsyncResponse;
import com.sharewith.smartudy.adapter.WriteFragmentRecyclerAdapter;
import com.sharewith.smartudy.directory.CustomDialog;
import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.dto.WriteFragComponent;
import com.sharewith.smartudy.fragment.QnAListFragment;
import com.sharewith.smartudy.fragment.WriteFragment;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.dao.Write_DBhelper;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import java.util.ArrayList;


public class QnAActivity extends AppCompatActivity{

    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private WriteFragment mWriteFragment;
    private QnAListFragment mQnAListFragment;
    private LinearLayout mLinear;
    private AppBarLayout mAppBar;
    private Write_DBhelper DBhelper;
    private FloatingActionButton mFab,mFab2;
    private FragmentManager mFragManager;
    private WriteFragComponent mData;
    private CustomDialog mDialog;
    private String mCategory;
    private String mQuestionId;

    private String registredID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        setMember();
        setTabLayout();
        setActionBar();
        setListener();
        //DBhelper.deleteAll();
    }


    private void setMember(){
        DBhelper = new Write_DBhelper(getApplicationContext());
        mAppBar = findViewById(R.id.activity_qna_appbar);
        mTabLayout = findViewById(R.id.write_tablayout);
        mToolbar = findViewById(R.id.write_tooblar);
        mFragManager = getSupportFragmentManager();
        mWriteFragment = new WriteFragment();
        mQuestionId = getIntent().getStringExtra("grp");
        mQnAListFragment = QnAListFragment.newInstance(mQuestionId,null);
        mLinear = findViewById(R.id.qna_linear);
        mFab = findViewById(R.id.activity_qna_fab);
        mFab2 = findViewById(R.id.activity_qna_fab2);
        mFab2.setVisibility(View.GONE);
        mFab2.setClickable(false);
        mFragManager.beginTransaction().add(R.id.activity_qna_container,mWriteFragment)
                .addToBackStack(null).hide(mWriteFragment)
                .add(R.id.activity_qna_container,mQnAListFragment).addToBackStack(null).commit();
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        mDialog = new CustomDialog(this, CustomDialog.ANSWER, display,metrics,Constant.PostAnswerURL);
        mCategory = getIntent().getStringExtra("category");
    }

    private void setTabLayout(){
        mTabLayout.addTab(mTabLayout.newTab().setText("질문"));
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

    private void toggleFab(){
        mFab.setVisibility(View.GONE);
        mFab.setClickable(false);
        mFab2.setVisibility(View.VISIBLE);
        mFab2.setClickable(true);
        int last = mTabLayout.getTabCount()-1;
        if(last != 0 && mTabLayout.getSelectedTabPosition() == last) {
            mLinear.setVisibility(View.INVISIBLE);
            mFragManager.beginTransaction().hide(mQnAListFragment).commit();
            mFragManager.beginTransaction().show(mWriteFragment).commit();
            mFab2.setVisibility(View.VISIBLE);
            mFab2.setClickable(true);
        }else {
            mLinear.setVisibility(View.VISIBLE);
            mFragManager.beginTransaction().show(mQnAListFragment).commit();
            mFragManager.beginTransaction().hide(mWriteFragment).commit();
            mFab2.setVisibility(View.GONE);
            mFab2.setClickable(false);
        }
    }

    public void afterAnswerPost(){
        mFragManager.beginTransaction().remove(mWriteFragment).commit();
        mTabLayout.removeTabAt(mTabLayout.getTabCount()-1);
        toggleFab();
        mFab.setVisibility(View.VISIBLE);
        mFab.setClickable(true);
        Log.d("temp","등록된 아이디 " +registredID);
        mQnAListFragment.getAnswerAfterPost(registredID);
    }

    private void setListener(){
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        toggleFab();
                        break;
                    case 1:
                        toggleFab();
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabLayout.addTab(mTabLayout.newTab().setText("답변 작성중"),true);
            }
        });
        mFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //답변 등록 버튼
                WriteFragComponent.builder builder = mWriteFragment.getDatas();//글작성창에 있는 제목,본문,이미지,음악파일 경로 가져옴.
                builder.setCategory(mCategory).setGrp(mQuestionId);
                mDialog.setBuilder(builder);//나머지 금액,과목,해시태그 설정을 위해
                mDialog.showDialog();
               }
        });
    }

    public String getRegistredID() {
        return registredID;
    }

    public void setRegistredID(String registredID) {
        this.registredID = registredID;
    }


}
