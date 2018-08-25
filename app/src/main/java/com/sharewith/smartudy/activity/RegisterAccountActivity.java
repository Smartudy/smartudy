package com.sharewith.smartudy.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.Constant;
import com.sharewith.smartudy.utils.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;


public class RegisterAccountActivity extends AppCompatActivity {

    private boolean isNickNameValid = false;
    private boolean isPasswordValid = false;
    private boolean isPasswordEquals = false;
    private boolean isPhoneNumValid = false; // 얘는 핸드폰인증 완료했을때만 true로!
    private boolean isMajorSet = false, isGradeSet = false;

    private String mNick;
    private String mPass;
    private String mPhone;
    private String mMajor;
    private String mGrade;

    private EditText mNick_Edit;
    private EditText mPassword_Edit, mPassWordCheck_Edit;
    private EditText mPhonenumber_Edit;
    ArrayList<String> major_list;


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

        setEdittextAttrs();

        setEdittextOnFocusChangeListeners();

        setButtonListeners();
    }

    private HttpResultVO registerToServerByPost(){
        String json="";
        HashMap<String,String> m = new HashMap<String,String>();
        m.put("nickname",mNick);
        m.put("password",mPass);
        m.put("phone",mPhone);
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

    public void setEdittextAttrs(){
        mNick_Edit = ((EditText) findViewById(R.id.textview_final_authentication_auth_nickname));
        mNick_Edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mNick_Edit.setInputType(InputType.TYPE_CLASS_TEXT);
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch (actionId){
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
//        //비밀번호칸 추가했어 여기 수정좀 해줘
        mPassword_Edit = findViewById(R.id.textview_final_authentication_auth_password);
        mPassword_Edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mPassword_Edit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);

        mPassWordCheck_Edit = findViewById(R.id.textview_final_authentication_auth_password_check);
        mPassWordCheck_Edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mPassWordCheck_Edit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);

//        editText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                editText.setFocusable(true);
//                editText.setCursorVisible(true);
//                return false;
//            }
//        });

        mPhonenumber_Edit = findViewById(R.id.textview_final_authentication_auth_phone);
        mPhonenumber_Edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mPhonenumber_Edit.setInputType(InputType.TYPE_CLASS_PHONE);
        mPhonenumber_Edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_NEXT:
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public void setEdittextOnFocusChangeListeners(){
        mNick_Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) { // TODO: loophole 없는지 확인하자!
                if (!hasFocus) {
                    // TODO: if()문의 조건을 바꿔야함 - if(이미 존재하는 아이디인 경우){} / else{}
                    String userInput = mNick_Edit.getText().toString();

                    if (isNickNameValid && userInput.equals(mNick)) return;

                    if (userInput.equals("공학수학마스터")) { // TODO : 유효한지 서버에서 체크해야
                        isNickNameValid = false;
                        mNick = "";
                        mNick_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임 입니다.", Toast.LENGTH_LONG).show();
                    } else if (!isNickInputValid(userInput)) {
                        isNickNameValid = false;
                        mNick_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(getApplicationContext(), "형식에 맞는 닉네임이 아닙니다.", Toast.LENGTH_LONG).show();
                    } else {
                        isNickNameValid = true;
                        mNick = userInput;
                        mNick_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        Toast.makeText(getApplicationContext(), "사용 가능한 닉네임 입니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mPassword_Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String userInput = mPassword_Edit.getText().toString();
                    if (userInput.equals(mPass)) return;

                    mPassword_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp);
                    isPasswordEquals = false;

                    if (isPasswordInputValid(userInput)) {
                        mPass = userInput;
                        isPasswordValid = true;
                        mPassword_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        Toast.makeText(RegisterAccountActivity.this, "유효한 비밀번호 입니다.", Toast.LENGTH_LONG).show();
                    } else {
                        mPass = "";
                        isPasswordValid = false;
                        mPassword_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(RegisterAccountActivity.this, "길이 5이상, 20이하의 비밀번호 입력", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        mPassWordCheck_Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String userInput = mPassWordCheck_Edit.getText().toString();
                    if (!isPasswordValid) {
                        mPassWordCheck_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                        Toast.makeText(RegisterAccountActivity.this, "유효한 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    } else { // isPasswordValid == true (위에서 유효한 비밀먼호를 입력한 상태임. 사용자가 같은 비밀번호를 입력하기만 하면돼
                        if (userInput.equals(mPass)) {
                            isPasswordEquals = true;
                            mPassWordCheck_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                            Toast.makeText(getApplicationContext(), "일치합니다.", Toast.LENGTH_LONG).show();
                        } else {
                            isPasswordEquals = false;
                            mPassWordCheck_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                            Toast.makeText(getApplicationContext(), "일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        mPhonenumber_Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String userInput = mPhonenumber_Edit.getText().toString();
                    // TODO: 여기 바꿔야해...임시코드임!
                    if (isPhoneNumValid && userInput.equals(mPhone)) return;

                    if (isPhoneNumInputValid(userInput)) {
                        isPhoneNumValid = true;
                        mPhone = userInput;
                        mPhonenumber_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                    } else {
                        isPhoneNumValid = false;
                        mPhone = "";
                        mPhonenumber_Edit.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_red_border);
                    }
                }
            }
        });
    }

    public void setButtonListeners(){
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
                final String[] grade = {"1", "2", "3", "4", "5"};
                builder.setItems(grade, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_grade.setBackgroundResource(R.drawable.edittext_round_white_background_radius_6dp_blue_border);
                        mGrade = grade[which];
                        btn_grade.setText(grade[which].concat("학년"));
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
                //mNick = mNick_Edit.getText().toString(); mPass = mPassword_Edit.getText().toString(); -> isNickValid isPassValid isPasswordEquals 등등 불린 멤버변수들이 모두 true라면 ok
                if (!isInputComplete()) // 서버에 가입 요청 보내기전 모든 입력이 완료되어있는지 최종체크
                    Toast.makeText(RegisterAccountActivity.this, "input not complete", Toast.LENGTH_LONG).show();
                else {
                    HttpResultVO result = registerToServerByPost();
                    if (result == null)
                        Toast.makeText(RegisterAccountActivity.this, Constant.NICK_DUPLICATED, Toast.LENGTH_LONG).show();
                    else if (!result.isSuccess())
                        Toast.makeText(RegisterAccountActivity.this, result.getError(), Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(RegisterAccountActivity.this, Constant.REGISTER_OK, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }
    
    public boolean isNickInputValid(String str) {
        // TODO: 닉네임 검사 기준 정해야함
        if (str.length() < 1 || str.length() > 20) return false;
        else return true;
    }
    public boolean isPasswordInputValid(String str){
        // TODO: 패스워드 검사 기준 정해야함
        if(str.length()<5 || str.length()>20) return false;
        else return true;
    }
    public boolean isPhoneNumInputValid(String str){
        // TODO: 임시 핸드폰번호 유효여부 확인 메소드 - 핸드폰인증 서비스 사용전까지 temp
        if(str.length()!=11) return false;
        return true;
    }

    // 서버에 가입 요청 보내기전 모든 입력이 완료되어있는지 최종체크하는 메서드
    public boolean isInputComplete(){
        if(isNickNameValid && isPasswordValid && isPasswordEquals && isPhoneNumValid && isMajorSet && isGradeSet)
            return true;
        else
            return false;
    }

}
