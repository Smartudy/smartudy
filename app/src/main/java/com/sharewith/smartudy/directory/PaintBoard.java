package com.sharewith.smartudy.directory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import com.sharewith.smartudy.utils.CustomPath;

import java.util.ArrayList;

/**
 * Created by Simjae on 2018-07-01.
 */

public class PaintBoard extends View {
        private ArrayList<CustomPath> paths = new ArrayList<CustomPath>();
        private int TOUCH_TOLERANCE = 4;
        private float currentX,currentY;
        private Canvas mCanvas;
        private Bitmap mBitmap;
        private Paint mDefaultPaint;
        private Paint mPaint;
        private Path mPath;
        float mX,mY;
        int currentColor;
        int strokeWidth;
        public static final int DEFAULT_COLOR = Color.BLACK;

        //new를 통해서 객체를 생성했을때 호출
        public PaintBoard(Context context) {
            this(context,null);
        }

        //XML에서 inflate 될때 호출되는 생성자.
        public PaintBoard(Context context, AttributeSet attrs){
            super(context,attrs);
        }

        //페인트 및 캔버스 초기화
    public void init(DisplayMetrics dm){
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //주변부 부드럽게
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 연결 부분 둥그렇게
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 끝 부분 둥그렇게
        strokeWidth = 16;
        currentColor = Color.BLACK;
        //mPaint.setXfermode(null);
        //mPaint.setAlpha(0xff);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888); //ARGB_8888설정 안하면 화면 검은색
        mCanvas = new Canvas(mBitmap);
    }

        //onDraw에서는 CustomPath 배열에 속한 path들을 그린다.
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.save(); // 캔버스에 이동/확대/축소/회전 등의 효과를 저장 후 복구하는 블럭
            for(CustomPath cp : paths){
                mPaint.setStrokeWidth(cp.getStrokeWidth());
                mPaint.setColor(cp.getColor());
                mCanvas.drawPath(cp.getPath(), mPaint);
            } // mCanvas에 연결된 mBitmap에 싹 그려주고

            canvas.drawBitmap(mBitmap,0,0,mDefaultPaint);
            canvas.restore();
        }

        /*
        처음 터치를 했을때 그 좌표가 mPath에 추가됨. ->touchStart
        그 다음에 터치를 움직이면 mPath에 곡선을 그림. -> touchMove
        마지막에 터치를 떼면 mPath에 라인을 그림. -> touchUp
        각 함수 내부에서 현재 터치점을 의미하는 mX,mY를 갱신함.
        그리고 mPath는 CustomPath(path 다른 속성을 포함하는 래퍼 클래스)의 배열에 추가됨.
        onDraw에서 CustomPath배열을 순회하면서 각 엘리먼트에 저장된 Path객체를 화면에 그림.
        * */
        public void touchStart(float x,float y){
           mPath = new Path();
           mPath.moveTo(x,y);
           CustomPath cpath = new CustomPath(currentColor,mPath,strokeWidth);
           paths.add(cpath);
           //touchStart에서 Path배열에 새로운 path가 추가 되어야 선을 그을때 바로바로 업데이트됨.
           mX = x;
           mY = y;
        }

        public void touchUp(float x,float y){
            mPath.lineTo(x,y);
        }

        public void touchMove(float x,float y){
            float dx = Math.abs(x-mX);
            float dy = Math.abs(y-mY);

            if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch(event.getAction()){
                case MotionEvent.ACTION_UP :
                    touchUp(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_DOWN :
                    touchStart(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE :
                    touchMove(x,y);
                    invalidate();
                    break;
            }
        return true;
    }
}
