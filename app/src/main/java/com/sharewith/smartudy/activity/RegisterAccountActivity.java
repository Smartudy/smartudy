package com.sharewith.smartudy.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sharewith.smartudy.directory.HttpResultVO;
import com.sharewith.smartudy.dto.AccountDto;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class RegisterAccountActivity extends AppCompatActivity {

    private boolean isNickNameValid = true;
    private String mNick;
    private String mMajor;
    private String mGrade;
    private String mPass;
    private EditText mNick_Edit;
    private EditText mPassword_Edit;
    ArrayList<String> major_list;

    String nickname = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("회원가입");

        updateMajorListFromDB();

        final EditText editText = ((EditText) findViewById(R.id.textview_final_authentication_auth_digit));
        mNick_Edit = editText;
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
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
        //비밀번호칸 추가했어 여기 수정좀 해줘
        mPassword_Edit = findViewById(R.id.textview_final_authentication_auth_password);



//        editText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                editText.setFocusable(true);
//                editText.setCursorVisible(true);
//                return false;
//            }
//        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) { // TODO: loophole 없는지 확인하자!
                if (!hasFocus) {
                    // TODO: if()문의 조건을 바꿔야함 - if(이미 존재하는 아이디인 경우){} / else{}
                    String userInput = editText.getText().toString();

                    if (isNickNameValid && userInput.equals(nickname)) return;

                    if (userInput.equals("공학수학마스터")) {
                        isNickNameValid = false;
                        nickname = "";
                        editText.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임 입니다.", Toast.LENGTH_LONG).show();
                    } else if (!isValid(userInput)) {
                        isNickNameValid = false;
                        editText.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(getApplicationContext(), "형식에 맞는 닉네임이 아닙니다.", Toast.LENGTH_LONG).show();
                    } else {
                        isNickNameValid = true;
                        nickname = userInput;
                        editText.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        Toast.makeText(getApplicationContext(), "사용 가능한 닉네임 입니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        final Button btn_major = ((Button) findViewById(R.id.btn_register_account_select_major));
        btn_major.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAccountActivity.this);
                String[] majors = new String[major_list.size()];
                major_list.toArray(majors);
                builder.setItems(majors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_major.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        mMajor = major_list.get(which);
                        btn_major.setText(mMajor);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        final Button btn_grade = ((Button) findViewById(R.id.btn_register_account_select_grade));
        btn_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAccountActivity.this);
                final String[] grade = {"1", "2", "3", "4", "5"};
                builder.setItems(grade, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_grade.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        mGrade = grade[which];
                        btn_grade.setText(grade[which].concat("학년"));
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ((Button) findViewById(R.id.btn_register_account_register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 현재까지 입력한 정보를 바탕으로 회원등록, 로그인 창으로 다시 돌아간다.
                    mNick = mNick_Edit.getText().toString();
                    mPass = mPassword_Edit.getText().toString();
                    HttpResultVO result = registerToServerByPost();
                    if(result == null)
                        Toast.makeText(RegisterAccountActivity.this,Constant.NICK_DUPLICATED,Toast.LENGTH_LONG).show();
                    else if(!result.isSuccess())
                        Toast.makeText(RegisterAccountActivity.this,result.getError(),Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(RegisterAccountActivity.this, Constant.REGISTER_OK,Toast.LENGTH_LONG).show();
                        finish();
                    }

            }
        });
    }

    private HttpResultVO registerToServerByPost(){
        String json="";
        HashMap<String,String> m = new HashMap<String,String>();
        m.put("nickname",mNick);
        m.put("password",mPass);
        String phone = ((EditText)findViewById(R.id.textview_final_authentication_auth_phone)).getText().toString();
        m.put("phone",phone);
        m.put("major",mMajor);
        m.put("grade",mGrade);
        HttpUtils http = new HttpUtils(HttpUtils.POST,m,Constant.RegisterURL,getApplicationContext());
        try {
            json = http.execute().get();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return new Gson().fromJson(json,HttpResultVO.class);

    }
    public void updateMajorListFromDB() {
        major_list = new ArrayList<String>();
        // TODO: update major list from the database
        String[] majors = {"가정교육과", "가정학과", "간호학과", "건강관리학과", "건축공학과", "건축학과", "게임공학과", "경영정보학과", "경영학과"};
        for (String str : majors)
            major_list.add(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    //((EditText) v).setCursorVisible(false);
                    //v.setFocusable(false);
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    //isValid(userInput)

    public boolean isValid(String str) {
        if (str.length() < 1 || str.length() > 20) return false;
        else return true;
    }
}
