package com.dvishapps.yourshop.ui.layout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.dvishapps.yourshop.models.RecycleItem;
import com.dvishapps.yourshop.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class ShopListDb {

    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public final static String SHOP_LIST_TABLE = "ShopList";

    public final static String SHOP_NO = "_id";
    public final static String SHOP_ID = "shop_id";
    public final static String SHOP_NAME = "shop_name";
    public final static String IMG_URL = "img_url";
    public static String COLUMN_IMAGE_BITMAP = "imageBitmap";

    /**
     * @param context
     */
    public ShopListDb(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long insertRecords(String id, String name, String imgUrl) {
        ContentValues values = new ContentValues();
        values.put(SHOP_ID, id);
        values.put(SHOP_NAME, name);
        values.put(IMG_URL, imgUrl);
        return database.insert(SHOP_LIST_TABLE, null, values);
    }


//    public long updateRecords(String id, String name, String imgUrl) {
//        ContentValues values = new ContentValues();
//        values.put(SHOP_ID, id);
//        values.put(SHOP_NAME, name);
//        values.put(IMG_URL, imgUrl);
//        return database.update(SHOP_LIST_TABLE,  values);
//    }

    public Cursor selectRecords() {
        String[] cols = new String[]{SHOP_ID};
        Cursor mCursor = database.query(true, SHOP_LIST_TABLE, cols, null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public List<RecycleItem> getAllData() {
        List<RecycleItem> list = new ArrayList<>();
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + SHOP_LIST_TABLE, null);

        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                RecycleItem recycleItem = new RecycleItem();
                recycleItem.setShopId(cursor.getString(cursor.getColumnIndex(SHOP_ID)));
                recycleItem.setName(cursor.getString(cursor.getColumnIndex(SHOP_NAME)));
                recycleItem.setId(cursor.getString(cursor.getColumnIndex(SHOP_NO)));
                recycleItem.setImageUrl(cursor.getString(cursor.getColumnIndex(IMG_URL)));

                list.add(recycleItem);
                cursor.moveToNext();
            }
        }

        return list;
    }

    public String getName(String shopId) {
        database = dbHelper.getReadableDatabase();
//        Cursor cursor = database.rawQuery("select * from " + SHOP_LIST_TABLE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM " + SHOP_LIST_TABLE + " WHERE " + SHOP_ID + " = ?", new String[]{shopId});
        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(SHOP_NAME));
        }

        return name;
    }

    public void insertImage(String shopId, Bitmap bitmap) {
        database = dbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_IMAGE_BITMAP, Tools.getByteArray(bitmap));
        String strFilter = SHOP_ID + "='" + shopId+"'";

        database.update(SHOP_LIST_TABLE, contentValues, strFilter, null);
    }


    public Bitmap getImageWithShopId(String shopId) {

        database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + SHOP_LIST_TABLE + " WHERE " + SHOP_ID + " = ?", new String[]{shopId,});

        if (cursor.moveToFirst()) {
            do {
                return Tools.getBitmapFromByteArray(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_BITMAP)));
            } while (cursor.moveToNext());
        }else{
            return null;
        }
    }

    public boolean CheckIsShopExist(String shopId) {
        SQLiteDatabase sqldb = dbHelper.getReadableDatabase();
        String Query = "Select * from " + SHOP_LIST_TABLE + " where " + SHOP_ID + " = '" + shopId + "'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


}