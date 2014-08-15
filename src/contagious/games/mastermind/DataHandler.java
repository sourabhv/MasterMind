package contagious.games.mastermind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

public class DataHandler {

    public static abstract class Highscores implements BaseColumns {
        public static final String TABLE_NAME = "highscores";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_GUESSES = "guesses";
    }

    public class OpenHelper extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "mastermind.db";
        public static final int DATABASE_VERSION = 1;
        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Highscores.TABLE_NAME + " (" +
                    Highscores._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Highscores.COLUMN_NAME + " TEXT, " +
                    Highscores.COLUMN_TIME + " INTEGER, " +
                    Highscores.COLUMN_GUESSES + " INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Highscores.TABLE_NAME);
            onCreate(db);
        }
    }

    public volatile SQLiteDatabase db = null;
    public static DataHandler dataHandler = null;

    public DataHandler(Context context) {
        db = (new OpenHelper(context.getApplicationContext())).getWritableDatabase();
    }

    public static synchronized DataHandler getInstance(Context context) {
        if (dataHandler == null) dataHandler = new DataHandler(context);
        return dataHandler;
    }

    public long count() {
        SQLiteStatement s = db.compileStatement("SELECT count(*) from " + Highscores.TABLE_NAME);
        return (long) s.simpleQueryForLong();
    }

    public List<Map<String, Object>> selectAll() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Cursor cursor = db.query(Highscores.TABLE_NAME, new String[] { Highscores.COLUMN_NAME,
                Highscores.COLUMN_TIME, Highscores.COLUMN_GUESSES }, null, null, null, null,
                Highscores.COLUMN_TIME + " asc, " + Highscores.COLUMN_GUESSES + " asc");

        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(cursor.getColumnName(0), cursor.getString(0));
                map.put(cursor.getColumnName(1), cursor.getString(1));
                map.put(cursor.getColumnName(2), cursor.getString(2));
                list.add(map);
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) cursor.close();
        return list;
    }

    public long insert(String name, int time, int guesses) {
        ContentValues cv  = new ContentValues(3);
        cv.put(Highscores.COLUMN_NAME, name);
        cv.put(Highscores.COLUMN_TIME, time);
        cv.put(Highscores.COLUMN_GUESSES, guesses);
        return db.insert(Highscores.TABLE_NAME, null, cv);
    }

    public void deleteAll() {
        db.delete(Highscores.TABLE_NAME, null, null);
    }

}
