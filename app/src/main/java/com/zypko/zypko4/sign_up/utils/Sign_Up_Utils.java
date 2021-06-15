package com.zypko.zypko4.sign_up.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.widget.Toast;

import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_User_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Sign_Up_Utils extends ContextWrapper {

    private ArrayList<User_Details> user_ArrayList;

    public Sign_Up_Utils(Context base) {
        super(base);
    }

    public ArrayList<User_Details> Get_details_of_user(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success == 1) {

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                user_ArrayList = new ArrayList<>();

                Log.e("OBJECT",""+object);

                //for (int i=0;i<array.length();i++){

                JSONObject obj = array.getJSONObject(0);

                //Log.e("SIZE_OBJ",""+obj);


                int user_id             = obj.getInt(Json_User_Details.USER_ID);
                String user_name        = obj.getString(Json_User_Details.USER_NAME);
                String user_image       = obj.getString(Json_User_Details.USER_IMAGE);
                String user_phone_no    = obj.getString(Json_User_Details.USER_PHONE_NO);
                String user_password    = obj.getString(Json_User_Details.USER_PASSWORD);
                String user_email       = obj.getString(Json_User_Details.USER_EMAIL);
                String user_referral    = obj.getString(Json_User_Details.USER_REFERRAL);

                User_Details user = new User_Details();

                user.setUser_id(user_id);
                user.setUser_name(user_name);
                user.setUser_image(user_image);
                user.setUser_phone_no(user_phone_no);
                user.setUser_password(user_password);
                user.setUser_email(user_email);
                user.setUser_referral(user_referral);


                 //Log.e("SIZE_IN_1",""+user_ArrayList.size());

                user_ArrayList.add(0, user);

                //  }

                //Log.e("SIZE_IN_2",""+user_ArrayList.size());

                return user_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR",e.toString());
        }

        return user_ArrayList;

    }

    public ArrayList<User_Details> Get_only_user_id(JSONObject object) {

        try {
            int success = object.getInt(Json_Objects.SUCCESS);

            if (success == 1) {

                JSONArray array = object.getJSONArray(Json_Objects.CONTENT);
                user_ArrayList = new ArrayList<>();

                Log.e("OBJECT",""+object);

                //for (int i=0;i<array.length();i++){

                JSONObject obj = array.getJSONObject(0);

                Log.e("SIZE_OBJ",""+obj);


                int user_id             = obj.getInt(Json_User_Details.USER_ID);


                User_Details user = new User_Details();

                user.setUser_id(user_id);

                //Log.e("SIZE_IN_1",""+user_ArrayList.size());

                user_ArrayList.add(0, user);

                //  }

                //Log.e("SIZE_IN_2",""+user_ArrayList.size());

                return user_ArrayList;

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR",e.toString());
        }

        return user_ArrayList;

    }

}
