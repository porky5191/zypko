package com.zypko.zypko4.cart.util;

public class Seter_Geter {


    private String hotel_name;
    private String hotel_address;
    private int  type;


    public static final int ONE_TYPE = 1;
    public static final int TWO_TYPE = 2;

    public Seter_Geter(String hotel_name, String hotel_address, int type){
        this.hotel_name = hotel_name;
        this.hotel_address = hotel_address;
        this.type = type;

    }


    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getHotel_address() {
        return hotel_address;
    }

    public void setHotel_address(String hotel_address) {
        this.hotel_address = hotel_address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
