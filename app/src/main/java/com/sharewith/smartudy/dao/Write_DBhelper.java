package com.sharewith.smartudy.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharewith.smartudy.dto.NotePadDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simjae on 2018-07-19.
 */

public class Write_DBhelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "sharewith.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME ="NOTEPAD";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENTS = "contents";
    //이렇게 하는 이유는 칼럼명&테이블명이 변경 되었을때 일일이 모든 코드를 수정하는일을 방지하기 위함.

    public Write_DBhelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE_NAME+"("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_TITLE+" TEXT, "+COL_CONTENTS+" TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }

    public void insertNotePad(NotePadDto dto){
        SQLiteDatabase db = getWritableDatabase();
        String title = dto.getTitle();
        String contents = dto.getContents();
        String sql = "INSERT INTO "+TABLE_NAME+" ("+COL_TITLE+","+COL_CONTENTS+") VALUES('"+title+"','"+contents+"');";
        db.execSQL(sql);
    }

    public List<NotePadDto> selectAllNotePad(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME;
        ArrayList<NotePadDto> notepads = new ArrayList<NotePadDto>();
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            String title = cursor.getString(1);
            String contents = cursor.getString(2);
            NotePadDto notepad = new NotePadDto(title,contents);
            notepads.add(notepad);
        }
        return notepads;
    }

    public NotePadDto findNotePadByTitle(String title){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+COL_TITLE+" = "+"'"+title+"'";
        Cursor cursor = db.rawQuery(sql,null);
        NotePadDto notepad = null;
        while(cursor.moveToNext()){
            if(!cursor.getString(1).equals(title)) continue;
            else{
                notepad = new NotePadDto();
                notepad.setTitle(cursor.getString(1));
                notepad.setContents(cursor.getString(2));
            }
        }
        return notepad;
    }


}
