package my.edu.utar.assinment1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "passwords.db";
    private static final String TABLE_NAME = "passwords";

    public Database(Context context){
        super(context, DATABASE_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, website TEXT, username TEXT, password TEXT)");

        db.execSQL("INSERT INTO " + TABLE_NAME + " (website, username, password) VALUES ('Gmail', 'user1@gmail.com', 'pass1234')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (website, username, password) VALUES ('Facebook', 'fb_user', 'fb1234')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (website, username, password) VALUES ('UTAR WBLE', 'student123', 'utar1234')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (website, username, password) VALUES ('Bank', 'my_account', 'bank1234')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (website, username, password) VALUES ('GitHub', 'coder_pro', 'git1234')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
