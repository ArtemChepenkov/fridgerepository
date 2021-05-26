package com.example.fridge;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.util.Log;
import java.io.*;

public class DataBaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "userbase.db";
    static String DATABASE_PATH;
    static final String TABLE = "users";
    static final int SCHEMA = 1;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_DATE = "date";
    Context MyContext;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
        this.MyContext = context;
        DATABASE_PATH = context.getFilesDir().getPath() + DATABASE_NAME;
    }
    //+ COLUMN_YEAR + " INTEGER);");
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT, "
                + COLUMN_YEAR + " TEXT, "
                + COLUMN_DATE + " TEXT);");

        db.execSQL("ALTER TABLE users ADD date TEXT");
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    void create_db() {
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DATABASE_PATH);
            if (!file.exists()) {
                myInput = MyContext.getAssets().open(DATABASE_NAME);
                String outfileName = DATABASE_PATH;
                myOutput = new FileOutputStream(outfileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("DataBaseHelper", e.getMessage());
        } finally {
            try {
                if (myOutput != null) myOutput.close();
                if (myInput != null) myInput.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("DataBaseHelper", e.getMessage());
            }

        }

    }


    public SQLiteDatabase open() throws SQLException {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}