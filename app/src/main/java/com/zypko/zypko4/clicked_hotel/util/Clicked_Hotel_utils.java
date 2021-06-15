package com.zypko.zypko4.clicked_hotel.util;

import com.zypko.zypko4.json_Objects.Json_Food_Category;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.show_searched_station_result_hotels.util.Hotels_of_Searched_Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Clicked_Hotel_utils {

    private ArrayList<Hotel> hotels_ArrayList;
    private Hotel hotel;

    public ArrayList<Hotel> Get_details_of_clicked_hotel(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                hotels_ArrayList = new ArrayList<>();


                for (int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    int hotel_id                        = obj.getInt(Json_Hotels_of_Searched_Station.HOTEL_ID);
                    String hotel_name                   = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_NAME);
                    String hotel_image                  = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_IMAGE);
                    String hotel_category               = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_CATEGORY);
                    String hotel_address                = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_ADDRESS);
                    String hotel_rating                 = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_RATING);
                    String food_category_name           = obj.getString(Json_Food_Category.FOOD_CATEGORY_NAME);
                    String food_category_description    = obj.getString(Json_Food_Category.FOOD_CATEGORY_DESCRIPTION);

                    hotel = new Hotel();

                    hotel.setHotel_id                   (hotel_id);
                    hotel.setHotel_name                 (hotel_name);
                    hotel.setHotel_image                (hotel_image);
                    hotel.setHotel_category             (hotel_category);
                    hotel.setHotel_address              (hotel_address);
                    hotel.setHotel_rating               (hotel_rating);
                    hotel.setFood_category_name         (food_category_name);
                    hotel.setFood_category_description  (food_category_description);


                    hotels_ArrayList.add(i,hotel);

                }

                return hotels_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hotels_ArrayList;

    }


}
