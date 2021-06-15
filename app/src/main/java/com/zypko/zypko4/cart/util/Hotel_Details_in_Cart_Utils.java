package com.zypko.zypko4.cart.util;

import com.zypko.zypko4.clicked_hotel.util.Hotel;
import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Hotel_Details_in_Cart_Utils {


    ArrayList<Hotel> hotel_ArrayList;
    Hotel hotel;

    public ArrayList<Hotel> Get_Hotel_Details(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                hotel_ArrayList = new ArrayList<>();


                for (int i=0;i<array.length();i++){

                    JSONObject obj = array.getJSONObject(i);

                    String hotel_name           = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_NAME);
                    String hotel_address        = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_ADDRESS);
                    String hotel_image          = obj.getString(Json_Hotels_of_Searched_Station.HOTEL_IMAGE);
                    int hotel_id                = obj.getInt(Json_Hotels_of_Searched_Station.HOTEL_ID);
                    String hotel_station        = obj.getString(Json_Hotels_of_Searched_Station.STATION_NAME);

                    hotel = new Hotel();

                    hotel.setHotel_name(hotel_name);
                    hotel.setHotel_image(hotel_image);
                    hotel.setHotel_address(hotel_address);
                    hotel.setHotel_id(hotel_id);
                    hotel.setHotel_station(hotel_station);

                    hotel_ArrayList.add(i,hotel);
                }
                return hotel_ArrayList;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hotel_ArrayList;
    }
}
