package com.example.ebtapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseBack extends SQLiteOpenHelper {

    private static final String USUARIO_TABLE_CREATE = "CREATE TABLE usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, idUsuario INTEGER, fullname TEXT, usuario TEXT, foto TEXT, idTipo INTEGER)";
    private static final String DB_NAME = "dbRespaldo.sqlite";
    private static final int DB_VERSION = 1;

    public DataBaseBack(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USUARIO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
