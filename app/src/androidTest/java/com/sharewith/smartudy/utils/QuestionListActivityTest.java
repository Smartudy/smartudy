package com.sharewith.smartudy.utils;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sharewith.smartudy.Interface.AsyncResponse;
import com.sharewith.smartudy.activity.QnAActivity;
import com.sharewith.smartudy.activity.QuestionListActivity;
import com.sharewith.smartudy.adapter.QuestionRecyclerAdapter;
import com.sharewith.smartudy.dto.Question;
import com.sharewith.smartudy.smartudy.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Simjae on 2018-08-18.
 */

@RunWith(AndroidJUnit4.class)
public class QuestionListActivityTest {

    @Rule
    public ActivityTestRule<QuestionListActivity> activityTestRule =
            new ActivityTestRule<QuestionListActivity>(QuestionListActivity.class);
    private Activity activity = null;
    private FragmentManager fm = null;
    private RecyclerView mRecyclerView = null;
    private int nowPage = 0;

    @Before
    public void init(){
        activity = activityTestRule.getActivity();
        fm = activity.getFragmentManager();
        mRecyclerView = activity.findViewById(R.id.recyclerView);
    }

    @Test
    public void test(){

        List<Question> questionList = new ArrayList<Question>();
        final QuestionRecyclerAdapter adapter = new QuestionRecyclerAdapter(questionList,mRecyclerView);
        adapter.setOnLoadMoreListener(new QuestionRecyclerAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                HashMap<String,String> arg = new HashMap<>();
                arg.put("page",String.valueOf(nowPage));
                Log.d("QuestionListActivity","현재 요청 페이지는 : "+nowPage);
                HttpUtils utils = new HttpUtils(HttpUtils.GET,arg, Constant.ListPageURL,activity.getApplicationContext());// /board/listpage/?page=0
                utils.setDelegate(new AsyncResponse() {
                    @Override
                    public void getAsyncResponse(String result) {
                        //Gson gson = new Gson();
                        //데이터 서버로부터 받아온 다음에 어댑터에 데이터 추가하고, notifydatasetchanged()실행
                        adapter.setLoaded();
                        nowPage += QuestionRecyclerAdapter.visibleThreshold;
                    }
                });
                utils.execute();

            }
        });
        adapter.setFirstData(); //onLoadMore();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext()));

    }

}