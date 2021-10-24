package tw.edu.yuntech.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class mySQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String db_name = "mcdonald.db";
    private final static int db_version = 1;
    private final static String tb_name = "foodlist";
    private SQLiteDatabase db;

    public mySQLiteOpenHelper(@Nullable Context context) {
        super(context,db_name,null,db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL = "CREATE TABLE IF NOT EXISTS "+ tb_name +
                "( " + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "name TEXT, " +
                "num INTEGER, " +
                "price INTEGER" + ");";
        db.execSQL(SQL);
        Log.d("created","sucessful");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS foodlist");
        onCreate(db);
    }

}
