package com.zypko.zypko4.hotel_category_clicked.util;

import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Food_All_Details_utils {

    ArrayList<Food_of_Clicked_Category> food_ArrayList;
    Food_of_Clicked_Category foods;

    public ArrayList<Food_of_Clicked_Category> Get_all_Food_Details(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                food_ArrayList = new ArrayList<>();


                for (int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    int food_id             = obj.getInt(Json_Food_of_Clicked_Category.FOOD_ID);
                    String food_name        = obj.getString(Json_Food_of_Clicked_Category.FOOD_NAME);
                    String photo_of_food    = obj.getString(Json_Food_of_Clicked_Category.PHOTO_OF_FOOD);
                    String food_details     = obj.getString(Json_Food_of_Clicked_Category.FOOD_DETAILS);
                    String veg              = obj.getString(Json_Food_of_Clicked_Category.VEG);
                    String food_rating      = obj.getString(Json_Food_of_Clicked_Category.FOOD_RATING);
                    int food_price          = obj.getInt(Json_Food_of_Clicked_Category.FOOD_PRICE);
                    int food_discount       = obj.getInt(Json_Food_of_Clicked_Category.FOOD_DISCOUNT);
                    int food_taxes          = obj.getInt(Json_Food_of_Clicked_Category.FOOD_TAXES);
                    int food_packing_charge = obj.getInt(Json_Food_of_Clicked_Category.FOOD_PACKING_CHARGE);
                    String hotel_name       = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_NAME);
                    int hotel_id            = obj.getInt(Json_Hotels_of_Searched_Station.HOTEL_ID);
                    String hotel_status     = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_STATUS);
                    String food_category_name     = obj.getString(Json_Food_of_Clicked_Category.FOOD_CATEGORY_NAME);
                    String food_station_name     = obj.getString(Json_Hotels_of_Searched_Station.STATION_NAME);

                    foods = new Food_of_Clicked_Category();

                    foods.setFood_id(food_id);
                    foods.setFood_name(food_name);
                    foods.setPhoto_of_food(photo_of_food);
                    foods.setFood_details(food_details);
                    foods.setVeg(veg);
                    foods.setFood_price(food_price);
                    foods.setFood_rating(food_rating);
                    foods.setFood_discount(food_discount);
                    foods.setFood_taxes(food_taxes);
                    foods.setFood_packing_charge(food_packing_charge);
                    foods.setFood_category_name(food_category_name);
                    foods.setHotel_status(hotel_status);
                    foods.setHotel_id(hotel_id);
                    foods.setHotel_name(hotel_name);
                    foods.setStation_name(food_station_name);

                    food_ArrayList.add(i,foods);

                }

                return food_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return food_ArrayList;

    }

}
