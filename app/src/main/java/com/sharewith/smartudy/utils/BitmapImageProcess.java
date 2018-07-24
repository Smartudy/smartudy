package com.sharewith.smartudy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.sharewith.smartudy.smartudy.R;

import java.io.IOException;

/**
 * Created by Simjae on 2018-07-23.
 */

public class BitmapImageProcess {
    String ImagePath;
    private int IMAGE_WIDTH;
    private int IMAGE_HEIGHT;
    public BitmapImageProcess(String imagePath,int width,int height) {
        ImagePath = imagePath;
        IMAGE_WIDTH = width;
        IMAGE_HEIGHT = height;
    }

    public Bitmap getResizeImage(){
        Bitmap bitmap = null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(ImagePath,op); //이미지 사이즈 확인용
        //inJustDecodeBounds 속성 떄문에 bitmap에는 null이 들어가고 이미지의 width,height,type이 옵션에 저장됨.
        op.inSampleSize = calculateInSampleSize(op,IMAGE_WIDTH,IMAGE_HEIGHT);
        op.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(ImagePath,op);
        return bitmap;
    }

    public Bitmap setOrientation(Bitmap bitmap){ //이미지 메타데이터 활용 이미지 회전
        ExifInterface exif = null;
        Log.d("LOG","Bitmap Path is "+ ImagePath);
        try {
            exif = new ExifInterface(ImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL); //상수로 정의된 사진의 회전 각도
        int degree = OrientationToDegree(orientation); //현재 이미지의 회전 각도
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    public int OrientationToDegree(int orientation){
        if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if(orientation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;
        return 0;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int size = 1;
        int currentWidth = options.outWidth;
        int currentHeight = options.outHeight;
        Log.d("LOG","변경전 Width : "+currentWidth+" 변경전 Height : "+currentHeight);
        Log.d("LOG","이미지뷰 Width : "+reqWidth+" 이미지뷰 Height : "+reqHeight);
        while (currentWidth > reqWidth && currentHeight > reqHeight) {
            size *=2;
            currentWidth /= 2;
            currentHeight /= 2;
        }
        Log.d("LOG","변경된 Width : "+currentWidth+" 변경된 Height : "+currentHeight);
        Log.d("LOG","축소 비율은 : "+"1/"+size);
        return size;
    }
}
