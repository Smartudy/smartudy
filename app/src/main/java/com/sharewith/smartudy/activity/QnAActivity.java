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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sharewith.smartudy.Interface.AsyncResponse;
import com.sharewith.smartudy.adapter.WriteFragmentRecyclerAdapter;
import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.dto.WriteFragComponent;
import com.sharewith.smartudy.fragment.QnAListFragment;
import com.sharewith.smartudy.fragment.WriteFragment;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.dao.Write_DBhelper;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import java.util.ArrayList;


public class QnAActivity extends AppCompatActivity implements WriteFragment.WriteFrag_To_QnAActivity{

    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private WriteFragment mWriteFragment;
    private QnAListFragment mQnAListFragment;
    private LinearLayout mLinear;
    private AppBarLayout mAppBar;
    private Write_DBhelper DBhelper;
    private FloatingActionButton mFab,mFab2;
    private FragmentManager mFragManager;

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
        mWriteFragment = new WriteFragment();
        mQnAListFragment = QnAListFragment.newInstance(null,null);
        mLinear = findViewById(R.id.qna_linear);
        mFab = findViewById(R.id.activity_qna_fab);
        mFab2 = findViewById(R.id.activity_qna_fab2);
        mFab2.setVisibility(View.GONE);
        mFab2.setClickable(false);
        mFragManager = getSupportFragmentManager();
        mFragManager.beginTransaction().add(R.id.activity_qna_container,mWriteFragment).addToBackStack(null).hide(mWriteFragment)
                .add(R.id.activity_qna_container,mQnAListFragment).addToBackStack(null).commit();

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
        switch(mTabLayout.getSelectedTabPosition()){
            case 0:
                mLinear.setVisibility(View.VISIBLE);
                mFragManager.beginTransaction().show(mQnAListFragment).commit();
                mFragManager.beginTransaction().hide(mWriteFragment).commit();
                mFab2.setVisibility(View.GONE);
                mFab2.setClickable(false);
                break;
            case 1:
                mLinear.setVisibility(View.GONE);
                mFragManager.beginTransaction().show(mWriteFragment).commit();
                mFragManager.beginTransaction().hide(mQnAListFragment).commit();
                mFab2.setVisibility(View.VISIBLE);
                mFab2.setClickable(true);
                break;
        }
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
            public void onClick(View v) {
                ArrayList<WriteFragComponent> datas = mWriteFragment.getDatas();
                String result = "";
                try {
                    HttpUtils util = HttpUtils.getInstance(HttpUtils.MULTIPART, null, Constant.PostURL, getApplicationContext(), datas);
                    util.setDelegate(new AsyncResponse() {
                        @Override
                        public void aftermultipart(String result) { //HttpUtils의 doInBackground() 마치면 자동으로 호출되는 메소드
                            Log.d("qna",result);
                            JsonParser parser = new JsonParser();
                            JsonObject json = parser.parse(result).getAsJsonObject();
                            Log.d("qna",json.toString());
                            if(json.get("success").getAsBoolean() == true){
                                Toast.makeText(getApplicationContext(), "게시글이 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                                mFragManager.beginTransaction().remove(mWriteFragment).commit();
                                mTabLayout.removeTabAt(1);
                            }else{
                                Toast.makeText(getApplicationContext(), "게시글 등록이 실패 하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    util.execute();
                    //결과는 HttpUtils의 onPostExecute()에서 알아서 aftermultipart() 호출함.
                }catch(Exception e){
                    Log.d("QnAActivity","멀티파트 요청 중 에러 발생");
                    e.printStackTrace();
                }


            }
        });
    }


    @Override
    public void addNotePad(NotePadDto notepad) {
        mQnAListFragment.addNotePad(notepad);
        //WriteFragment에서 글 입력시 데이터 변화를 QnAListFragment에 있는 리싸이클러뷰의 어댑터에 알려야함.
        //WriteFragment->QnAActivity->QnAListFragment 순서로 통신하게끔 중간에 액티비티가 껴있음.
        //프래그먼트들끼리 바로 통신 불가능, 중간에 액티비티가 무조건 껴야함.
    }


}
