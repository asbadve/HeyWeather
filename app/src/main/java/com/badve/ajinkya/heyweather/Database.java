package com.badve.ajinkya.heyweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.badve.ajinkya.heyweather.Models.City;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Ajinkya on 01-06-2015.
 */
public class Database {

    private static final String TAG = Database.class.getSimpleName();
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public Database(Context context){
        mDatabaseHelper = new DatabaseHelper(context);
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void insertCity(City city){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CITY_TITLE, city.getName()); // Contact Name
        // Inserting Row
        mDatabase.insert(mDatabaseHelper.TABLE_CITY, null, values);
        Log.d(TAG,"insert values"+city);
       // if(mDatabase.isOpen())
        //mDatabase.close(); // Closing database connection
    }
    public void insertCityList(ArrayList<City> mCityArrayList){
        String sql = "INSERT INTO " + DatabaseHelper.TABLE_CITY + " VALUES (?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < mCityArrayList.size(); i++) {
            City currentCity= mCityArrayList.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentCity.getName());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        Log.d("inserting entries ",""+ mCityArrayList.size() +""+ new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public boolean isCityPresent(){

        Cursor cur = mDatabase.rawQuery("SELECT COUNT(*) FROM "+DatabaseHelper.TABLE_CITY, null);
        if (cur != null) {
            cur.moveToFirst();                       // Always one row returned.
            if (cur.getInt (0) == 0) {               // Zero count means empty table.
               return true;
            }
        }

        return false;
    }

    public ArrayList<City> readCity(){
        ArrayList<City> currentCity = new ArrayList<>();

        String[] columns = {DatabaseHelper.COLUMN_UID,
                DatabaseHelper.COLUMN_CITY_TITLE,
        };

        Cursor cursor = mDatabase.query((DatabaseHelper.TABLE_CITY), columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("loading entries " ,""+ cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                City city = new City();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank movie object to contain our data
                city.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY_TITLE)));
                //add the City to the list of movie objects which we plan to return
                currentCity.add(city);
            }
            while (cursor.moveToNext());
        }
        return currentCity;
    }




    private static class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * Create a helper object to create, open, and/or manage a database.
         * This method always returns very quickly.  The database is not actually
         * created or opened until one of {@link #getWritableDatabase} or
         * {@link #getReadableDatabase} is called.
         *
         * @param context to use to open or create the database
         * @param name    of the database file, or null for an in-memory database
         * @param factory to use for creating cursor objects, or null for the default
         * @param version number of the database (starting at 1); if the database is older,
         *                {@link #onUpgrade} will be used to upgrade the database; if the database is
         *                newer, {@link #onDowngrade} will be used to downgrade the database
         */

        public static final String TABLE_CITY = "city";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_CITY_TITLE = "city_title";
        private static final String CREATE_TABLE_CITY = "CREATE TABLE " + TABLE_CITY + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CITY_TITLE + " TEXT" +
                ");";
        private static final String DB_NAME = "city_db";
        private static final int DB_VERSION = 1;
        private static final String TAG = DatabaseHelper.class.getSimpleName();
        private Context mContext;



        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        /**
         * Called when the database is created for the first time. This is where the
         * creation of tables and the initial population of the tables should happen.
         *
         * @param db The database.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE_CITY);
                Log.d(TAG,"create table box office executed");
            } catch (SQLiteException exception) {
                Log.d(TAG,"Exception"+exception);
                //L.t(mContext, exception + "");
            }

        }

        /**
         * Called when the database needs to be upgraded. The implementation
         * should use this method to drop tables, add tables, or do anything else it
         * needs to upgrade to the new schema version.
         * <p/>
         * <p>
         * The SQLite ALTER TABLE documentation can be found
         * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
         * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
         * you can use ALTER TABLE to rename the old table, then create the new table and then
         * populate the new table with the contents of the old table.
         * </p><p>
         * This method executes within a transaction.  If an exception is thrown, all changes
         * will automatically be rolled back.
         * </p>
         *
         * @param db         The database.
         * @param oldVersion The old database version.
         * @param newVersion The new database version.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                //L.m("upgrade table box office executed");
                Log.d(TAG,"upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_CITY+ " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                //L.t(mContext, exception + "");
                Log.d(TAG,"exception"+exception);
            }

        }
    }
}
