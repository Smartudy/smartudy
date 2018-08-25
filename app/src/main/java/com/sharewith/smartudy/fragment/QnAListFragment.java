package com.sharewith.smartudy.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sharewith.smartudy.Interface.AsyncResponse;
import com.sharewith.smartudy.adapter.QnAListAdapter;
import com.sharewith.smartudy.dto.Answer;
import com.sharewith.smartudy.dto.NotePadDto;
import com.sharewith.smartudy.dto.Question_Selected;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.dao.Write_DBhelper;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class QnAListFragment extends Fragment { //뷰페이저의 1페이지에 해당하는 리싸이클러뷰
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecyclerView;
    private String mParam1;
    private String mParam2;


    Write_DBhelper DBhelper;

    public QnAListFragment() {
    }

    public static QnAListFragment newInstance(String param1, String param2) {
        QnAListFragment fragment = new QnAListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setMember(View view){
        mRecyclerView = view.findViewById(R.id.qna_recycler_view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qna_list, container, false);
        setMember(view);
        Question_Selected mSelectedQuestion = getQuestionFromServer();
        ArrayList<Answer> answers = getAnswersFromServer();
        QnAListAdapter adapter = new QnAListAdapter(getContext(),mSelectedQuestion,answers);
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    public Question_Selected getQuestionFromServer(){
        HashMap<String,String> map = new HashMap<>();
        Question_Selected q = new Question_Selected();
        map.put("id",mParam1);
        Log.d("QnAListFragment","질문글의 아이디는 " + mParam1);
        HttpUtils utils = new HttpUtils(HttpUtils.GET,map, Constant.GetQuestionURL,getContext());
        try {
            String result = utils.execute().get();
            Gson gson = new Gson();
            q = gson.fromJson(result,Question_Selected.class);
            Log.d("temp",q.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return q;
    }

    public ArrayList<Answer> getAnswersFromServer(){ //처음 답변 목록 가져올때 사용
        HashMap<String,String> map = new HashMap<>();
        ArrayList<Answer> answers = null;
        map.put("grp",mParam1);
        HttpUtils utils = new HttpUtils(HttpUtils.GET,map, Constant.GetAnswersURL,getContext());
        try {
            String result = utils.execute().get();
            Log.d("QnAListFragment",result+"서버로 부터 수신");
            JsonObject root = (JsonObject)new JsonParser().parse(result);
            Gson gson = new Gson();
            if(root.get("success").getAsBoolean() == true) {
                JsonArray arr = root.get("datas").getAsJsonArray();
                answers = gson.fromJson(arr,new TypeToken<ArrayList<Answer>>(){}.getType());
                for (int i = 0; i < answers.size(); i++)
                    Log.d("QnAListFragment", answers.get(i).getTitle() + " 답변을 서버로 부터 수신함");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return answers;
    }

    public void getAnswerAfterPost(String AnswerID){ //답변 등록후 등록된 답변 가져오기 위해 사용
        HashMap<String,String> map = new HashMap<>();
        map.put("id",AnswerID);
        HttpUtils utils = new HttpUtils(HttpUtils.GET,map, Constant.GetAnswerURL,getContext());
        try {
            String result = utils.execute().get();
            Log.d("QnAListFragment","서버로 부터 수신됨 : "+result);
            if(result != null){
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);
                JsonObject object = element.getAsJsonObject();
                if(object.get("success").getAsBoolean() == true){
                    JsonElement e = object.get("data");
                    Answer answer = new Gson().fromJson(e,Answer.class);
                    ((QnAListAdapter)mRecyclerView.getAdapter()).add(answer);
                }else{
                    Log.d("QnAListFragment","등록된 답변 가져오기 실패");
                    return;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void scroll(int position){
        LinearLayoutManager llm = (LinearLayoutManager)mRecyclerView.getLayoutManager();
        if(position != 0)
            llm.scrollToPositionWithOffset(position,-65); //패딩20들어간거빼줌.
        else
            llm.scrollToPositionWithOffset(position,0);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
