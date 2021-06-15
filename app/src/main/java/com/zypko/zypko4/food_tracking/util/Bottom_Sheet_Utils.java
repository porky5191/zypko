package com.zypko.zypko4.food_tracking.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.json_Objects.Json_Bottom_Sheet_Delivery_Boy;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.json_Objects.Json_User_Details;
import com.zypko.zypko4.sign_up.utils.User_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Bottom_Sheet_Utils extends ContextWrapper {

    private ArrayList<Bottom_Sheet> db_ArrayList;
    private Context context;

    public Bottom_Sheet_Utils(Context base) {
        super(base);
        this.context = base;
    }


    public ArrayList<Bottom_Sheet> Get_details_of_delivery_boy_and_address(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success == 1) {

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                db_ArrayList = new ArrayList<>();

                Log.e("OBJECT",""+object);

                //for (int i=0;i<array.length();i++){

                JSONObject obj = array.getJSONObject(0);

                //Log.e("SIZE_OBJ",""+obj);


                int order_table_id              = obj.getInt(Json_Bottom_Sheet_Delivery_Boy.ORDER_TABLE_ID);
                String coach                    = obj.getString(Json_Bottom_Sheet_Delivery_Boy.COACH);
                String seat_no                  = obj.getString(Json_Bottom_Sheet_Delivery_Boy.SEAT_NO);
                int delivery_boy_id             = obj.getInt(Json_Bottom_Sheet_Delivery_Boy.DELIVERY_BOY_ID);
                String delivery_boy_name        = obj.getString(Json_Bottom_Sheet_Delivery_Boy.DELIVERY_BOY_NAME);
                String delivery_boy_phone_no    = obj.getString(Json_Bottom_Sheet_Delivery_Boy.DELIVERY_BOY_PHONE_NO);
                String delivery_boy_image       = obj.getString(Json_Bottom_Sheet_Delivery_Boy.DELIVERY_BOY_IMAGE);
                String train_number             = obj.getString(Json_Bottom_Sheet_Delivery_Boy.TRAIN_NUMBER);
                String train_name               = obj.getString(Json_Bottom_Sheet_Delivery_Boy.TRAIN_NAME);
                String getDelivery_boy_rating   = obj.getString(Json_Bottom_Sheet_Delivery_Boy.DELIVERY_BOY_RATING);

                Bottom_Sheet bottom_sheet = new Bottom_Sheet();

                bottom_sheet.setOrder_table_id          (order_table_id);
                bottom_sheet.setCoach                   (coach);
                bottom_sheet.setSeat_no                 (seat_no);
                bottom_sheet.setDelivery_boy_id         (delivery_boy_id);
                bottom_sheet.setDelivery_boy_name       (delivery_boy_name);
                bottom_sheet.setDelivery_boy_phone_no   (delivery_boy_phone_no);
                bottom_sheet.setDelivery_boy_image      (delivery_boy_image);
                bottom_sheet.setTrain_number            (train_number);
                bottom_sheet.setTrain_name              (train_name);
                bottom_sheet.setDelivery_boy_rating(getDelivery_boy_rating);


                new PrefEditor(context).writeData(Json_Shared_Preference.DELIVERY_BOY_NAME      ,""+delivery_boy_name);
                new PrefEditor(context).writeData(Json_Shared_Preference.DELIVERY_BOY_ID        ,""+delivery_boy_id);
                new PrefEditor(context).writeData(Json_Shared_Preference.DELIVERY_BOY_PHONE_NO  ,""+delivery_boy_phone_no);
                new PrefEditor(context).writeData(Json_Shared_Preference.DELIVERY_BOY_IMAGE     ,""+delivery_boy_image);


                Log.e("SIZE_IN_1",""+delivery_boy_image);

                db_ArrayList.add(0, bottom_sheet);

                //  }

                //Log.e("SIZE_IN_2",""+user_ArrayList.size());

                return db_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR",e.toString());
        }

        return db_ArrayList;

    }


    public String[] Get_food_tracking_details(JSONObject object) {

        String[] tracking_details = new String[5];
        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success == 1) {

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);



                Log.e("OBJECT",""+object);

                JSONObject obj = array.getJSONObject(0);

                tracking_details[0]     = obj.getString("ORDER_ACCEPTED");
                tracking_details[1]     = obj.getString("COOCKING");
                tracking_details[2]     = obj.getString("PACKING");
                tracking_details[3]     = obj.getString("ORDER_PICKED_UP");
                tracking_details[4]     = obj.getString("DELIVERED");

                Log.e("OBJECT","<><>//><><>"+obj.getString("DELIVERED"));

                return tracking_details;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR",e.toString());
        }

        return tracking_details;

    }



}
