package com.sharewith.smartudy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    }

    public void setRadioBtnOnClickListener(){
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup_filter);
        final RadioButton rb_newest=(RadioButton)findViewById(R.id.radiobtn_filter_newest);
        final RadioButton rb_price=(RadioButton)findViewById(R.id.radiobtn_filter_price);
        final RadioButton rb_oldest=(RadioButton)findViewById(R.id.radiobtn_filter_oldest);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
