package com.example.a0836527.tpfinal_cartes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static DatabaseHelper instance;
    public SQLiteDatabase database;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE scores (_id INTEGER PRIMARY KEY AUTOINCREMENT, score INTEGER);");
        insertScore(0, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE FROM scores;");
        onCreate(db);
    }

    private DatabaseHelper(Context context){
        super(context, "db", null, 1);
        ouvrirDB();
    }

    private void ouvrirDB(){
        database = this.getWritableDatabase();
    }

    public void fermerDB(){
        database.close();
    }

    public static DatabaseHelper getInstance(Context context){
        if(instance == null){
            instance = new DatabaseHelper(context.getApplicationContext());
        }

        return instance;
    }

    public void insertScore(int scoreInsert, SQLiteDatabase db) {
        ContentValues temp = new ContentValues();
        temp.put("score", scoreInsert);
        db.insert("scores", null, temp);
    }

    public void updateScore(int scoreUpdate, SQLiteDatabase db){
        ContentValues temp = new ContentValues();
        temp.put("score", scoreUpdate);
        db.update("scores", temp, "_id = 1", null);
    }

    public int fetchHighScore() {
        int highScore = 0;
        Cursor c = database.rawQuery("SELECT * FROM scores ORDER BY score DESC", null);

        if(c.moveToFirst()){
            highScore = c.getInt(1);
        }

        return highScore;
    }

}