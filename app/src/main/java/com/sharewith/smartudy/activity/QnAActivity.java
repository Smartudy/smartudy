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
        mQuestionId = getIntent().getStringExtra("grp");
        mQnAListFragment = QnAListFragment.newInstance(mQuestionId,null);
        mLinear = findViewById(R.id.qna_linear);
        mFab = findViewById(R.id.activity_qna_fab);
        mFab2 = findViewById(R.id.activity_qna_fab2);
        mFab2.setVisibility(View.GONE);
        mFab2.setClickable(false);
        mFragManager = getSupportFragmentManager();
        mFragManager.beginTransaction().add(R.id.activity_qna_container,mWriteFragment).addToBackStack(null).hide(mWriteFragment)
                .add(R.id.activity_qna_container,mQnAListFragment).addToBackStack(null).commit();
        mDialog = new CustomDialog(this);
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
        switch(mTabLayout.getSelectedTabPosition()){
            case 0:
                mLinear.setVisibility(View.VISIBLE);
                mFragManager.beginTransaction().show(mQnAListFragment).commit();
                mFragManager.beginTransaction().hide(mWriteFragment).commit();
                mFab2.setVisibility(View.GONE);
                mFab2.setClickable(false);
                break;
            case 1:
                mLinear.setVisibility(View.INVISIBLE);
                mFragManager.beginTransaction().hide(mQnAListFragment).commit();
                mFragManager.beginTransaction().show(mWriteFragment).commit();
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
                mDialog.showDialog();
               }
        });
    }

    public void postToServer(){ //작성된 글의 내용(다이얼로그 포함)을 서버로 전송
        String result = "";
        WriteFragComponent.builder builder = mWriteFragment.getDatas(); //글작성창에 있는 제목,본문,이미지,음악파일 경로 가져옴.
        mDialog.setDialogContents(builder); //다이얼로그의 과목명,해쉬태그,금액까지 취합.
        Log.d("QnAActivity","질문의 아이디는 "+mQuestionId);
        mData = builder.setCategory(mCategory).setGrp(mQuestionId).build(); // 카테고리명 까지 취합.
        Log.d("QnAActivity","현재 카테고리 " + mCategory);
        Log.d("QnAActivity",mData.toString());
        try {
            HttpUtils util = new HttpUtils(HttpUtils.MULTIPART, null, Constant.PostAnswerURL, getApplicationContext());
            util.setMultipartdata(mData);
            util.setDelegate(new AsyncResponse() {
                @Override
                public void getAsyncResponse(String result) { //HttpUtils의 doInBackground() 마치면 자동으로 호출되는 메소드
                    Log.d("qna",result);
                    JsonParser parser = new JsonParser();
                    JsonObject json = parser.parse(result).getAsJsonObject();
                    Log.d("qna",json.toString());
                    if(json.get("success").getAsBoolean() == true){
                        Toast.makeText(getApplicationContext(), "게시글이 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                        mFragManager.beginTransaction().remove(mWriteFragment).commit();
                        mTabLayout.removeTabAt(1);
                        mDialog.dismiss();
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
    private void showdialog(){
//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
//        int width = dm.widthPixels; //디바이스 화면 너비
//        int height = dm.heightPixels; //디바이스 화면 높이
//
//        dial = (Button) findViewById(R.id.dial);
//        cd = new CustomDialog(this);
//        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
//        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
//        wm.width = width / 2;  //화면 너비의 절반
//        wm.height = height / 2;  //화면 높이의 절반
//        dial.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cd.show();  //다이얼로그
//            }
//        });
    }


}
