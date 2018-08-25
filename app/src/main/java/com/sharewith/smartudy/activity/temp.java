package com.sharewith.smartudy.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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

import com.sharewith.smartudy.dao.AccountDBOpenHelper;
import com.sharewith.smartudy.dto.AccountDto;
import com.sharewith.smartudy.smartudy.R;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class temp extends AppCompatActivity {

    private boolean isNickNameValid = false;
    private boolean isPasswordValid = false;
    private boolean isPasswordEquals = false;
    private boolean isMajorSet = false, isGradeSet = false;
    private ArrayList<String> major_list;

    private String telnum = "";
    private String nickname = "";
    private String password = "";
    private String major = "";
    private int grade = 0;

    private AccountDBOpenHelper mAccountDBOpenHelper;

    private static final String TAG = "RegisterAccount DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("회원가입");

        telnum = getIntent().getStringExtra("telnum");

        updateMajorListFromDB();

        //setEditTextAttrs();

        //setBtnOnClickListeners();
    }


    public void updateMajorListFromDB() {
        major_list = new ArrayList<String>();
        // TODO: update major list from the database
        String[] majors = {"가정교육과", "가정학과", "간호학과", "건강관리학과", "건축공학과", "건축학과", "게임공학과", "경영정보학과", "경영학과"};
        for (String str : majors)
            major_list.add(str);
    }
/*
    public void setEditTextAttrs(){
        final EditText edittext_nick = ((EditText) findViewById(R.id.textview_register_account_nickname));
        edittext_nick.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edittext_nick.setInputType(InputType.TYPE_CLASS_TEXT);
//        edittext_nick.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch (actionId) {
//                    case EditorInfo.IME_ACTION_NEXT:
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });

        final EditText edittext_password = ((EditText)findViewById(R.id.textview_register_account_password));
        edittext_password.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edittext_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);

        final EditText edittext_password_check = ((EditText)findViewById(R.id.textview_register_account_password_check));
        edittext_password_check.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edittext_password_check.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
        //input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edittext_password_check.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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


//        editText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                editText.setFocusable(true);
//                editText.setCursorVisible(true);
//                return false;
//            }
//        });
        edittext_nick.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) { // TODO: loophole 없는지 확인하자!
                if (!hasFocus) {
                    // TODO: if()문의 조건을 바꿔야함 - if(이미 존재하는 아이디인 경우){} / else{}
                    String userInput = edittext_nick.getText().toString();
                    if (isNickNameValid && userInput.equals(nickname)) return;

                    if (userInput.equals("공학수학마스터")) {
                        isNickNameValid = false;
                        nickname = "";
                        edittext_nick.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임 입니다.", Toast.LENGTH_LONG).show();
                    } else if (!isValid(userInput)) {
                        isNickNameValid = false;
                        edittext_nick.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(getApplicationContext(), "형식에 맞는 닉네임이 아닙니다.", Toast.LENGTH_LONG).show();
                    } else {
                        isNickNameValid = true;
                        nickname = userInput;
                        edittext_nick.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        Toast.makeText(getApplicationContext(), "사용 가능한 닉네임 입니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        edittext_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String userInput = edittext_password.getText().toString();
                    if(userInput.equals(password)) return; // 포커스가 바뀌었으나, 비밀번호가 이전과 동일한 경우

                    edittext_password_check.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp);
                    isPasswordEquals = false;

                    if(isPasswordInputValid(userInput)) // 유효한 비밀번호가 입력된 경우
                    {
                        password = userInput;
                        isPasswordValid = true;
                        edittext_password.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        Toast.makeText(getApplicationContext(), "유효한 비밀번호 입니다.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        password = "";
                        isPasswordValid = false;
                        edittext_password.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(getApplicationContext(), "길이 5이상, 20이하의 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        edittext_password_check.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String userInput = edittext_password_check.getText().toString();
                    if(!isPasswordValid){
                        edittext_password_check.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(getApplicationContext(), "유효한 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                    }
                    else{ // isPasswordValid == true
                        if(userInput.equals(password)){ // [비밀번호] == [비밀번호 확인]
                            isPasswordEquals = true;
                            edittext_password_check.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                            Toast.makeText(getApplicationContext(), "일치합니다.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            isPasswordEquals = false;
                            edittext_password_check.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                            Toast.makeText(getApplicationContext(), "일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public void setBtnOnClickListeners(){
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
                        btn_major.setText(major_list.get(which));
                        major = major_list.get(which);
                        isMajorSet = true;
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
                final String[] gradeArr = {"1", "2", "3", "4", "5"};
                builder.setItems(gradeArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_grade.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        btn_grade.setText(gradeArr[which].concat("학년"));
                        grade = Integer.parseInt(gradeArr[which]);
                        isGradeSet = true;
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
                if (isNickNameValid && isPasswordValid && isPasswordEquals && isMajorSet && isGradeSet) { // TODO: 다른조건도 확인해야한다! 학과 및 학년까지 모두 설정했는지.
                    AccountDto account = new AccountDto(telnum, nickname, password, major, grade);
                    addAccountToDB(account);
                    mAccountDBOpenHelper.close();
                    finish();
                }
                else {
                    Toast.makeText(RegisterAccountActivity.this, "정보입력이 불완전합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addAccountToDB(AccountDto account){
        Log.d(TAG, "new account\ntelnum: "+account.getA_tel_number()
                +"\nnickname: "+account.getA_nick()
                +"\npassword: "+account.getA_password()
                +"\nmajor: "+account.getA_major()
                +"\ngrade: "+Integer.toString(account.getA_grade())
        );
        mAccountDBOpenHelper = new AccountDBOpenHelper(RegisterAccountActivity.this);
        try{
            mAccountDBOpenHelper.open();
        } catch (SQLException e){
            e.printStackTrace();
        }
        mAccountDBOpenHelper.insertColumn(account);
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

    public boolean isValid(String str) {
        if (str.length() < 1 || str.length() > 20) return false;
        else return true;
    }

    public boolean isPasswordInputValid(String str){
        // TODO: 패스워드 검사 기준 정해야
        if(str.length()<5 || str.length() >20) return false;
        else return true;
    }

    @Override
    protected void onDestroy() {
        //mAccountDBOpenHelper.close();
        super.onDestroy();
    }*/
}