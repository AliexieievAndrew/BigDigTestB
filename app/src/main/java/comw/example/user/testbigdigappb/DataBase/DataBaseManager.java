package comw.example.user.testbigdigappb.DataBase;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

public class DataBaseManager {
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TIME_OF_USE = "timeOfUse";

    Context ctx;

    public DataBaseManager(Context ctx) {
        this.ctx = ctx;
    }

    final Uri LINK_URI = Uri
            .parse("content://comw.example.user.testbigdigappa.DataBase/linksImageTab");


    public void addToDataBase(final String link, final int status, final long timeOfUse) {
        Thread one = new Thread() {

            @Override
            public void run() {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_LINK, link);
                cv.put(COLUMN_STATUS,status);
                cv.put(COLUMN_TIME_OF_USE, timeOfUse);
                Uri uri = ctx.getContentResolver().insert(LINK_URI,cv);
            }
        };

        one.start();

    }

    public void editValueDataBase(final int id, final int status,final long timeOfUse) {
        Thread one = new Thread() {

            @Override
            public void run() {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_STATUS,status);
                cv.put(COLUMN_TIME_OF_USE, timeOfUse);
                Uri uri = ContentUris.withAppendedId(LINK_URI,id);
                int cnt = ctx.getContentResolver().update(uri,cv,null,null);
            }
        };

        one.start();
    }

    public void deleteValueDataBase(final int id){
        Thread one = new Thread() {
            @Override
            public void run() {
                Uri uri = ContentUris.withAppendedId(LINK_URI,id);
                int cnt = ctx.getContentResolver().delete(uri,null,null);
            }
        };
        one.start();
    }

    public String getLinkById(final int id){
        Uri uri= Uri
                .parse("content://comw.example.user.testbigdigappa.DataBase/linksImageTab/"+id);

        Cursor cursor = ctx.getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();

        return new String(cursor.getString(cursor.getColumnIndex(COLUMN_LINK)));
    }

    public int getStatus(final int id){
        Uri uri= Uri
                .parse("content://comw.example.user.testbigdigappa.DataBase/linksImageTab/"+id);

        Cursor cursor = ctx.getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        int result = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS));
        return result;
    }
}
