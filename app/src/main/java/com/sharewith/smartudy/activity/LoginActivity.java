package com.sharewith.smartudy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sharewith.smartudy.directory.HttpResultVO;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText mPhone;
    private EditText mPassword;
    private TextView mFindpw;
    private TextView mRegister;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPhone = findViewById(R.id.login_phone);
        mPassword = findViewById(R.id.login_password);
        mRegister = findViewById(R.id.textview_login_register);
        mFindpw = findViewById(R.id.textview_login_find_pw);
        mLogin = findViewById(R.id.btn_login_loginbtn);
        setListener();
}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setListener(){
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterAccountActivity.class));
            }
        });
        mFindpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindPasswordActivity.class));
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpResultVO result = LoginToServer();
                    if(result == null){//비회원
                        Toast.makeText(getApplicationContext(), Constant.NO_MEMBER,Toast.LENGTH_LONG).show();
                    }else if(!result.isSuccess()){
                            //연락처, 비밀번호 불일치
                            Toast.makeText(getApplicationContext(), Constant.LOGIN_FAIL,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "로그인 성공",Toast.LENGTH_LONG).show();
                        finish();
                    }
            }
        });



    }
    private HttpResultVO LoginToServer(){
        String json="";
        HashMap<String,String> map = new HashMap<>();
        map.put("phone",mPhone.getText().toString());
        map.put("password",mPassword.getText().toString());
        HttpUtils http = HttpUtils.getInstance(HttpUtils.POST,map,Constant.LoginURL,getApplicationContext(),null);
        try {
            json = http.execute().get();
            Log.d("LOG",json);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return new Gson().fromJson(json,HttpResultVO.class);
    }
}
