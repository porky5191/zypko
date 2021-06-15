package com.zypko.zypko4.clicked_random_food.utils;

import android.util.Log;

import com.zypko.zypko4.json_Objects.Json_Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.show_searched_station_result_hotels.util.Hotels_of_Searched_Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Food_of_Clicked_Category_Utils {

    private ArrayList<Food_of_Clicked_Category> food_ArrayList;

    public ArrayList<Food_of_Clicked_Category> Get_all_Food_clicked_Category(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                food_ArrayList = new ArrayList<>();

                Log.d("URL.",object.toString());

                for (int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    int food_id             = obj.getInt(Json_Food_of_Clicked_Category.FOOD_ID);
                    String food_name        = obj.getString(Json_Food_of_Clicked_Category.FOOD_NAME);
                    String photo_of_food    = obj.getString(Json_Food_of_Clicked_Category.PHOTO_OF_FOOD);
                    String food_details     = obj.getString(Json_Food_of_Clicked_Category.FOOD_DETAILS);
                    String veg              = obj.getString(Json_Food_of_Clicked_Category.VEG);
                    int food_price          = obj.getInt(Json_Food_of_Clicked_Category.FOOD_PRICE);
                    String hotel_name       = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_NAME);
                    int hotel_id            = obj.getInt(Json_Hotels_of_Searched_Station.HOTEL_ID);
                    String station_name     = obj.getString(Json_Food_of_Clicked_Category.STATION_NAME);
                    int station_id          = obj.getInt(Json_Hotels_of_Searched_Station.STATION_ID);
                    int packaging_charge    = obj.getInt(Json_Food_of_Clicked_Category.FOOD_PACKING_CHARGE);
                    int food_taxes          = obj.getInt(Json_Food_of_Clicked_Category.FOOD_TAXES);
                    int food_discount       = obj.getInt(Json_Food_of_Clicked_Category.FOOD_DISCOUNT);
                    String offer_by_us      = obj.getString(Json_Food_of_Clicked_Category.OFFER_BY_US);
                    String text_above_image = obj.getString(Json_Food_of_Clicked_Category.TEXT_ABOVE_IMAGE);

                    Food_of_Clicked_Category foods = new Food_of_Clicked_Category();

                    foods.setFood_id(food_id);
                    foods.setFood_name(food_name);
                    foods.setPhoto_of_food(photo_of_food);
                    foods.setFood_details(food_details);
                    foods.setVeg(veg);
                    foods.setHotel_id(hotel_id);
                    foods.setFood_price(food_price);
                    foods.setHotel_name(hotel_name);
                    foods.setStation_name(station_name);
                    foods.setStation_id(station_id);
                    foods.setFood_packing_charge(packaging_charge);
                    foods.setFood_taxes(food_taxes);
                    foods.setFood_discount(food_discount);
                    foods.setOffer_by_us(offer_by_us);
                    foods.setText_above_image(text_above_image);

                    food_ArrayList.add(i, foods);

                }

                Log.d("URL..",""+food_ArrayList.size());

                return food_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("URL..c",""+e.toString());
        }

        return food_ArrayList;

    }

}
