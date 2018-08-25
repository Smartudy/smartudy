package com.sharewith.smartudy.directory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sharewith.smartudy.Interface.AsyncResponse;
import com.sharewith.smartudy.activity.FilterActivity;
import com.sharewith.smartudy.activity.QnAActivity;
import com.sharewith.smartudy.activity.QuestionWriteActivity;
import com.sharewith.smartudy.adapter.QnAListAdapter;
import com.sharewith.smartudy.dto.WriteFragComponent;
import com.sharewith.smartudy.fragment.QnAListFragment;
import com.sharewith.smartudy.fragment.WriteFragment;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import java.util.ArrayList;

/**
 * Created by Simjae on 2018-08-10.
 */

public class CustomDialog extends Dialog {
    public static final int QUESTION = 0;
    public static final int  ANSWER = 1;
    private int type;
    private Context context;
    private WriteFragComponent mData;
    private  Display display;
    private DisplayMetrics metrics;
    private FragmentManager fm;
    private String url;
    ArrayList<String> subject_available;
    ArrayList<String> subject_selected;
    ArrayList<String> hashtag_selected;
    WriteFragComponent.builder builder;
    String selected = "";
    private SeekBar mSeekbar;
    private TextView mMoney;
    public CustomDialog(Context context,int type,Display display,DisplayMetrics metrics,String url) {
        super(context);
        this.context = context;
        this.display = display;
        this.metrics = metrics;
        this.type = type;
        this.url = url;
        // 액티비티의 타이틀바를 숨긴다.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        setContentView(R.layout.dialog_custom);
        mSeekbar = findViewById(R.id.dialog_custom_seekbar);
    }
    public void showDialog(){
        init_arraylists(); //과목추가 버튼 클릭시 나올 내용들
        setSubjectAddBtnListener();
        setHashTagAddBtnListener();
        setOkBtnListener();
        //setDialogSize();
        setSeekbarListener();
        show();
    }

    public void setDialogContents(){
        builder.setSubject(subject_selected);
        builder.setHashtag(hashtag_selected);
        builder.setMoney(mMoney.getText().toString());
        Log.d("CustomDialog","현재 설정된 질문의 금액은 "+mMoney.getText().toString());
    }

    private void setSeekbarListener(){
        mSeekbar.incrementProgressBy(50);
        final int step = 50;
        mSeekbar.setProgress(0);
        mMoney = findViewById(R.id.dialog_custom_text_money);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress/step;
                progress = progress * step;
                mMoney.setText(String.valueOf(progress));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    private void setDialogSize(){
        Point size = new Point();
        display.getSize(size);
        Window window = getWindow();
        window.setLayout( (int)(size.x * 0.9f),(int)(size.y * 0.7f) );
    }

    private void init_arraylists(){
        hashtag_selected = new ArrayList<String>();
        subject_available = new ArrayList<String>();
        subject_selected = new ArrayList<String>();
        selected="";
        // TODO: this array should be loaded from the server
        String[] arr = {"C/C++", "Java", "Python", "Embedded", "BlockChain", "IoT", "Android", "iOS", "AI"};
        for(String str : arr){
            subject_available.add(str);
        }
    }

    private void setOkBtnListener(){
        findViewById(R.id.dialog_custom_okbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogContents();//모든 정보 취합 완료
                PostToServer();
                if(type == ANSWER)
                    ((QnAActivity)context).afterAnswerPost();
            }
        });
    }

    private void PostToServer(){
        try {
            mData = builder.build();
            Log.d("CustomDIalog",mData.toString()+"서버로 전송");
            HttpUtils util = new HttpUtils(HttpUtils.MULTIPART, null, url, context);
            util.setMultipartdata(mData);
            String result = util.execute().get();
            Log.d("CustomDialog",result);
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(result).getAsJsonObject();
            if(json.get("success").getAsBoolean() == true){
                Toast.makeText(context, "게시글이 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                if(type == ANSWER)
                    ((QnAActivity)context).setRegistredID(json.get("id").getAsString());
                dismiss();
            }else{
                Toast.makeText(context, "게시글 등록이 실패 하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Log.d("QnAActivity","멀티파트 요청 중 에러 발생");
            e.printStackTrace();
        }
    }

    public void setHashTagAddBtnListener(){
        findViewById(R.id.btn_filter_add_hashtag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("해시태그를 입력해주세요");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hashtag_selected.add(input.getText().toString());
                        final LinearLayout linearLayout = findViewById(R.id.linear_layout_filter_hashtag);
                        View view = LayoutInflater.from(context).inflate(R.layout.filter_white_button, null);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        // *** layout내의 view 변화를 애니메이션화 해주는 코드 ***
                        TransitionManager.beginDelayedTransition(linearLayout);

                        params.setMarginEnd(dpToPx(8));
                        view.setPadding(dpToPx(16),0,dpToPx(16),0);
                        view.setLayoutParams(params);
                        ((Button)view).setText("#".concat(input.getText().toString()));
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                dialogBuilder.setMessage("삭제하시겠습니까?");
                                dialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String str = ((Button)v).getText().toString();
                                        hashtag_selected.remove(str);
                                        linearLayout.removeView(v);
                                    }
                                });
                                dialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create().show();
                            }
                        });
                        int viewCount = linearLayout.getChildCount();
                        linearLayout.addView(view, viewCount-1);
                        final HorizontalScrollView sv = (HorizontalScrollView)findViewById(R.id.scrollview_filter_hashtag);
                        sv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                            }
                        }, 500L);
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "hashtag adding cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(false).create().show();
            }
        });
    }
    private void setSubjectAddBtnListener(){

        findViewById(R.id.btn_filter_add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String[] array=new String[subject_available.size()];
                subject_available.toArray(array);
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        selected = subject_available.get(pos);
                        subject_available.remove(pos);
                        subject_selected.add(selected);

                        final LinearLayout linearLayout = findViewById(R.id.linear_layout_filter_subject);
                        View view = LayoutInflater.from(context).inflate(R.layout.filter_white_button, null);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        params.setMarginEnd(dpToPx(8));
                        TransitionManager.beginDelayedTransition(linearLayout);
                        view.setPadding(dpToPx(16),0,dpToPx(16),0);
                        view.setLayoutParams(params);
                        ((Button)view).setText(selected);
                        selected="";
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                dialogBuilder
                                        .setMessage("삭제하시겠습니까?")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String str = ((Button)v).getText().toString();
                                                subject_selected.remove(str);
                                                subject_available.add(str);
                                                linearLayout.removeView(v);
                                                ((HorizontalScrollView)findViewById(R.id.scrollview_filter_subject)).fullScroll(View.FOCUS_RIGHT);
                                            }
                                        }).create().show();
                            }
                        });
                        int viewCount = linearLayout.getChildCount();
                        linearLayout.addView(view, viewCount-1);
                        final HorizontalScrollView sv = (HorizontalScrollView)findViewById(R.id.scrollview_filter_subject);
                        sv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                            }
                        }, 500L);
                    }
                });
                builder.setCancelable(true);
                builder.setTitle("과목 추가").create().show();
            }
        });
    }


    public WriteFragComponent.builder getBuilder() {
        return builder;
    }
    public void setBuilder(WriteFragComponent.builder builder) {
        this.builder = builder;
    }

    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    private int dpToPx(int sizeInDP){
        int pxVal = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDP, metrics);
        return pxVal;
    }
}
