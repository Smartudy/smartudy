package com.sharewith.smartudy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sharewith.smartudy.smartudy.R;

public class FinalAuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_authentication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("인증번호 확인");

        // 이전 액티비티에서 전화번호를 넘겨받는다.( TODO: 이름, 생년월일 등 필요시 다른정보도)
        Intent intent = getIntent();
        final String telnum = intent.getStringExtra("telnum");

        ((Button)findViewById(R.id.btn_final_authentication_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
                intent.putExtra("telnum", telnum);
                startActivity(intent);
                finish();
            }
        });

        ((Button)findViewById(R.id.btn_final_authentication_retransmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((EditText)findViewById(R.id.textview_final_authentication_auth_digit)).getText().toString();
                Toast.makeText(getApplicationContext(), "input: ".concat(str), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
