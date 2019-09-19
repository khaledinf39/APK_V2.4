package com.mpos.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mpos.Model.Transaction_Model;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DataBaseName = "mPOS.db";
    // First Table (Transaction_Table)
    public static final String TableName = "Transaction_Table";
    //Second Table (Reconciliation)
    public static final String Reconciliation_Table = "Reconciliation";
    //third Table lastSyncDateTime
    public static final String lastSyncDateTime = "lastSyncDateTimeTable";
    //Transaction Column
    public static final String Col1 = "ID";
    public static final String Col2 = "Amount";
    public static final String Col3 = "RRN";
    public static final String Col4 = "StartDateTime";
    public static final String Col5 = "Transaction_XML";

    // Reconciliation Column
    public static final String R_Col1 = "ID";
    public static final String R_Col2 = "DateTime";
    public static final String R_Col3 = "Reconciliation_XML";

    //lastSyncDateTime Column
    public static final String Sync_Col1 = "lastSyncDateTime";


    public DataBaseHelper(Context context) {
        super(context, DataBaseName, null, 4);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(new StringBuilder().append("create table ").append(lastSyncDateTime).append("  (").append(Sync_Col1).append(" TEXT )").toString());
        db.execSQL(new StringBuilder().append("create table ").append(TableName).append("  (").append(Col1).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ").append(Col2).append(" TEXT ,").append(Col3).append(" TEXT , ").append(Col4).append(" TEXT ,").append(Col5).append(" TEXT)").toString());
        db.execSQL(new StringBuilder().append("create table ").append(Reconciliation_Table).append("  (").append(R_Col1).append(" INTEGER PRIMARY KEY AUTOINCREMENT , ").append(R_Col2).append(" TEXT ,").append(R_Col3).append(" TEXT ) ").toString());


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(new StringBuilder().append("DROP TABLE IF EXISTS ").append(TableName).toString());
        sqLiteDatabase.execSQL(new StringBuilder().append("DROP TABLE IF EXISTS ").append(Reconciliation_Table).toString());
        sqLiteDatabase.execSQL(new StringBuilder().append("DROP TABLE IF EXISTS ").append(lastSyncDateTime).toString());
        onCreate(sqLiteDatabase);
    }

    public boolean InsertData(String Value1, String Value2, String Value3, String Value4, String Table_Name) {
        boolean res = false;
        Cursor selcetRes;
        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        switch (Table_Name) {
            case "Transaction_Table":
                contentValues.put(Col2, Value1); // as Amount
                contentValues.put(Col3, Value2); // as RRN
                contentValues.put(Col4, Value3); // sa StartDateTime
                contentValues.put(Col5, Value4); // sa Transaction XML
                selcetRes = db.rawQuery("SELECT * FROM " + Table_Name + " WHERE RRN = ?", new String[]{Value2});
                if (selcetRes.getCount() < 1) {
                    result = db.insert(TableName, null, contentValues);
                    if (result == -1) res = false;
                    else res = true;
                }

                break;
            case "Reconciliation":
                contentValues.put(R_Col2, Value1); // as DateTime
                contentValues.put(R_Col3, Value2); // as Reconciliation_XML

                selcetRes = db.rawQuery("SELECT * FROM " + Table_Name + " WHERE Reconciliation_XML = ?", new String[]{Value2});
                if (selcetRes.getCount() < 1) {
                    result = db.insert(Reconciliation_Table, null, contentValues);
                    if (result == -1) res = false;
                    else res = true;
                }
                break;

            case "lastSyncDateTime":
                contentValues.put(Sync_Col1, Value1); // as lastSyncDateTime
                    result = db.insert(lastSyncDateTime, null, contentValues);
                    if (result == -1) res = false;
                    else res = true;
                break;

        }
        return res;


    }

    public Cursor getAllData(String Table_Name, String from, String To) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("",null);

            switch (Table_Name)
            {
                case "Transaction_Table":
                case "Reconciliation":
                    res = db.rawQuery("select * from " + Table_Name + " where ID BETWEEN " + from + " AND " + To + " ORDER BY ID ASC", null);

                    break;
                case "lastSyncDateTimeTable" :
                    res = db.rawQuery("select * from " + Table_Name, null);
                    break;

            }
        return res;
    }

    public Cursor pokedRow(String Table_Name, int pos) {
        Cursor res;
        SQLiteDatabase db = this.getWritableDatabase();
        res = db.rawQuery("select * from " + Table_Name + " WHERE ID = ?", new String[]{Integer.toString(pos + 1)});
        return res;
    }

    public boolean deleteAllData(String Table_Name) {
        Cursor res;
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(Table_Name, "ID != ?", new String[]{"10000"});
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_Name + "'");
        if (result == 0) return false;
        else return true;
    }

    public void synk(ArrayList<Transaction_Model> synkData) {

        boolean result = false;
        for (Transaction_Model item : synkData) {
                result = InsertData(item.getAmount(), item.getRRN(), item.getDate(), "", "Transaction_Table");
        }


    }
    public void updateLastSyncDate(String dateTime ,String TableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE "+lastSyncDateTime +" SET lastSyncDateTime = "+dateTime ;
        db.execSQL(strSQL);
    }

    public void updateTransactionXML(String XML ,String RRN )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE Transaction_Table SET Transaction_XML = '"+XML + "' Where RRN =  "+RRN ;
        db.execSQL(strSQL);
    }

}

