package com.sharewith.smartudy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sharewith.smartudy.activity.QnAActivity;
import com.sharewith.smartudy.dto.Question;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by cheba on 2018-07-09.
 */

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private RecyclerView mRecycler;
    private List<Question> mDatas;
    public static int visibleThreshold = 5; //리싸이클러뷰 하단에 가려진 부분에 몇개의 아이템이 대기 해야 하는가?
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    private OnLoadMoreListener onLoadMoreListener; //서버로 부터 데이터를 가져오는 리스너 인터페이스
    RecyclerView.OnClickListener mItemListener;
    private int page = 0;
    private int total = 0;
    private String mCategory = "";
    public QuestionRecyclerAdapter(List<Question> list, final RecyclerView recycler,String category){
        this.mDatas=list;
        this.mRecycler = recycler;
        total = getTotalDatas(category);
        Log.d("QuestionRecyclerAdapter","서버에 존재하는 "+category+"의 데이터 개수는"+total+"입니다.");
        if(mRecycler.getLayoutManager() instanceof LinearLayoutManager){
                    mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)mRecycler.getLayoutManager();
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            if(!loading && totalItemCount <= visibleThreshold + lastVisibleItem){ // 리싸이클러 뷰 하단부에 항상 visibleThreshold 개수 만큼의 아이템이 있어야 함.
                                if(onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore(page);
                                }
                                loading = true;
                            }
                        }
            });
        }
        mCategory = category;

    }

    public int getTotalDatas(String category){
        HashMap<String,String> map = new HashMap<>();
        map.put("category",category);
        Log.d("QuestionRecyclerAdapter",category);
        HttpUtils utils = new HttpUtils(HttpUtils.GET,map, Constant.GetQuestionCountURL,null);
        try {
            String result = utils.execute().get();
            JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
            total = obj.get("count").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public void addDatas(List<Question> datas)
    {
        int count = datas.size();
        for(int i=0; i<count; i++)
            mDatas.add(datas.get(i));
        notifyDataSetChanged();
    }
    public void setFirstData(){
        onLoadMoreListener.onLoadMore(page);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        return mDatas.get(position) != null ? 0 : 1 ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;
        if(viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_row_layout, parent, false);
            return new ItemViewHolder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            return new ProgressViewHolder(view);
        }
    }




    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder){
            Question data = mDatas.get(position);
            Log.d("QuestionRecyclerAdapter",data.toString());
            ((ItemViewHolder) holder).title.setText(data.getTitle());
            ((ItemViewHolder) holder).content.setText(data.getContent());
            ((ItemViewHolder) holder).price.setText(data.getMoney());
            ((ItemViewHolder) holder).time.setText(data.getFormattedTime());
            ((ItemViewHolder) holder).hashtag.setText(data.getHashtag());
            (holder.itemView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = holder.itemView.getContext();
                    Intent intent = new Intent(context,QnAActivity.class);
                    intent.putExtra("grp",mDatas.get(position).getId());
                    intent.putExtra("category",mCategory);
                    context.startActivity(intent);
                }
            });
        }else if(holder instanceof ProgressViewHolder){
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView title,content,hashtag,price,time;
        public ImageView image;
        public ItemViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:질문 목록 클릭시 해당 질문 답변창으로 이동한다.
                }
            });
            title=itemView.findViewById(R.id.textview_question_title);
            content=itemView.findViewById(R.id.textview_question_content);
            hashtag=itemView.findViewById(R.id.textview_question_hashtag);
            price=itemView.findViewById(R.id.textview_question_price);
            image = itemView.findViewById(R.id.imageview_question_content);
            time=itemView.findViewById(R.id.textview_question_remaining_time);
        }
    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

    }

    public interface OnLoadMoreListener{
        void onLoadMore(int page);
    }

    public void setLoaded(){
        loading = false;
        page += visibleThreshold;
        if(page > total)
            loading = true; // 데이터를 모두 가져온 경우 더이상 데이터 업뎃 금지
    }
    public RecyclerView getmRecycler() {
        return mRecycler;
    }
    public void setmRecycler(RecyclerView mRecycler) {
        this.mRecycler = mRecycler;
    }
    public List<Question> getmDatas() {
        return mDatas;
    }
    public void setmDatas(List<Question> mDatas) {
        this.mDatas = mDatas;
    }
    public int getVisibleThreshold() {
        return visibleThreshold;
    }
    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }
    public int getLastVisibleItem() {
        return lastVisibleItem;
    }
    public void setLastVisibleItem(int lastVisibleItem) {
        this.lastVisibleItem = lastVisibleItem;
    }
    public int getTotalItemCount() {
        return totalItemCount;
    }
    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }
    public boolean isLoading() {
        return loading;
    }
    public void setLoading(boolean loading) {
        this.loading = loading;
    }
    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    public void setListener(RecyclerView.OnClickListener listener){
        this.mItemListener = listener;
    }
}
