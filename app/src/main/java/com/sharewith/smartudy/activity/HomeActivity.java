package com.sharewith.smartudy.activity;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sharewith.smartudy.directory.PaintBoard;
import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.EndDrawerToggle;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private EndDrawerToggle mDrawerToggle;
    private final int PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        permissionCheck(); //카메라 접근 권한 획득을 위해
        // TODO: 임시 로그인 창 이동용 버튼. 수정해야한다!
        ((Button)findViewById(R.id.btn_home_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        // TODO: 여기까지!!

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        mDrawerLayout= (DrawerLayout)findViewById(R.id.home_drawer);

        mDrawerToggle = new EndDrawerToggle(this,mDrawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer){

            @Override //드로어가 열렸을때
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override //드로어가 닫혔을때
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };//액션바 드로우 토글 구현 -> 액션바에 있는 돼지바 클릭시 네비게이션 드로어 나타남.

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //액션바를 커스터마이징 하기 위해
//        actionBar.setCustomView(R.layout.toolbar_home);
//        actionBar.setElevation(0); //z축 깊이 0으로 설정 하여 그림자 없애기.
//        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        /* 페인트보드 잠시 접어두기.
        //페인트보드 내부에서 윈도우 사이즈를 구하기 위해 displayMetrics를 전달//
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //윈도우 매니저 -> 디스플레이 -> DisplayMetrics를 통해 화면 사이즈 가져오기//
        PaintBoard paintBoard = (PaintBoard)findViewById(R.id.PaintBoard);
        paintBoard.init(dm); //펜 스타일,굵기,색상 default로 초기화*/

        // 각 카테고리(과목) 선택 시에 적용될 OnClickListener를 적용시켜준다.
        setCategoryClickListener();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //여기에 액션바 메뉴 클릭 대한 이벤트를 처리하면 된다.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void setCategoryClickListener(){
        final String[] categories = {"Humanities", "Sociology", "Education", "Engineering", "Natural science", "Medical science", "Art&Physical studies", "8번째과목(미정)", "9번째과목(미정)"};
        for(int i=0;i<9;i++){
            final int idx = i;
            ImageButton btn = (ImageButton)findViewById(getResources().getIdentifier("ImageButton"+Integer.toString(i),"id",getPackageName()));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Snackbar.make(v, categories[idx], Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    // TODO : 선택 시 각 과목에 맞는 질문목록창으로 넘어가야한다. 재플린3번화면 xml 구성 후 다시 작업예정

                    //Intent intent = new Intent(getApplicationContext(), QuestionListActivity.class); 질문&답변창 레이아웃 구성 완료 : 2018.7.15 재철
                    //Intent intent = new Intent(getApplicationContext(),QnAActivity.class);

                    Intent intent = new Intent(getApplicationContext(), QuestionListActivity.class);
                    //Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    intent.putExtra("categoryName", categories[idx]);
                    startActivity(intent);
                }
            });
        }
    }

    public boolean currentpermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }

    public void permissionCheck(){
        //현재 앱에 카메라 권한이 있을 경우 PackageManager.PERMISSION_GRANTED or DENIED
        if(currentpermission() == false){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,WRITE_EXTERNAL_STORAGE)||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,READ_EXTERNAL_STORAGE)){
                //과거에 권한 요청을 거절한적있는경우 true를 리턴.
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(getString(R.string.permission_require_title));
                alertBuilder.setMessage(R.string.permission_require_message);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE,CAMERA}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}
                        ,PERMISSION_REQUEST_CODE);
                //거절한적 없는 경우 바로 권한을 요청하면 됨.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {
                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if(!flag) finish();
            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

