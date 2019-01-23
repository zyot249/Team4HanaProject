package shyn.zyot.mytravels;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Team2SQLHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "travel_db";
    public static final String TABLE_NAME = "travel_diary";

    public Team2SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insert(){
        String queryInsert = "insert into  sinh_vien (full_name, year) values ('coca',24);";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(queryInsert);
    }
    public void getAll(String[] imgUri){
        String querySelect = "select * from  travel_diary" ;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(false,"travel_diary",null,null,null,null,null,null,null);
        int i = 0;
        while(cursor.moveToNext()){
            imgUri[i] =  cursor.getString(cursor.getColumnIndex("imgUri"));
            i++;
            Log.d("Success","as");
        }
    }

}
