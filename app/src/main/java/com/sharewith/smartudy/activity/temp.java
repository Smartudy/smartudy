package com.sharewith.smartudy.activity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sharewith.smartudy.dao.AccountDBOpenHelper;
import com.sharewith.smartudy.smartudy.R;

public class temp extends AppCompatActivity {

    private AccountDBOpenHelper mAccountDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAccountDBOpenHelper = new AccountDBOpenHelper(temp.this);
        try{
            mAccountDBOpenHelper.open();
        } catch (SQLException e){
            e.printStackTrace();
        }

        Cursor cursor = null;
        cursor = mAccountDBOpenHelper.getAllColumns();
        Log.i("tempTAG","account row count = "+cursor.getCount());
        String string = "";
        while(cursor.moveToNext())
        {
            string += "전화번호: ";
            string += cursor.getString(cursor.getColumnIndex("aTelNumber"));
            string += "\n닉네임: ";
            string += cursor.getString(cursor.getColumnIndex("aNick"));
            string += "\n비밀번호: ";
            string += cursor.getString(cursor.getColumnIndex("aPassword"));
            string += "\n전공: ";
            string += cursor.getString(cursor.getColumnIndex("aMajor"));
            string += "\n학년: ";
            string += Integer.toString(cursor.getInt(cursor.getColumnIndex("aGrade")));
            string += "\n\n";
        }
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
        ((TextView)findViewById(R.id.textview_temp)).setText(string);

        cursor.close();
    }

    @Override
    protected void onDestroy() {
        mAccountDBOpenHelper.close();
        super.onDestroy();
    }
}
