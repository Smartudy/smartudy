package com.sharewith.smartudy.activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.sharewith.smartudy.directory.PaintBoard;
        import com.sharewith.smartudy.smartudy.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //액션바를 커스터마이징 하기 위해
        actionBar.setCustomView(R.layout.toolbar_home);
        actionBar.setElevation(0); //z축 깊이 0으로 설정 하여 그림자 없애기.

        /* 페인트보드 잠시 접어두기.
        //페인트보드 내부에서 윈도우 사이즈를 구하기 위해 displayMetrics를 전달//
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //윈도우 매니저 -> 디스플레이 -> DisplayMetrics를 통해 화면 사이즈 가져오기//

        PaintBoard paintBoard = (PaintBoard)findViewById(R.id.PaintBoard);
        paintBoard.init(dm); //펜 스타일,굵기,색상 default로 초기화
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }
}

