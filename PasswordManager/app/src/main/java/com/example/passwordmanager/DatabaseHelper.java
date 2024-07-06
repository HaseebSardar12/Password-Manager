package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PasswordManager.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE passwords (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, url TEXT, deleted INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS passwords");
        onCreate(db);
    }

    public long addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        return db.insert("users", null, contentValues);
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public long addPassword(String username, String password, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("url", url);
        return db.insert("passwords", null, contentValues);
    }

    public Cursor getPasswords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, username, password, url, deleted FROM passwords WHERE deleted = 0", null);
    }

    public Cursor getPassword(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, username, password, url FROM passwords WHERE id = ?", new String[]{String.valueOf(id)});
    }

    public void deletePassword(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deleted", 1);
        db.update("passwords", contentValues, "id = ?", new String[]{String.valueOf(id)});
    }

    public void restorePassword(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deleted", 0);
        db.update("passwords", contentValues, "id = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getDeletedPasswords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, username, password, url FROM passwords WHERE deleted = 1", null);
    }

    public void updatePassword(int id, String username, String password, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("url", url);
        db.update("passwords", contentValues, "id = ?", new String[]{String.valueOf(id)});
    }
}
