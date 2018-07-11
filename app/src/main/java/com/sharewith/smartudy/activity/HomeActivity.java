package com.sharewith.smartudy.activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.sharewith.smartudy.directory.PaintBoard;
        import com.sharewith.smartudy.smartudy.R;
import com.sharewith.smartudy.utils.EndDrawerToggle;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private EndDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
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
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //액션바를 커스터마이징 하기 위해
        actionBar.setCustomView(R.layout.toolbar_home);
        actionBar.setElevation(0); //z축 깊이 0으로 설정 하여 그림자 없애기.
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);

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
                    Intent intent = new Intent(getApplicationContext(), QuestionListActivity.class);
                    //Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    intent.putExtra("categoryName", categories[idx]);
                    startActivity(intent);
                }
            });
        }
    }

}

