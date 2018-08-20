package com.sharewith.smartudy.directory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sharewith.smartudy.activity.FilterActivity;
import com.sharewith.smartudy.activity.QnAActivity;
import com.sharewith.smartudy.dto.WriteFragComponent;
import com.sharewith.smartudy.smartudy.R;

import java.util.ArrayList;

/**
 * Created by Simjae on 2018-08-10.
 */

public class CustomDialog extends Dialog {
    Context context;
    QnAActivity activity;
    ArrayList<String> category_available;
    ArrayList<String> category_selected;
    ArrayList<String> hashtag_selected;
    String selected = "";
    private SeekBar mSeekbar;
    private TextView mMoney;
    public CustomDialog(Context context) {
        super(context);
        this.context = context;
        activity = ((QnAActivity)context);
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
        setDialogSize();
        setSeekbarListener();
        show();
    }

    public void setDialogContents(WriteFragComponent.builder comp){
        comp.setSubject(category_selected);
        comp.setHashtag(hashtag_selected);
        comp.setMoney(mMoney.getText().toString());
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
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Window window = getWindow();
        window.setLayout( (int)(size.x * 0.9f),(int)(size.y * 0.7f) );
    }

    private void init_arraylists(){
        hashtag_selected = new ArrayList<String>();
        category_available = new ArrayList<String>();
        category_selected = new ArrayList<String>();
        selected="";
        // TODO: this array should be loaded from the database
        String[] arr = {"C/C++", "Java", "Python", "Embedded", "BlockChain", "IoT", "Android", "iOS", "AI"};
        for(String str : arr){
            category_available.add(str);
        }
    }

    private void setOkBtnListener(){
        findViewById(R.id.dialog_custom_okbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.postToServer();
            }
        });
    }

    public void setHashTagAddBtnListener(){
        findViewById(R.id.btn_filter_add_hashtag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                        params.setMarginEnd(dpToPx(8));
                        view.setPadding(dpToPx(16),0,dpToPx(16),0);
                        view.setLayoutParams(params);
                        ((Button)view).setText("#".concat(input.getText().toString()));
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
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
                        linearLayout.addView(view, 0);
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "hashtag adding cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).setCancelable(false).create().show();
            }
        });
    }
    private void setSubjectAddBtnListener(){

        findViewById(R.id.btn_filter_add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                String[] array=new String[category_available.size()];
                category_available.toArray(array);
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        selected = category_available.get(pos);
                        category_available.remove(pos);
                        category_selected.add(selected);

                        final LinearLayout linearLayout = findViewById(R.id.linear_layout_filter_subject);
                        View view = LayoutInflater.from(context).inflate(R.layout.filter_white_button, null);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        params.setMarginEnd(dpToPx(8));
                        view.setPadding(dpToPx(16),0,dpToPx(16),0);
                        view.setLayoutParams(params);
                        ((Button)view).setText(selected);
                        selected="";
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                                dialogBuilder
                                        .setMessage("삭제하시겠습니까?")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String str = ((Button)v).getText().toString();
                                                category_selected.remove(str);
                                                category_available.add(str);
                                                linearLayout.removeView(v);
                                            }
                                        }).create().show();
                            }
                        });
                        linearLayout.addView(view, 0);
                    }
                });
                builder.setCancelable(true);
                builder.setTitle("과목 추가").create().show();
            }
        });
    }



    private int dpToPx(int sizeInDP){
        int pxVal = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDP, activity.getResources().getDisplayMetrics()
        );
        return pxVal;
    }
}
