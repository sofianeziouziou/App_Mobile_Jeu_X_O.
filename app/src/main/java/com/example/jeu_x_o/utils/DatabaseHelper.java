package com.example.jeu_x_o.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "tictactoe.db";
    public static final int DB_VERSION = 2; // CHANGER À 2

    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    public static final String TABLE_HISTORY = "history";
    public static final String COL_HISTORY_ID = "id";
    public static final String COL_USERNAME_HISTORY = "username"; // NOUVELLE COLONNE
    public static final String COL_WINNER = "winner";
    public static final String COL_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "(" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)";

        // AJOUTER LA COLONNE USERNAME DANS HISTORY
        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + "(" +
                COL_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME_HISTORY + " TEXT, " + // NOUVELLE COLONNE
                COL_WINNER + " TEXT, " +
                COL_DATE + " TEXT)";

        db.execSQL(createUsersTable);
        db.execSQL(createHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // MIGRATION : AJOUTER LA COLONNE USERNAME À LA TABLE HISTORY
            db.execSQL("ALTER TABLE " + TABLE_HISTORY + " ADD COLUMN " +
                    COL_USERNAME_HISTORY + " TEXT DEFAULT 'Anonyme'");
        } else {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
            onCreate(db);
        }
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, cv);
        return result != -1;
    }

    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " +
                        COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // MODIFIER saveGame POUR PRENDRE LE USERNAME
    public void saveGame(String username, String winner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME_HISTORY, username); // SAUVEGARDER LE USERNAME
        cv.put(COL_WINNER, winner);

        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        cv.put(COL_DATE, currentDate);

        db.insert(TABLE_HISTORY, null, cv);
    }

    // MÉTHODE EXISTANTE POUR COMPATIBILITÉ
    public void saveGame(String winner) {
        saveGame("Anonyme", winner); // VALEUR PAR DÉFAUT
    }

    public Cursor getHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + COL_HISTORY_ID + " DESC",
                null
        );
    }

    // NOUVELLE MÉTHODE POUR FILTRER PAR USERNAME
    public Cursor getHistoryByUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM " + TABLE_HISTORY +
                        " WHERE " + COL_USERNAME_HISTORY + "=?" +
                        " ORDER BY " + COL_HISTORY_ID + " DESC",
                new String[]{username}
        );
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + "=?",
                new String[]{username}
        );

        boolean taken = cursor.getCount() > 0;
        cursor.close();
        return taken;
    }
}