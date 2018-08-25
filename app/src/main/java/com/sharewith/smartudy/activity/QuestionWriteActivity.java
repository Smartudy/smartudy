package com.sharewith.smartudy.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sharewith.smartudy.dao.Write_DBhelper;
import com.sharewith.smartudy.directory.CustomDialog;
import com.sharewith.smartudy.dto.WriteFragComponent;
import com.sharewith.smartudy.fragment.QnAListFragment;
import com.sharewith.smartudy.fragment.WriteFragment;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.Constant;

public class QuestionWriteActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private CustomDialog mDialog;
    private TextView okbtn;
    private WriteFragment mWriteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_write);
        setMember();
        setActionBar();
        setListener();
    }

    private void setMember(){
        mToolbar = findViewById(R.id.question_write_toolbar);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        mDialog = new CustomDialog(this,CustomDialog.QUESTION,display,metrics, Constant.PostQuestionURL);
        okbtn = findViewById(R.id.question_write_ok);
        mWriteFragment = (WriteFragment)getSupportFragmentManager().findFragmentById(R.id.question_write_fragment);
    }

    private void setListener() {
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteFragComponent.builder builder = mWriteFragment.getDatas();
                builder.setCategory(getIntent().getStringExtra("category"));
                mDialog.setBuilder(builder);//글작성창에 있는 제목,본문,이미지,음악파일 경로 가져옴.
                mDialog.showDialog();
            }
        });
    }

    private void setActionBar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setDisplayShowTitleEnabled(false);
    }
}
