package com.dvishapps.yourshop.ui.layout.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.dvishapps.yourshop.ui.layout.database.ShopListDb.COLUMN_IMAGE_BITMAP;
import static com.dvishapps.yourshop.ui.layout.database.ShopListDb.SHOP_LIST_TABLE;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "your_shop";

    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + SHOP_LIST_TABLE + "( _id integer primary key ," +
            "shop_id text not null," +
            "shop_name text not null ," +
            COLUMN_IMAGE_BITMAP + " BLOB ," +
            "img_url text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + SHOP_LIST_TABLE);
        onCreate(database);
    }
}