package com.sharewith.smartudy.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sharewith.smartudy.Interface.AsyncResponse;
import com.sharewith.smartudy.adapter.QuestionRecyclerAdapter;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.dto.Question;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    private static String[] suggestions = new String[]{"abcdefg","bcdefgh","cdefghi","defghij","efghijk"};
    private RecyclerView mRecyclerView;
    private String mCategory;
    protected String resourceSizeTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Intent intent = getIntent();
        mCategory = intent.getStringExtra("categoryName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView title =  findViewById(R.id.question_list_title);
        title.setText(mCategory);
        Log.d("QuestionListActivity","현재 카테고리는 "+mCategory);

        // 자동완성 검색 창 관련 속성 설정
        setAutoCompleteTextViewAttrs();
        // 플로팅버튼(하단 더하기버튼)의 리스너 등록
        setFloatingActionBtnClickListener();

        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Question> questionList = new ArrayList<Question>();
        final QuestionRecyclerAdapter adapter = new QuestionRecyclerAdapter(questionList,mRecyclerView,mCategory);
        adapter.setOnLoadMoreListener(new QuestionRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page) {
                HashMap<String,String> arg = new HashMap<>();
                arg.put("page",String.valueOf(page));
                arg.put("category",mCategory);
                Log.d("QuestionListActivity",mCategory+"의 "+String.valueOf(page)+"번 페이지 서버로 요청");
                HttpUtils utils = new HttpUtils(HttpUtils.GET,arg, Constant.ListPageURL,getApplicationContext());// /board/listpage/?page=0
                utils.setDelegate(new AsyncResponse() { //HttpUtils가 작업을 끝냈을때
                    @Override
                    public void getAsyncResponse(String result) {
                        //서버로부터 데이터 받아서 어댑터에 데이터 추가
                        if(result == null) return;
                        Log.d("QuestionListActivity",result);
                        JsonParser parser = new JsonParser();
                        List<Question> datas;
                        try {
                            JsonObject root = parser.parse(result).getAsJsonObject();
                            JsonElement success = root.get("success");
                            if(success.getAsBoolean()){
                                JsonArray arr = (JsonArray)root.get("datas");
                                Gson gson = new Gson();
                                Question[] qarr = gson.fromJson(arr,Question[].class);
                                datas = Arrays.asList(qarr);
                                adapter.addDatas(datas);
                            }else{
                               Log.d("QuestionListActivity","서버에서 질문 목록 받아오기 실패");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapter.setLoaded();
                    }
                });
                utils.execute(); //서버에서 질문 리스트 가져오기

            }
        });
        adapter.setFirstData();//onLoadMore() 메소드 실행됨.
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(layoutResID, null);
        resourceSizeTag = (String)view.getTag();
        super.setContentView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quesiont_list, menu);
        //TODO: 더 다양한 화면 크기 지원시 옵션을 늘려야한다.
        if(resourceSizeTag.equals("large")){
            menu.findItem(R.id.menu_filter_setting).setIcon(resizeImage(R.mipmap.rectangle_3,dpToPx(35),dpToPx(35)));
        }
        return super.onCreateOptionsMenu(menu);
    }

    public int dpToPx(int sizeInDP){
        int pxVal = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources().getDisplayMetrics()
        );
        return pxVal;
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
                Intent intent = new Intent(QuestionListActivity.this,QnAActivity.class);
                intent.putExtra("category",mCategory);
                startActivity(intent);
            }
        });
    }

    private Drawable resizeImage(int resId, int w, int h)
    {
        // load the origial Bitmap
        Bitmap BitmapOrg = BitmapFactory.decodeResource(getResources(), resId);
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0,width, height, matrix, true);
        return new BitmapDrawable(QuestionListActivity.this.getResources(), resizedBitmap);
    }
}
