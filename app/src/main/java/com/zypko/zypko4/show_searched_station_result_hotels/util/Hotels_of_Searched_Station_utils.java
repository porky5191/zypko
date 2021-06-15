package com.zypko.zypko4.show_searched_station_result_hotels.util;

import com.zypko.zypko4.json_Objects.Json_Food_Category;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.random_hotel.util.Food_category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Hotels_of_Searched_Station_utils {

    ArrayList<Hotels_of_Searched_Station> hotels_ArrayList;
    Hotels_of_Searched_Station hotels;


    public ArrayList<Hotels_of_Searched_Station> Get_all_hotels_of_searched_station(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                hotels_ArrayList = new ArrayList<>();


                for (int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    int hotel_id                = obj.getInt(Json_Hotels_of_Searched_Station.HOTEL_ID);
                    String hotel_name           = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_NAME);
                    String hotel_image          = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_IMAGE);
                    String hotel_category       = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_CATEGORY);
                    String hotel_station_name   = obj.getString(Json_Hotels_of_Searched_Station.STATION_NAME);
                    int hotel_station_id        = obj.getInt(Json_Hotels_of_Searched_Station.STATION_ID);

                    hotels = new Hotels_of_Searched_Station();

                    hotels.setHotel_id      (hotel_id);
                    hotels.setHotel_name    (hotel_name);
                    hotels.setHotel_image   (hotel_image);
                    hotels.setHotel_category(hotel_category);
                    hotels.setHotel_station (hotel_station_name);
                    hotels.setStation_id    (hotel_station_id);


                    hotels_ArrayList.add(i,hotels);

                }

                return hotels_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hotels_ArrayList;

    }

}
