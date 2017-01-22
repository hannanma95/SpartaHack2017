package edu.msu.hannanma.spartahack;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DbTools extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Item_Info";
    // Contacts table name
    private static final String Table_Items = "Items";
    // Shops Table Columns names
    private static final String Key_Name = "Name";
    private static final String Key_Frequency ="Frequency";
    private static final String Key_Price = "Price";
    private static final String Key_Purchase_Date = "Purchase_Date";

    public DbTools(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String Create_Items_Table ="CREATE TABLE " + Table_Items + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Key_Name + " TEXT, " + Key_Frequency + " TEXT, "
        + Key_Price + " TEXT, " + Key_Purchase_Date + " TEXT)";
        db.execSQL(Create_Items_Table);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Table_Items);
        // Creating tables again
        onCreate(db);
    }

    // Adding new item
    public long addItem(EditActivity.Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_Name, item.name);
        values.put(Key_Frequency, item.freq);
        values.put(Key_Price, item.price);
        values.put(Key_Purchase_Date, item.purchaseDate);
        // Inserting Row
        long id = db.insert(Table_Items, null, values);
        db.close(); // Closing database connection
        return id;
    }

    // Getting All Items
    public List<EditActivity.Item> getAllItems() {
        List<EditActivity.Item> items = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + Table_Items;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EditActivity.Item item = new EditActivity.Item();
                item.id = cursor.getLong(0);
                item.name = cursor.getString(1);
                item.freq = cursor.getString(2);
                item.price = cursor.getString(3);
                item.purchaseDate = cursor.getString(4);
                // Adding contact to list
                items.add(item);
            } while (cursor.moveToNext());
        }
        // return contact list
        return items;
    }

    // Getting Needed Items
    public List<EditActivity.Item> getNeededItems() {
        List<EditActivity.Item> items = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + Table_Items;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EditActivity.Item item = new EditActivity.Item();
                item.id = cursor.getLong(0);
                item.name = cursor.getString(1);
                item.freq = cursor.getString(2);
                item.price = cursor.getString(3);
                item.purchaseDate = cursor.getString(4);
                if (item.purchaseDate.isEmpty()) {
                    items.add(item);
                    continue;
                }
                // Adding contact to list
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date purchaseDate;
                try {
                    purchaseDate = df.parse(item.purchaseDate);
                } catch (ParseException e) {
                    continue;
                }
                Calendar c = Calendar.getInstance();
                c.setTime(purchaseDate);
                c.add(Calendar.DATE, Integer.valueOf(item.freq));
                if (Calendar.getInstance().after(c))
                    items.add(item);
            } while (cursor.moveToNext());
        }
        // return contact list
        return items;
    }

    // Updating an item
    public int updateItem(EditActivity.Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Key_Name, item.name);
        values.put(Key_Frequency, item.freq);
        values.put(Key_Price, item.price);
        values.put(Key_Purchase_Date, item.purchaseDate);
        // updating row
        return db.update(Table_Items, values, "ID = ?",
                new String[]{String.valueOf(item.id)});
    }

    // Deleting an item
    public void deleteItem(EditActivity.Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Table_Items, "ID = ?",
                new String[] { String.valueOf(item.id) });
        db.close();
    }
}

