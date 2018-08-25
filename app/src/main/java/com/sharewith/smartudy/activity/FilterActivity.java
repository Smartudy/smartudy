package com.sharewith.smartudy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.sharewith.smartudy.smartudy.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    // 사용자가 선택한 값을 QuestionListActivity에서 startActivityForResult(intent) 사용하여 리턴받는 식으로 사용해야한다! (나중에) - DONE!

    private RadioGroup radioGroup_order, radioGroup_etc;
    private CrystalRangeSeekbar seekbar;

    private ArrayList<String> options_available, options_selected;
    private ArrayList<String> hashtag_selected;

    private String selected = "";

    private int orderOption = 0;
    private int maxCoin=0, minCoin=1000;
    private int etcOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("필터");

        init_arraylists();

        setRadioBtnOnClickListener();

        setSeekBarChangeListener();

        setSubjectAddBtnListener();

        setHashTagAddBtnListener();

        findViewById(R.id.btn_filter_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FilterActivity.this, "결과전달..", Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.putExtra("orderOption", orderOption);  // 필터정보를 인텐트에 담는다
                intent.putExtra("subjectArrayList", options_selected);
                intent.putExtra("hashtagArrayList", hashtag_selected);
                intent.putExtra("minCoin", minCoin);
                intent.putExtra("maxCoin", maxCoin);
                intent.putExtra("etcOption", etcOption);
                setResult(RESULT_OK, intent); // QuestionListActivity에게 RESULT_OK 상태임과 함께 인텐트에 담긴 결과값을 전달한다.
                finish();
            }
        });
    }

    public void init_arraylists(){
        hashtag_selected = new ArrayList<String>();
        options_available = new ArrayList<String>();
        options_selected = new ArrayList<String>();
        selected="";
        // TODO: this array should be loaded from the server
        String[] arr = {"C/C++", "Java", "Python", "Embedded", "BlockChain", "IoT", "Android", "iOS", "AI"};
        for(String str : arr){
            options_available.add(str);
        }
    }

    public void setSubjectAddBtnListener(){

        ((Button)findViewById(R.id.btn_filter_add_subject)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(FilterActivity.this);
                String[] array=new String[options_available.size()];
                options_available.toArray(array);
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {
                        dialog.dismiss();
                        selected = options_available.get(pos);
                        options_available.remove(pos);
                        options_selected.add(selected);

                        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout_filter_subject);
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.filter_white_button, null);

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
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FilterActivity.this);
                                dialogBuilder
                                        .setMessage("삭제하시겠습니까?")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String str = ((Button)v).getText().toString();
                                                options_selected.remove(str);
                                                options_available.add(str);
                                                linearLayout.removeView(v);
                                                ((HorizontalScrollView)findViewById(R.id.scrollview_filter_subject)).fullScroll(View.FOCUS_RIGHT);
                                                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "subject removal cancelled", Toast.LENGTH_SHORT).show();
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
                builder.setTitle("과목 추가");
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "subject adding cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setHashTagAddBtnListener(){
        ((Button)findViewById(R.id.btn_filter_add_hashtag)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(FilterActivity.this);
                builder.setTitle("해시태그를 입력해주세요");
                final EditText input = new EditText(FilterActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(getApplicationContext(), "hashtag add", Toast.LENGTH_SHORT).show();
                        hashtag_selected.add(input.getText().toString());
                        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout_filter_hashtag);
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.filter_white_button, null);

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
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FilterActivity.this);
                                dialogBuilder.setMessage("삭제하시겠습니까?");
                                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String str = ((Button)v).getText().toString();
                                        hashtag_selected.remove(str);
                                        linearLayout.removeView(v);
                                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "subject removal cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                AlertDialog alertDialog = dialogBuilder.create();
                                alertDialog.show();
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
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "hashtag adding cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setRadioBtnOnClickListener(){
        radioGroup_order = (RadioGroup)findViewById(R.id.radiogroup_filter);
        final RadioButton rb_newest=(RadioButton)findViewById(R.id.radiobtn_filter_newest);
        final RadioButton rb_price=(RadioButton)findViewById(R.id.radiobtn_filter_price);
        final RadioButton rb_oldest=(RadioButton)findViewById(R.id.radiobtn_filter_oldest);

        radioGroup_order.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rb_newest.isChecked()){
                    rb_newest.setBackgroundResource(R.drawable.btn_round_green_background);
                    rb_newest.setTextColor(getColor(R.color.selectedTextColor));
                    rb_price.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_price.setTextColor(getColor(R.color.textColor));
                    rb_oldest.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_oldest.setTextColor(getColor(R.color.textColor));
                    orderOption = 0;
                }
                else if(rb_price.isChecked()){
                    rb_newest.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_newest.setTextColor(getColor(R.color.textColor));
                    rb_price.setBackgroundResource(R.drawable.btn_round_green_background);
                    rb_price.setTextColor(getColor(R.color.selectedTextColor));
                    rb_oldest.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_oldest.setTextColor(getColor(R.color.textColor));
                    orderOption = 1;
                }
                else if(rb_oldest.isChecked()){
                    rb_newest.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_newest.setTextColor(getColor(R.color.textColor));
                    rb_price.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_price.setTextColor(getColor(R.color.textColor));
                    rb_oldest.setBackgroundResource(R.drawable.btn_round_green_background);
                    rb_oldest.setTextColor(getColor(R.color.selectedTextColor));
                    orderOption = 2;
                }
                else{
                    Toast.makeText(getApplicationContext(), "radio group error", Toast.LENGTH_LONG).show();
                }
            }
        });

        radioGroup_etc = (RadioGroup)findViewById(R.id.radiogroup_etc);
        final RadioButton rb_all = (RadioButton)findViewById(R.id.radiobtn_filter_etc_all);
        final RadioButton rb_finished = (RadioButton)findViewById(R.id.radiobtn_filter_etc_finished);
        final RadioButton rb_unfinished = (RadioButton)findViewById(R.id.radiobtn_filter_etc_unfinished);

        radioGroup_etc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rb_finished.isChecked()){
                    rb_all.setButtonDrawable(R.drawable.unchecked);
                    rb_finished.setButtonDrawable(R.drawable.checked);
                    rb_unfinished.setButtonDrawable(R.drawable.unchecked);
                    etcOption = 0;
                }
                else if(rb_unfinished.isChecked()){
                    rb_all.setButtonDrawable(R.drawable.unchecked);
                    rb_finished.setButtonDrawable(R.drawable.unchecked);
                    rb_unfinished.setButtonDrawable(R.drawable.checked);
                    etcOption = 1;
                }
                else if(rb_all.isChecked()){
                    rb_all.setButtonDrawable(R.drawable.checked);
                    rb_finished.setButtonDrawable(R.drawable.unchecked);
                    rb_unfinished.setButtonDrawable(R.drawable.unchecked);
                    etcOption = 2;
                }
            }
        });
    }

    public void setSeekBarChangeListener(){
        seekbar = (CrystalRangeSeekbar)findViewById(R.id.seekbar_filter_price);
        seekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minCoin = minValue.intValue();
                maxCoin = maxValue.intValue();
                ((TextView)findViewById(R.id.textview_filter_min_price)).setText(Integer.toString(minCoin));
                ((TextView)findViewById(R.id.textview_filter_max_price)).setText(Integer.toString(maxCoin));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public int dpToPx(int sizeInDP){
        int pxVal = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources().getDisplayMetrics()
        );
        return pxVal;
    }

}
