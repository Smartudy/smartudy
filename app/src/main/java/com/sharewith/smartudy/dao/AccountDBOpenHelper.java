package com.sharewith.smartudy.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharewith.smartudy.dto.AccountDto;

/**
 * Created by cheba on 2018-07-23.
 */

public class AccountDBOpenHelper {
    private static final String DATABASE_NAME = "accountDB.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DataBaseHelper mDBHelper;
    private Context mContext;

    private class DataBaseHelper extends SQLiteOpenHelper{
        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번 호출됨
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS accountDB (" +
                    "aTelNumber CHAR(15) PRIMARY KEY," +
                    "aNick CHAR(100)," +
                    "aPassword CHAR(100)," +
                    "aMajor CHAR(100)," +
                    "aGrade INTEGER);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS accountDB");
            onCreate(db);
        }
    }

    public AccountDBOpenHelper(Context context){this.mContext=context;};

    public AccountDBOpenHelper open() throws SQLException{
        mDBHelper = new DataBaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB=mDBHelper.getWritableDatabase();
        return this;
    }
    public void close(){ mDB.close(); }

    public long insertColumn(AccountDto account){
        ContentValues values = new ContentValues();
        values.put("aTelNumber", account.getPhone());
        values.put("aNick", account.getNick());
        values.put("aPassword", account.getPassword());
        values.put("aMajor", account.getMajor());
        values.put("aGrade", account.getGrade());
        return mDB.insert("accountDB", null, values);
    }

    public boolean deleteColumn(AccountDto account){
        return mDB.delete("accountDB", "aTelNumber="+account.getPhone(),null)>0;
    }

    public Cursor getAllColumns(){
        return mDB.query("accountDB",null, null,null,null,null,null);
    }

    public boolean checkIfRowExists(String telnumber, String password){
        Cursor cursor = mDB.query("accountDB", null, "aTelNumber=? AND aPassword=?", new String[]{telnumber, password},null,null,null);
        if(cursor.getCount()<1){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
