package com.zypko.zypko4.random_hotel.util;

import com.zypko.zypko4.json_Objects.Json_Food_Category;
import com.zypko.zypko4.json_Objects.Json_Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Random_Food_Category_utils {

    private ArrayList<Food_category> food_category_ArrayList;
    private Food_category food_category;

    public ArrayList<Food_category> Get_all_food_category(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                food_category_ArrayList = new ArrayList<>();


                for (int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    int id = obj.getInt(Json_Food_Category.FOOD_CATEGORY_ID);
                    String name = obj.getString(Json_Food_Category.FOOD_CATEGORY_NAME);
                    String description = obj.getString(Json_Food_Category.FOOD_CATEGORY_DESCRIPTION);
                    String photo = obj.getString(Json_Food_Category.FOOD_CATEGORY_PHOTO);

                    food_category = new Food_category();

                    food_category.setId(id);
                    food_category.setName(name);
                    food_category.setDescription(description);
                    food_category.setPhoto(photo);

                    food_category_ArrayList.add(i,food_category);

                }

                return food_category_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return food_category_ArrayList;

    }
}
