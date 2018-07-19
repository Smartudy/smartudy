package com.sharewith.smartudy.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sharewith.smartudy.smartudy.R;

public class RegisterAccountActivity extends AppCompatActivity {

    private boolean isNickNameValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("회원가입");

        final EditText editText = ((EditText)findViewById(R.id.textview_final_authentication_auth_digit));
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if (editText.getText().toString().equals("master")){
                        isNickNameValid=false;
                        editText.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                    }
                    else{
                        isNickNameValid=true;
                        editText.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                    }
                }
            }
        });
        final Button btn_major = ((Button)findViewById(R.id.btn_register_account_select_major));
        btn_major.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_major.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
            }
        });
        final Button btn_grade = ((Button)findViewById(R.id.btn_register_account_select_grade));
        btn_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_grade.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
