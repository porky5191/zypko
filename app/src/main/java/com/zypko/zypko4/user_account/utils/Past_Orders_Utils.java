package com.zypko.zypko4.user_account.utils;

import android.util.Log;

import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Past_Order_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Past_Orders_Utils {

    private ArrayList<Past_Order> past_orders_ArrayList;
    private ArrayList<Recent_Order> recent_orders_ArrayList;

    public ArrayList<Past_Order> Get_Past_Orders_details(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                past_orders_ArrayList = new ArrayList<>();


                for (int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    int order_table_id          = obj.getInt(Json_Past_Order_Details.ORDER_TABLE_ID);
                    String hotel_name           = obj.getString(Json_Past_Order_Details.HOTEL_NAME);
                    String hotel_address        = obj.getString(Json_Past_Order_Details.HOTEL_ADDRESS);
                    String station_name         = obj.getString(Json_Past_Order_Details.STATION_NAME);
                    String delivered_time       = obj.getString(Json_Past_Order_Details.DELIVERED_TIME);
                    String order_date           = obj.getString(Json_Past_Order_Details.ORDER_DATE);
                    String delivered            = obj.getString(Json_Past_Order_Details.DELIVERED);
                    String paid_amount          = obj.getString(Json_Past_Order_Details.PAID_AMOUNT);
                    String total_items          = obj.getString(Json_Past_Order_Details.TOTAL_ITEMS);
                    String paid_by_cash         = obj.getString(Json_Past_Order_Details.PAY_BY_CASH_ONLINE);
                    String order_accepted       = obj.getString(Json_Past_Order_Details.ORDER_ACCEPTED);

                    Past_Order past_order = new Past_Order();

                    past_order.setOrder_table_id(order_table_id);
                    past_order.setHotel_name(hotel_name);
                    past_order.setHotel_address(hotel_address);
                    past_order.setStation_name(station_name);
                    past_order.setDelivered_time(delivered_time);
                    past_order.setOrder_date(order_date);
                    past_order.setDelivered(delivered);
                    past_order.setPaid_amount(paid_amount);
                    past_order.setTotal_items(total_items);
                    past_order.setPay_by_cash_online(paid_by_cash);
                    past_order.setOrder_accepted(order_accepted);

                    past_orders_ArrayList.add(i, past_order);

                }

                return past_orders_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return past_orders_ArrayList;

    }


    public ArrayList<Recent_Order> Get_Recent_Orders_details(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                recent_orders_ArrayList = new ArrayList<>();

                Log.e("size"," .  "+array.length());
                //Log.e("size","");

                for (int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    int order_table_id          = obj.getInt(Json_Past_Order_Details.ORDER_TABLE_ID);
                    String hotel_name           = obj.getString(Json_Past_Order_Details.HOTEL_NAME);
                    String hotel_address        = obj.getString(Json_Past_Order_Details.HOTEL_ADDRESS);
                    String station_name         = obj.getString(Json_Past_Order_Details.STATION_NAME);
                    String paid_amount          = obj.getString(Json_Past_Order_Details.PAID_AMOUNT);
                    String total_items          = obj.getString(Json_Past_Order_Details.TOTAL_ITEMS);
                    String paid_by_cash         = obj.getString(Json_Past_Order_Details.PAY_BY_CASH_ONLINE);

                    String partially_paid_amount   = obj.getString(Json_Past_Order_Details.PARTIALLY_PAID_AMOUNT);
                    String amount_left             = obj.getString(Json_Past_Order_Details.AMOUNT_LEFT);
                    String percent_paid            = obj.getString(Json_Past_Order_Details.PERCENT_PAID);

                    Recent_Order recent_order = new Recent_Order();

                    recent_order.setOrder_table_id(order_table_id);
                    recent_order.setHotel_name(hotel_name);
                    recent_order.setHotel_address(hotel_address);
                    recent_order.setStation_name(station_name);
                    recent_order.setPaid_amount(paid_amount);
                    recent_order.setTotal_items(total_items);
                    recent_order.setPay_by_cash_online(paid_by_cash);
                    recent_order.setPartially_paid_amount(partially_paid_amount);
                    recent_order.setAmount_left(amount_left);
                    recent_order.setPercent_paid(percent_paid);

                    recent_orders_ArrayList.add(i, recent_order);

                }

                Log.e("size"," .>  "+recent_orders_ArrayList.size());
                return recent_orders_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recent_orders_ArrayList;

    }



}
