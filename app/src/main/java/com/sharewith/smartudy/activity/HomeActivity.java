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
import static android.Manifest.permission.RECORD_AUDIO;
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


        temp(); // TODO: 임시 버튼들.

        setToolbarAndDrawerAttrs();

        // 각 카테고리(과목) 선택 시에 적용될 OnClickListener를 적용시켜준다.
        setCategoryClickListener();

    }

    public void setToolbarAndDrawerAttrs(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.toolbar_menu_bell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "alarm not implemented", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.findViewById(R.id.toolbar_menu_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerToggle.toggle();
            }
        });

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
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
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
        return super.onOptionsItemSelected(item);
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
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
        ContextCompat.checkSelfPermission(this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }

    public void permissionCheck(){
        final String[] permissions = {WRITE_EXTERNAL_STORAGE
                , READ_EXTERNAL_STORAGE,CAMERA,RECORD_AUDIO};
        //현재 앱에 카메라 권한이 있을 경우 PackageManager.PERMISSION_GRANTED or DENIED
        if(!currentpermission()){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,WRITE_EXTERNAL_STORAGE)||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,RECORD_AUDIO)){
                //과거에 권한 요청을 거절한적있는경우 true를 리턴.
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(getString(R.string.permission_require_title));
                alertBuilder.setMessage(R.string.permission_require_message);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(HomeActivity.this, permissions, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            }else{
                ActivityCompat.requestPermissions(this, permissions ,PERMISSION_REQUEST_CODE);
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
                        Log.d("LOG",permissions[i] + " 권한이 거부 되었습니다.");
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

    public void temp(){
        ((Button)findViewById(R.id.btn_home_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        ((Button)findViewById(R.id.btn_home_level)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HonorLevelActivity.class);
                startActivity(intent);
            }
        });
        ((Button)findViewById(R.id.btn_home_show_accounts)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), temp.class);
                startActivity(intent);
            }
        });
    }
}

