package com.example.bbcnewsreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bbcnewsreader.models.Article;

import java.util.ArrayList;

public class NewsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bbc_news.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FAVOURITES = "favourites";

    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_DATE = "pubDate";
    private static final String COL_LINK = "link";
    private static final String COL_SECTION = "section";

    public NewsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_FAVOURITES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_LINK + " TEXT UNIQUE, " +
                COL_SECTION + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        onCreate(db);
    }

    public long addFavourite(Article article) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, article.getTitle());
        values.put(COL_DESCRIPTION, article.getDescription());
        values.put(COL_DATE, article.getPubDate());
        values.put(COL_LINK, article.getLink());
        values.put(COL_SECTION, article.getSection());
        return db.insertWithOnConflict(TABLE_FAVOURITES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<Article> getAllFavourites() {
        ArrayList<Article> articles = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITES, null, null, null, null, null, COL_ID + " DESC");

        while (cursor.moveToNext()) {
            articles.add(new Article(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_LINK)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_SECTION))
            ));
        }
        cursor.close();
        return articles;
    }

    public int deleteFavourite(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_FAVOURITES, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
