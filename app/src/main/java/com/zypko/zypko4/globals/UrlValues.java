package com.zypko.zypko4.globals;

public interface UrlValues {


    //String SERVER = "https://jestii.com/ZYPKO/";
    String SERVER = "https://zypkofoods.com/ZYPKO/";


    //***************************************************************************************************************//
    // Images
    String IMAGE_URL = SERVER+"image/";

    // food category
    String FOOD_CATEGORY_IMAGE_URL = SERVER+"image/food_category/";
    //Hotel
    String HOTEL_IMAGE_URL = SERVER+"image/station/";



    //hotel category
    String HOTEL_CATEGORY_IMAGE_URL = SERVER+"image/station/";

    //***************************************************************************************************************//


    //// php

    String FOOD_CATEGORY_PHP_URL = SERVER+"php/user/RandomFoodCategory.php";

    String HOTELS_OF_SEARCHED_STATION_PHP_URL = SERVER+"php/user/Hotels_of_Searched_Station.php";


    String CLICKED_FOOD_CATEGORY_AND_STATION_SHOW_RESULT_PHP_URL = SERVER+"php/user/Clicked_Food_Category_and_Station_Show_Result.php";

    String CLICKED_FOOD_CATEGORY_FROM_HOTEL_DETAILS_PHP_URL = SERVER+"php/user/Clicked_Food_Category_from_Hotel_Details.php";
    String GET_ALL_FOOD_OF_HOTEL = SERVER+"php/user/Hotel_All_Food_Details.php";


    String SEARCH_STATION_SUGGESTION = SERVER+"php/user/Load_station_names.php";

    String SEARCH_HOTEL_SUGGESTION = SERVER+"php/user/Hotel_Suggestion.php";
    String SEARCH_FOOD_SUGGESTION = SERVER+"php/user/Food_Suggestion.php";
    String SEARCH_CATEGORY_SUGGESTION = SERVER+"php/user/Food_Category_Suggestion.php";

    String CLICKED_HOTEL_SHOW_RESULT = SERVER+"php/user/Clicked_Hotel_Show_Result.php";

    String CLICKED_FOOD_GET_ALL_DETAILS = SERVER+"php/user/Clicked_Food_Get_All_Details.php";

    String GET_HOTEL_DETAILS = SERVER+"php/user/Get_Hotel_Details.php";


    // user account past order and recent order
    String GET_PAST_ORDER_DETAILS = SERVER+"php/user/Get_Past_Orders_Of_User.php";
    String GET_RECENT_ORDER_DETAILS = SERVER+"php/user/Get_Recent_Orders_Of_User.php";

    String SEND_USER_IMAGE = SERVER+"php/user/Send_User_Image.php";

    String CHECK_USER_DETAILS = SERVER+"php/user/Check_Users_Phone_and_Password.php";


    String GET_ALL_TRAIN_DETAILS = SERVER+"php/user/Get_All_Train_Details.php";

    String GET_CART_DETAILS = SERVER+"php/user/Get_Cart_Details.php";


    // Sign Up
    String CHECK_USER_PHONE_NO = SERVER+"php/user/Check_User_Phone_No_for_Forgot_Password.php";
    String INSERT_USER_DETAILS = SERVER+"php/user/Insert_User_Details.php";


    // Forgot password
    String CHECK_USER_PHONE_NO_FOR_FORGOT_PASSWORD = SERVER+"php/user/Check_User_Phone_No_for_Forgot_Password.php";
    String SET_USER_NEW_PASSWORD = SERVER+"php/user/Set_User_new_Password.php";

    // Bottom Sheet Delivery boy
    String BOTTOM_SHEET_DELIVERY_BOY_AND_ADDRESS = SERVER+"php/user/Delivery_Boy_Details_For_Bottom_Sheet.php";
    // Get food tracking details
    String GET_FOOD_TRACKING_DETAILS = SERVER+"php/user/Get_Food_Tracking_Details.php";


    /// Set delivery boy id is delivery boy rejects the order
    String SET_DELIVERY_BOY_ID_AND_HOLD_10_MINUTS = SERVER+"php/user/Set_Delivery_Boy_id_and_Hold_10_Minutes.php";

    /// Reject order from hotel after 20 minutes is not accepted by hotel
    String REJECT_ORDER_FROM_HOTEL_IF_TIME_EXCEEDED = SERVER+"php/user/Reject_User_Order_from_Hotel_after_20Minutes.php";


    // Payment successful
    String PAYMENT_SUCCESSFUL = SERVER+"php/user/Successful_Payment.php";

    // alarm manager
    String UPDATE_ALARM_MANAGER = SERVER+"php/user/Update_Train_Arrival_TIme.php";


    // past order remove
    String PAST_ORDER_REMOVE = SERVER+"php/user/Remove_Past_Order.php";

    // Rate Delivery Boy
    String RATE_DELIVERY_BOY = SERVER+"php/user/Rate_Delivery_Boy.php";


}
