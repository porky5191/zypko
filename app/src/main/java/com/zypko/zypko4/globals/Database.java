package com.zypko.zypko4.globals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.zypko.zypko4.clicked_hotel.util.Food_Sqlite;
import com.zypko.zypko4.json_Objects.Json_History;
import com.zypko.zypko4.random_hotel.util.History;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private Context context;
    private SQLiteDatabase db;
    private SQLiteOpenHelper helper;

    public Database(Context context) {
        super(context, "sqlite_database", null, 9);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table_Name.SELECT_ITEMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, FOOD_ID INTEGER, FOOD_NAME varchar(20), VEG varchar(1), FOOD_ACTUAL_PRICE INTEGER,FOOD_MADE_PRICE INTEGER, TOTAL_PRICE INTEGER, FOOD_DISCOUNT INTEGER, FOOD_TAXES INTEGER, FOOD_PACKING_CHARGE INTEGER, TOTAL_ITEMS INTEGER, HOTEL_NAME varchar(30), HOTEL_ID INTEGER );");
        db.execSQL("CREATE TABLE " + Table_Name.SEARCHED_HISTORY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_ID INTEGER, SEARCHED_NAME varchar(30), SEARCHED_CATEGORY varchar(20) );");
        db.execSQL("CREATE TABLE " + Table_Name.FOOD_TRACKING + " (ORDER_TABLE_ID INTEGER PRIMARY KEY , ORDER_ACCEPTED varchar(1), COCKING varchar(1), PACKING varchar(1), PICKED_UP varchar(1), DELIVERED varchar(1) );");

        db.execSQL("INSERT INTO " + Table_Name.SEARCHED_HISTORY + " ( ITEM_ID,SEARCHED_NAME,SEARCHED_CATEGORY ) VALUES( 2, 'Paltan Bazar', '" + Json_History.STATION_NAME + "' ) ");
        db.execSQL("INSERT INTO " + Table_Name.SEARCHED_HISTORY + " ( ITEM_ID,SEARCHED_NAME,SEARCHED_CATEGORY ) VALUES( 1, 'Beverages', '" + Json_History.CATEGORY_NAME + "' ) ");
        db.execSQL("INSERT INTO " + Table_Name.SEARCHED_HISTORY + " ( ITEM_ID,SEARCHED_NAME,SEARCHED_CATEGORY ) VALUES( 2, '12 Aana Bengali Hotel', '" + Json_History.HOTEL_NAME + "' ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name.SELECT_ITEMS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name.SEARCHED_HISTORY + ";");
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name.FOOD_TRACKING + ";");

        onCreate(db);
    }


    private void insert_selected_item(int food_id, String food_name, String veg, int food_actual_price, int food_total_price, int food_made_price, int total_items, int food_discount, int food_taxes, int food_packing_charge, String hotel_name, int hotel_id) {

        try {

            helper = new Database(context);
            db = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("FOOD_ID", food_id);
            contentValues.put("FOOD_NAME", "" + food_name);
            contentValues.put("VEG", "" + veg);
            contentValues.put("FOOD_ACTUAL_PRICE", "" + food_actual_price);
            contentValues.put("FOOD_MADE_PRICE", "" + food_made_price);
            contentValues.put("TOTAL_PRICE", "" + food_total_price);
            contentValues.put("FOOD_DISCOUNT", "" + food_discount);
            contentValues.put("FOOD_TAXES", "" + food_taxes);
            contentValues.put("FOOD_PACKING_CHARGE", "" + food_packing_charge);
            contentValues.put("TOTAL_ITEMS", "" + total_items);
            contentValues.put("HOTEL_NAME", "" + hotel_name);
            contentValues.put("HOTEL_ID", "" + hotel_id);


            db.insert(Table_Name.SELECT_ITEMS, null, contentValues);

            //Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();

            db.close();

        } catch (SQLException ex) {
            //Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }

    }


    private void update_selected_item(int food_id, String food_name, String veg, int food_actual_price, int food_made_price, int food_total_price, int total_items, int food_discount, int food_taxes, int food_packing_charge, String hotel_name, int hotel_id) {

        try {
            helper = new Database(context);
            db = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put("TOTAL_PRICE", "" + food_total_price);
            contentValues.put("FOOD_DISCOUNT", "" + food_discount);
            contentValues.put("TOTAL_ITEMS", "" + total_items);
            contentValues.put("FOOD_TAXES", "" + food_taxes);
            contentValues.put("FOOD_PACKING_CHARGE", "" + food_packing_charge);


            db.update(Table_Name.SELECT_ITEMS, contentValues, "FOOD_ID=" + food_id, null);

            //Toast.makeText(context, "Updated..", Toast.LENGTH_SHORT).show();
            db.close();

        } catch (Exception ex) {
            Toast.makeText(context, "not Updated..", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "\n" + ex, Toast.LENGTH_LONG).show();
        }
    }


    public void check_selected_item(int food_id, String food_name, String veg, int food_actual_price, int food_made_price, int food_total_price, int total_items, int food_discount, int food_taxes, int food_packing_charge, String hotel_name, int hotel_id) {

        db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + Table_Name.SELECT_ITEMS + " WHERE FOOD_ID = " + food_id + " ;", null);

        int numRows = data.getCount();

        db.close();

        if (numRows != 0) {
            update_selected_item(food_id, food_name, veg, food_actual_price, food_made_price, food_total_price, total_items, food_discount, food_taxes, food_packing_charge, hotel_name, hotel_id);
        } else {
            insert_selected_item(food_id, food_name, veg, food_actual_price, food_made_price, food_total_price, total_items, food_discount, food_taxes, food_packing_charge, hotel_name, hotel_id);
        }
    }


    public ArrayList<Food_Sqlite> get_selected_item(int food_id) {

        db = this.getWritableDatabase();

        ArrayList<Food_Sqlite> arrayList = new ArrayList<>();

        Cursor data = db.rawQuery("SELECT * FROM " + Table_Name.SELECT_ITEMS + " WHERE FOOD_ID = " + food_id + " ;", null);

        int numRows = data.getCount();

        db.close();

        if (numRows == 0) {

            //Toast.makeText(context, "No Selected Items", Toast.LENGTH_SHORT).show();

        } else {
            int i = 0;
            while (data.moveToNext()) {
                Food_Sqlite food = new Food_Sqlite();

                food.settotal_items(data.getInt(data.getColumnIndexOrThrow("TOTAL_ITEMS")));
                food.setFood_total_price(data.getInt(data.getColumnIndexOrThrow("TOTAL_PRICE")));
                food.setFood_actual_price(data.getInt(data.getColumnIndexOrThrow("FOOD_ACTUAL_PRICE")));
                food.setFood_made_price(data.getInt(data.getColumnIndexOrThrow("FOOD_MADE_PRICE")));
                food.setFood_discount(data.getInt(data.getColumnIndexOrThrow("FOOD_DISCOUNT")));

                arrayList.add(i, food);
                i++;
            }

        }

        db.close();
        return arrayList;
    }


    public ArrayList<Food_Sqlite> get_all_selected_items() {

        db = this.getWritableDatabase();

        ArrayList<Food_Sqlite> arrayList = new ArrayList<>();

        Cursor data = db.rawQuery("SELECT SUM(TOTAL_PRICE) AS TOTAL_PRICE,SUM(TOTAL_ITEMS) AS TOTAL_ITEMS FROM " + Table_Name.SELECT_ITEMS + " ;", null);

        int numRows = data.getCount();

        db.close();

        if (numRows == 0) {

            //Toast.makeText(context, "No Selected Items", Toast.LENGTH_SHORT).show();

        } else {
            int i = 0;
            while (data.moveToNext()) {
                Food_Sqlite food = new Food_Sqlite();

                food.settotal_items(data.getInt(data.getColumnIndexOrThrow("TOTAL_ITEMS")));
                food.setFood_total_price(data.getInt(data.getColumnIndexOrThrow("TOTAL_PRICE")));


                arrayList.add(i, food);
                i++;
            }

        }

        db.close();
        return arrayList;
    }


    public ArrayList<Food_Sqlite> get_food_details_in_cart() {

        db = this.getWritableDatabase();

        ArrayList<Food_Sqlite> arrayList = new ArrayList<>();

        Cursor data = db.rawQuery("SELECT * FROM " + Table_Name.SELECT_ITEMS + " ;", null);

        int numRows = data.getCount();

        //db.close();

        if (numRows != 0) {
            int i = 0;
            while (data.moveToNext()) {
                Food_Sqlite food = new Food_Sqlite();

                food.setFood_id(data.getInt(data.getColumnIndexOrThrow("FOOD_ID")));
                food.setFood_name(data.getString(data.getColumnIndexOrThrow("FOOD_NAME")));
                food.setVeg(data.getString(data.getColumnIndexOrThrow("VEG")));
                food.setFood_actual_price(data.getInt(data.getColumnIndexOrThrow("FOOD_ACTUAL_PRICE")));
                food.setFood_made_price(data.getInt(data.getColumnIndexOrThrow("FOOD_MADE_PRICE")));
                food.setFood_total_price(data.getInt(data.getColumnIndexOrThrow("TOTAL_PRICE")));
                food.setFood_discount(data.getInt(data.getColumnIndexOrThrow("FOOD_DISCOUNT")));
                food.setFood_taxes(data.getInt(data.getColumnIndexOrThrow("FOOD_TAXES")));
                food.setFood_packing_charge(data.getInt(data.getColumnIndexOrThrow("FOOD_PACKING_CHARGE")));
                food.settotal_items(data.getInt(data.getColumnIndexOrThrow("TOTAL_ITEMS")));
                food.setHotel_name(data.getString(data.getColumnIndexOrThrow("HOTEL_NAME")));
                food.setHotel_id(data.getInt(data.getColumnIndexOrThrow("HOTEL_ID")));


                arrayList.add(i, food);
                i++;
            }

        }

        db.close();
        return arrayList;
    }


    public int[] get_total_items_and_total_amount() {

        db = this.getWritableDatabase();

        int[] array = new int[2];

        Cursor data = db.rawQuery("SELECT SUM(TOTAL_ITEMS) AS TOTAL_ITEMS,SUM(TOTAL_PRICE) AS TOTAL_AMOUNT FROM " + Table_Name.SELECT_ITEMS + "  ;", null);

        int numRows = data.getCount();

        db.close();

        if (numRows != 0) {
            data.moveToNext();

            array[0] = data.getInt(data.getColumnIndexOrThrow("TOTAL_ITEMS"));
            array[1] = data.getInt(data.getColumnIndexOrThrow("TOTAL_AMOUNT"));
        }

        db.close();

        return array;
    }


    public int[] get_total_packaging_charge_and_total_food_taxes() {

        db = this.getWritableDatabase();

        int[] array = new int[2];

        Cursor data = db.rawQuery("SELECT SUM(FOOD_PACKING_CHARGE) AS FOOD_PACKING_CHARGE,SUM(FOOD_TAXES) AS FOOD_TAXES FROM " + Table_Name.SELECT_ITEMS + "  ;", null);

        int numRows = data.getCount();

        db.close();

        if (numRows != 0) {
            data.moveToNext();

            array[0] = data.getInt(data.getColumnIndexOrThrow("FOOD_PACKING_CHARGE"));
            array[1] = data.getInt(data.getColumnIndexOrThrow("FOOD_TAXES"));
        }

        db.close();

        return array;
    }


    public int get_hotel_id_of_cart_items() {

        db = this.getWritableDatabase();

        int hotel_id = 0;

        Cursor data = db.rawQuery("SELECT HOTEL_ID FROM " + Table_Name.SELECT_ITEMS + "  ;", null);

        int numRows = data.getCount();

        db.close();

        if (numRows != 0) {
            data.moveToNext();

            hotel_id = data.getInt(data.getColumnIndexOrThrow("HOTEL_ID"));
        }

        db.close();

        return hotel_id;
    }


    public int[] get_food_id_of_cart_items() {

        db = this.getWritableDatabase();

        int[] food_id = new int[0];

        Cursor data = db.rawQuery("SELECT FOOD_ID FROM " + Table_Name.SELECT_ITEMS + "  ;", null);

        int numRows = data.getCount();

        db.close();

        if (numRows != 0) {
            food_id = new int[numRows];
            int i = 0;

            while (data.moveToNext()) {

                food_id[i] = data.getInt(data.getColumnIndexOrThrow("FOOD_ID"));

                i++;
            }

        }

        db.close();

        return food_id;
    }


    public ArrayList<Food_Sqlite> get_total_discount_and_total_amount() {

        db = this.getWritableDatabase();

        ArrayList<Food_Sqlite> arrayList = new ArrayList<>();

        Cursor data = db.rawQuery("SELECT FOOD_DISCOUNT,TOTAL_PRICE,TOTAL_ITEMS FROM " + Table_Name.SELECT_ITEMS + "  ;", null);

        int numRows = data.getCount();

        if (numRows != 0) {
            int i = 0;
            while (data.moveToNext()) {
                Food_Sqlite food = new Food_Sqlite();

                food.setFood_total_price(data.getInt(data.getColumnIndexOrThrow("TOTAL_PRICE")));
                food.setFood_discount(data.getInt(data.getColumnIndexOrThrow("FOOD_DISCOUNT")));
                food.settotal_items(data.getInt(data.getColumnIndexOrThrow("TOTAL_ITEMS")));
                arrayList.add(i, food);
                i++;
            }

        }
        db.close();

        return arrayList;
    }


