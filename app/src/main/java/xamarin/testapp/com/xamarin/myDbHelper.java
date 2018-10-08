package xamarin.testapp.com.xamarin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class myDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "xamarin.db";
    public myDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public myDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table xamarin_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,COUNTRY TEXT," +
                "MSG TEXT,VALUE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS xamarin_table");
        onCreate(db);
    }

    public void insertXamarinData(String cntry,String msg,String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("COUNTRY",cntry);
        contentValues.put("MSG",msg);
        contentValues.put("VALUE",value);
        String TABLE_NAME = "xamarin_table";

        Cursor res = db.rawQuery("select * from xamarin_table",null);
        if(res.getCount()!=0){
            //db.delete(TABLE_NAME,null,null);
            if (res != null && res.moveToFirst()) {
                do {
                    String id = res.getString(res.getColumnIndex("ID"));
                    db.update("xamarin_table", contentValues, "ID=" + id, null);
                }  while (res.moveToNext());
        }
        res.close();
        }
        else
        {
            long id = db.insert(TABLE_NAME,null ,contentValues);
        }
        Log.d("count", Integer.toString(res.getCount()));
    }
    public Cursor getXamarinData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from xamarin_table",null);
        Log.d(res.toString(), "getXamarinData: ");
        return res;
    }
    public void deleteXamarinData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("xamarin_table",null,null);
    }
}
