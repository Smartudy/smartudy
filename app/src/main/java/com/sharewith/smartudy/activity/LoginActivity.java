package com.sharewith.smartudy.activity;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharewith.smartudy.dao.AccountDBOpenHelper;
import com.sharewith.smartudy.smartudy.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setOnEditorActionListener();
        setOnClickListeners();

    }

    public void setOnEditorActionListener(){
        ((EditText)findViewById(R.id.edittext_login_password)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setOnClickListeners(){
        ((TextView)findViewById(R.id.textview_login_register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "회원가입", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                startActivity(intent);
            }
        });
        ((TextView)findViewById(R.id.textview_login_find_pw)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "비밀번호 찾기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        ((Button)findViewById(R.id.btn_login_loginbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "loginbtn clicked", Toast.LENGTH_SHORT).show();
                String userinput_phone_number = ((EditText)findViewById(R.id.edittext_login_phone_number)).getText().toString();
                String userinput_password = ((EditText)findViewById(R.id.edittext_login_password)).getText().toString();

                AccountDBOpenHelper mAccountDBOpenHelper = new AccountDBOpenHelper(LoginActivity.this);
                try{
                    mAccountDBOpenHelper.open();
                }catch (SQLException e){
                    e.printStackTrace();
                }
                boolean isValid = mAccountDBOpenHelper.checkIfRowExists(userinput_phone_number, userinput_password);
                mAccountDBOpenHelper.close();

                if(isValid){
                    // 올바른 번호와 비밀번호 입력.
                    // TODO : 로그인 세션....
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
