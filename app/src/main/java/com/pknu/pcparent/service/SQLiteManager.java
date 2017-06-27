package com.pknu.pcparent.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.pknu.pcparent.vo.SmsVO;

import java.util.List;

/**
 * Created by Hoon on 2017-06-13.
 */

public class SQLiteManager {

    private SQLiteDatabase sqliteDB = null;

    public final static String SQLITE_DB_NAME = "pcparent.db";
    public final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS SMS_T (SMS_NO TEXT, SMS_DATE TEXT, SMS_TEXT TEXT, LAT REAL, LNG REAL, ADDRESS TEXT)";
    public final static String SQL_INSERT = "INSERT INTO SMS_T (SMS_NO, SMS_DATE, SMS_TEXT, LAT, LNG, ADDRESS) VALUES ('";
    public final static String SQL_DELETE_ROWID = "DELETE FROM SMS_T WHERE rowid=";
    public final static String SQL_SELECT_ALL = "SELECT rowid, * FROM SMS_T ORDER BY rowid DESC";
    public final static String SQL_GET_TOP_ROWID = "SELECT rowid FROM SMS_T ORDER BY rowid DESC LIMIT 1";
    public final static String SQL_DELETE_ALL = "DELETE FROM SMS_T";
    public final static String SQL_DROP_TABLE = "DROP TABLE SMS_T";

    /** 생성자 **/
    public SQLiteManager() {
        try {
            /** SD카드가 있어야 사용 가능 **/
            this.sqliteDB = SQLiteDatabase.openOrCreateDatabase("/mnt/sdcard/"+SQLITE_DB_NAME, null);

            // 테이블 생성
            Log.d("SQLiteManager()", "sqlCreateTbl");
            this.sqliteDB.execSQL(SQL_CREATE_TABLE);

//            // 데이터 삭제
//            Log.d("SQLiteManager()", "sqlDelete");
//            this.sqliteDB.execSQL(SQL_DELETE_ALL);

//            // 테이블 삭제
//            Log.d("SQLiteManager()", "sqlDropTbl");
//            this.sqliteDB.execSQL(SQL_DROP_TABLE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    /***** 초기 SQLite 데이터를 읽은 후 리스트에 저장 *****/
    public void getAllData(List<SmsVO> list) {
        Cursor cursor = this.sqliteDB.rawQuery(SQL_SELECT_ALL, null);
        while(cursor.moveToNext()) {
            // SQLite로부터 읽은 데이터를 list에 추가.
            list.add(new SmsVO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6)));
        }
    }
    public void getAllData(List<SmsVO> list, String tag) {
        Log.d(tag, "sqlSelectAll");
        Cursor cursor = this.sqliteDB.rawQuery(SQL_SELECT_ALL, null);
        while(cursor.moveToNext()) {
            Log.d(tag, "SMS_T["+cursor.getInt(0)+"]"+": "+cursor.getString(1)+", "+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getDouble(4)+","+cursor.getDouble(5)+","+cursor.getString(6));
            // SQLite로부터 읽은 데이터를 list에 추가.
            list.add(new SmsVO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6)));
        }
    }

    /***** Top RowId 반환 *****/
    public int getTopRowId() {
        Cursor cursor = this.sqliteDB.rawQuery(SQL_GET_TOP_ROWID, null);
        int topRowId = -1;
        while(cursor.moveToNext()) topRowId = cursor.getInt(0);
        return topRowId;
    }
    public int getTopRowId(String tag) {
        Cursor cursor = this.sqliteDB.rawQuery(SQL_GET_TOP_ROWID, null);
        Log.d(tag, "sqlGetTopRowId");
        int topRowId = -1;
        while(cursor.moveToNext()) {
            topRowId = cursor.getInt(0);
            Log.d(tag, "topRowId: "+topRowId);
        }
        return topRowId;
    }

    /***** 데이터 추가 *****/
    public void insertData(String smsNumber, String smsDate, String smsText, Double lat, Double lng, String address) {
        this.sqliteDB.execSQL(SQL_INSERT + smsNumber + "', '" + smsDate + "', '" + smsText + "', " + lat + ", " + lng + ", '"+ address +"')");
    }
    public void insertData(String smsNumber, String smsDate, String smsText, Double lat, Double lng, String address, String tag) {
        Log.d(tag, "sqlInsert");
        this.sqliteDB.execSQL(SQL_INSERT + smsNumber + "', '" + smsDate + "', '" + smsText + "', " + lat + ", " + lng + ", '"+ address +"')");
    }

    /***** 데이터 삭제 *****/
    public void deleteData(int rowId) {
        this.sqliteDB.execSQL(SQL_DELETE_ROWID + rowId);
    }
    public void deleteData(int rowId, String tag) {
        Log.d(tag, "sqlDelete");
        this.sqliteDB.execSQL(SQL_DELETE_ROWID + rowId);
    }


    /***** Log - Select *****/
    public void logSelectAll() {
        Log.d("logSelectAll()", "sqlSelectAll");
        Cursor cursor = this.sqliteDB.rawQuery(SQL_SELECT_ALL, null);
        while(cursor.moveToNext())
            Log.d("logSelectAll()", "SMS_T["+cursor.getInt(0)+"]"+": "+cursor.getString(1)+", "+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getDouble(4)+","+cursor.getDouble(5)+","+cursor.getString(6));
    }
    public void logSelectAll(String tag) {
        Log.d(tag, "sqlSelectAll");
        Cursor cursor = this.sqliteDB.rawQuery(SQL_SELECT_ALL, null);
        while(cursor.moveToNext())
            Log.d("logSelectAll()", "SMS_T["+cursor.getInt(0)+"]"+": "+cursor.getString(1)+", "+cursor.getString(2)+"\n"+cursor.getString(3)+"\n"+cursor.getDouble(4)+","+cursor.getDouble(5)+","+cursor.getString(6));
    }


    public SQLiteDatabase getSQLiteDB() {
        return sqliteDB;
    }

}
