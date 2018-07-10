package com.sharewith.smartudy.utils;
import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sharewith.smartudy.smartudy.R;


public class EndDrawerToggle implements DrawerLayout.DrawerListener {

    private DrawerLayout drawerLayout;
    private DrawerArrowDrawable arrowDrawable;
    private AppCompatImageButton toggleButton;
    private String openDrawerContentDesc;
    private String closeDrawerContentDesc;

    public EndDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar,
                           int openDrawerContentDescRes, int closeDrawerContentDescRes) {

        this.drawerLayout = drawerLayout;
        this.openDrawerContentDesc = activity.getString(openDrawerContentDescRes);
        this.closeDrawerContentDesc = activity.getString(closeDrawerContentDescRes);

        arrowDrawable = new DrawerArrowDrawable(toolbar.getContext()); // 햄버거바 or 화살표 왔다갔다하게 하는 클래스
        arrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_END); // 화살표의 방향을 지정한다.(왼쪽방향)

        toggleButton = new AppCompatImageButton(toolbar.getContext(),null,
                R.attr.toolbarNavigationButtonStyle);//이전버전과 호환을 위해서 새롭게 생긴 이미지 버튼이다.
        toolbar.addView(toggleButton, new Toolbar.LayoutParams(GravityCompat.END));//기본 액션바드로어토글에는 GravityCompat.Start로 되어있어서 왼쪽에 나타나는 것이다.
        toggleButton.setImageDrawable(arrowDrawable);//이미지 버튼에 햄버거바 이미지를 붙임. 이미지버튼은 툴바에 붙어있음.
        toggleButton.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                toggle(); //드로어가 열려있으면 닫고 닫혀있으면 여는 리스너
                                            }
                                        }
        );
    }

    public void syncState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            setPosition(1f);
        }
        else {
            setPosition(0f);
        }
    }

    public void toggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    public void setPosition(float position) {
        if (position == 1f) {
            arrowDrawable.setVerticalMirror(true);
            toggleButton.setContentDescription(closeDrawerContentDesc);
        }
        else if (position == 0f) {
            arrowDrawable.setVerticalMirror(false);
            toggleButton.setContentDescription(openDrawerContentDesc);
        }
        arrowDrawable.setProgress(position);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        setPosition(Math.min(1f, Math.max(0, slideOffset)));
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        setPosition(1f);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        setPosition(0f);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

}