package com.sharewith.smartudy.activity;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.view.Menu;
        import com.sharewith.smartudy.directory.PaintBoard;
        import com.sharewith.smartudy.smartudy.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //페인트보드 내부에서 윈도우 사이즈를 구하기 위해 displayMetrics를 전달//
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //윈도우 매니저 -> 디스플레이 -> DisplayMetrics를 통해 화면 사이즈 가져오기//

        PaintBoard paintBoard = (PaintBoard)findViewById(R.id.PaintBoard);
        paintBoard.init(dm); //펜 스타일,굵기,색상 default로 초기화


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }
}

