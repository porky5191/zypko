package com.zypko.zypko4.item_clicked.util;

import android.util.Log;

import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Hotel_All_Food_Utils {

    private ArrayList<Food_of_Clicked_Category> food_ArrayList;

    public ArrayList<Food_of_Clicked_Category> Get_all_Hotel_Foods(JSONObject object) {

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
                    int hotel_id            = obj.getInt(Json_Hotels_of_Searched_Station.HOTEL_ID);
                    int station_id          = obj.getInt(Json_Hotels_of_Searched_Station.STATION_ID);

                    Food_of_Clicked_Category foods = new Food_of_Clicked_Category();

                    foods.setFood_id(food_id);
                    foods.setFood_name(food_name);
                    foods.setPhoto_of_food(photo_of_food);
                    foods.setFood_details(food_details);
                    foods.setVeg(veg);
                    foods.setHotel_id(hotel_id);
                    foods.setFood_price(food_price);
                    foods.setStation_id(station_id);

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
