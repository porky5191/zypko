package com.zypko.zypko4.order_details.urils;

import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Train_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Get_All_Train_Utils {


    private String[] all_trains;

    public String[] Get_train_details(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success > 0){

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);

                all_trains = new String[array.length()];

                for (int i=0;i<array.length();i++){


                    JSONObject obj = array.getJSONObject(i);

                    int train_id            = obj.getInt(Json_Train_Details.TRAIN_ID);
                    String train_name       = obj.getString(Json_Train_Details.TRAIN_NAME);
                    int train_number        = obj.getInt(Json_Train_Details.TRAIN_NUMBER);

                    all_trains[i] = train_number+"( "+train_name+" )";

                }

                return all_trains;

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return all_trains;

    }




}
