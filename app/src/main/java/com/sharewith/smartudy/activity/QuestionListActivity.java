package com.sharewith.smartudy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharewith.smartudy.directory.QuestionRecyclerAdapter;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.Question;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    private static String[] suggestions = new String[]{"abcdefg","bcdefgh","cdefghi","defghij","efghijk"};
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(categoryName);


        // 자동완성 검색 창 관련 속성 설정
        setAutoCompleteTextViewAttrs();

        // 플로팅버튼(하단 더하기버튼)의 리스너 등록
        setFloatingActionBtnClickListener();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        List<Question> questionList = new ArrayList<Question>();
        collectQuestionDataFromDB(categoryName, questionList); // DB에서 해당 과목의 모든 질문을 받아온다.(일단은 임의 데이터)
        recyclerView.setAdapter(new QuestionRecyclerAdapter(questionList, R.layout.question_row_layout));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void collectQuestionDataFromDB(String categoryName, List<Question> list){
        for(int i=0;i<10;i++){
            Question question=new Question(
                    "배열수식 도와주세요",
                    "구글링해서 어떤 IR Table을 찾아봐도 \n아세틸기의 wavenum ber는 ",
                    "#재료공학 #건축공학 #환경공학",
                    12,
                    new Date()
            );
            list.add(question);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quesiont_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //case android.R.id.home:

            case R.id.menu_filter_setting:
                //Toast.makeText(this, "filter", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void setAutoCompleteTextViewAttrs(){
        AutoCompleteTextView actv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        actv.setAdapter(adapter);
        actv.setThreshold(1);
    }
    public void setFloatingActionBtnClickListener(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