//////////////////*********************************************************************************************************************************//////////////////////


    //  SEARCHED HISTORY


    public void insert_searched_item(String name, String category, int item_id) {

        if (check_history(name)) {
            try {

                helper = new Database(context);
                db = helper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put("SEARCHED_NAME", name);
                contentValues.put("SEARCHED_CATEGORY", category);
                contentValues.put("ITEM_ID", item_id);

                db.insert(Table_Name.SEARCHED_HISTORY, null, contentValues);

                //Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();

                db.close();

            } catch (SQLException ex) {
                //Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public ArrayList<History> get_history(String category) {

        db = this.getWritableDatabase();

        ArrayList<History> arrayList = new ArrayList<>();

        Cursor data = db.rawQuery("SELECT * FROM " + Table_Name.SEARCHED_HISTORY + " WHERE SEARCHED_CATEGORY = '" + category + "' ;", null);

        int numRows = data.getCount();

        db.close();

        if (numRows != 0) {
            int i = 0;
            while (data.moveToNext()) {
                History history = new History();

                history.setSearched_name(data.getString(data.getColumnIndexOrThrow("SEARCHED_NAME")));
                history.setSearched_category(data.getString(data.getColumnIndexOrThrow("SEARCHED_CATEGORY")));
                history.setItem_id(data.getInt(data.getColumnIndexOrThrow("ITEM_ID")));

                arrayList.add(i, history);
                i++;
            }

        }

        db.close();
        return arrayList;
    }


    public boolean check_history(String name) {

        db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + Table_Name.SEARCHED_HISTORY + " WHERE SEARCHED_NAME = '" + name + "' ;", null);

        int numRows = data.getCount();

        db.close();

        return numRows == 0;
    }


//////////////////********************************************  FOOD TRACKING  *************************************************************************************//////////////////////


    public void insert_food_tracking_details(int order_table_id) {

        try {

            helper = new Database(context);
            db = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("ORDER_TABLE_ID", order_table_id);
            contentValues.put("ORDER_ACCEPTED", "n");
            contentValues.put("COCKING", "n");
            contentValues.put("PACKING", "n");
            contentValues.put("PICKED_UP", "n");
            contentValues.put("DELIVERED", "n");


            db.insert(Table_Name.FOOD_TRACKING, null, contentValues);

            db.close();

        } catch (SQLException ex) {
        }

    }


    public void update_food_tracking_details(int order_table_id, String name, String value) {

        try {
            helper = new Database(context);
            db = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put("" + name, "" + value);


            db.update(Table_Name.FOOD_TRACKING, contentValues, "ORDER_TABLE_ID=" + order_table_id, null);

            db.close();

        } catch (Exception ex) {
        }
    }


    public String[] get_tracking_details(int order_table_id) {

        db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + Table_Name.FOOD_TRACKING + " WHERE ORDER_TABLE_ID = " + order_table_id + " ;", null);
        int numRows = data.getCount();

        String[] tracking_details = new String[5];

        if (numRows != 0) {

            data.moveToNext();


            tracking_details[0] = data.getString(data.getColumnIndexOrThrow("ORDER_ACCEPTED"));
            tracking_details[1] = data.getString(data.getColumnIndexOrThrow("COCKING"));
            tracking_details[2] = data.getString(data.getColumnIndexOrThrow("PACKING"));
            tracking_details[3] = data.getString(data.getColumnIndexOrThrow("PICKED_UP"));
            tracking_details[4] = data.getString(data.getColumnIndexOrThrow("DELIVERED"));
        }
        db.close();
        return tracking_details;
    }


