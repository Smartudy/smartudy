package com.sharewith.smartudy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.sharewith.smartudy.smartudy.R;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("필터");

        setRadioBtnOnClickListener();

        setSeekBarChangeListener();

        setSubjectAddBtnListener();

        setHashTagAddBtnListener();
    }

    public void setSubjectAddBtnListener(){
        ((Button)findViewById(R.id.btn_filter_add_subject)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"not implemented", Toast.LENGTH_SHORT).show();
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_layout_filter_subject);
                Button btn = (Button)(LayoutInflater.from(getApplicationContext()).inflate(R.layout.filter_white_button, null));
                btn.setText("new");
                btn.setHeight((findViewById(R.id.btn_filter_add_subject)).getHeight());

                linearLayout.addView(btn, 0);

            }
        });
    }

    public void setHashTagAddBtnListener(){
        ((Button)findViewById(R.id.btn_filter_add_hashtag)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"not implemented", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setRadioBtnOnClickListener(){
        final RadioGroup radioGroup_order = (RadioGroup)findViewById(R.id.radiogroup_filter);
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
                }
                else if(rb_price.isChecked()){
                    rb_newest.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_newest.setTextColor(getColor(R.color.textColor));
                    rb_price.setBackgroundResource(R.drawable.btn_round_green_background);
                    rb_price.setTextColor(getColor(R.color.selectedTextColor));
                    rb_oldest.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_oldest.setTextColor(getColor(R.color.textColor));
                }
                else if(rb_oldest.isChecked()){
                    rb_newest.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_newest.setTextColor(getColor(R.color.textColor));
                    rb_price.setBackgroundResource(R.drawable.btn_round_white_background);
                    rb_price.setTextColor(getColor(R.color.textColor));
                    rb_oldest.setBackgroundResource(R.drawable.btn_round_green_background);
                    rb_oldest.setTextColor(getColor(R.color.selectedTextColor));
                }
                else{
                    Toast.makeText(getApplicationContext(), "radio group error", Toast.LENGTH_LONG).show();
                }
            }
        });

        final RadioGroup radioGroup_etc = (RadioGroup)findViewById(R.id.radiogroup_etc);
        final RadioButton rb_finished = (RadioButton)findViewById(R.id.radiobtn_filter_etc_finished);
        final RadioButton rb_unfinished = (RadioButton)findViewById(R.id.radiobtn_filter_etc_unfinished);

        radioGroup_etc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rb_finished.isChecked()){
                    rb_finished.setButtonDrawable(R.drawable.checked);
                    rb_unfinished.setButtonDrawable(R.drawable.unchecked);
                }
                else{
                    rb_finished.setButtonDrawable(R.drawable.unchecked);
                    rb_unfinished.setButtonDrawable(R.drawable.checked);
                }
            }
        });
    }

    public void setSeekBarChangeListener(){
        CrystalRangeSeekbar seekbar = (CrystalRangeSeekbar)findViewById(R.id.seekbar_filter_price);
        seekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                ((TextView)findViewById(R.id.textview_filter_min_price)).setText(Integer.toString(minValue.intValue()));
                ((TextView)findViewById(R.id.textview_filter_max_price)).setText(Integer.toString(maxValue.intValue()));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