//////////////////*********************************************************************************************************************************//////////////////////

    public void Clear_sqlite() {
        helper = new Database(context);
        db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM " + Table_Name.SELECT_ITEMS + " ;");

        db.close();
    }

    public void Clear_History() {
        helper = new Database(context);
        db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM " + Table_Name.SEARCHED_HISTORY + " ;");

        db.close();
    }


    public void Delete_sqlite_one_selected_row(int food_id) {
        helper = new Database(context);
        db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM " + Table_Name.SELECT_ITEMS + " WHERE FOOD_ID=" + food_id + " ;");

        db.close();
    }


    public void show_Sqlite() {

        db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + Table_Name.SELECT_ITEMS + ";", null);


        int numRows = data.getCount();
        if (numRows != 0) {

            while (data.moveToNext()) {

                int food_id             = data.getInt(data.getColumnIndexOrThrow("FOOD_ID"));
                String food_name        = data.getString(data.getColumnIndexOrThrow("FOOD_NAME"));
                int food_actual_price   = data.getInt(data.getColumnIndexOrThrow("FOOD_ACTUAL_PRICE"));
                int food_made_price     = data.getInt(data.getColumnIndexOrThrow("FOOD_MADE_PRICE"));
                int total_price         = data.getInt(data.getColumnIndexOrThrow("TOTAL_PRICE"));
                int total_discount      = data.getInt(data.getColumnIndexOrThrow("FOOD_DISCOUNT"));
                int total_tax           = data.getInt(data.getColumnIndexOrThrow("FOOD_TAXES"));
                int total_pack          = data.getInt(data.getColumnIndexOrThrow("FOOD_PACKING_CHARGE"));
                int total_items         = data.getInt(data.getColumnIndexOrThrow("TOTAL_ITEMS"));
                String hotel_name       = data.getString(data.getColumnIndexOrThrow("HOTEL_NAME"));
                int hotel_id            = data.getInt(data.getColumnIndexOrThrow("HOTEL_ID"));


                Log.d("SHOW_SQL", "" + food_id + "  n>" + food_name + "  a>" + food_actual_price + " m>" + food_made_price + " total>" + total_price + "  d>" + total_discount + " t>" + total_tax + " pac>" + total_pack + " i>" + total_items + "  " + hotel_name + "  " + hotel_id);
            }
        }

        db.close();
    }


    public void show_food_tracking() {

        db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + Table_Name.FOOD_TRACKING + ";", null);


        int numRows = data.getCount();
        if (numRows != 0) {

            while (data.moveToNext()) {

                int order_table_id = data.getInt(data.getColumnIndexOrThrow("ORDER_TABLE_ID"));
                String accept = data.getString(data.getColumnIndexOrThrow("ORDER_ACCEPTED"));
                String cocking = data.getString(data.getColumnIndexOrThrow("COCKING"));
                String packing = data.getString(data.getColumnIndexOrThrow("PACKING"));
                String picked_up = data.getString(data.getColumnIndexOrThrow("PICKED_UP"));
                String delivered = data.getString(data.getColumnIndexOrThrow("DELIVERED"));


                Log.d("SHOW_SQL", "" + order_table_id + "  n>" + accept + "  a>" + cocking + " m>" + packing + " l>" + picked_up + "  d>" + delivered);
            }
        }

        db.close();
    }


}
