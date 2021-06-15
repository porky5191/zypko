package com.zypko.zypko4.clicked_hotel.util;

public class Food_Sqlite {
    
    private int food_id;
    private String food_name;
    private String veg;
    private int food_actual_price;
    private int food_made_price;
    private int food_total_price;
    private int total_items;
    private int food_discount;
    private int food_taxes;
    private int food_packing_charge;
    private String hotel_name;
    private int hotel_id;

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_actual_price() {
        return food_actual_price;
    }

    public void setFood_actual_price(int food_actual_price) {
        this.food_actual_price = food_actual_price;
    }

    public int getFood_total_price() {
        return food_total_price;
    }

    public void setFood_total_price(int food_total_price) {
        this.food_total_price = food_total_price;
    }

    public int gettotal_items() {
        return total_items;
    }

    public void settotal_items(int total_items) {
        this.total_items = total_items;
    }

    public int getFood_discount() {
        return food_discount;
    }

    public void setFood_discount(int food_discount) {
        this.food_discount = food_discount;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getVeg() {
        return veg;
    }

    public void setVeg(String veg) {
        this.veg = veg;
    }

    public int getFood_taxes() {
        return food_taxes;
    }

    public void setFood_taxes(int food_taxes) {
        this.food_taxes = food_taxes;
    }

    public int getFood_packing_charge() {
        return food_packing_charge;
    }

    public void setFood_packing_charge(int food_packing_charge) {
        this.food_packing_charge = food_packing_charge;
    }

    public int getFood_made_price() {
        return food_made_price;
    }

    public void setFood_made_price(int food_made_price) {
        this.food_made_price = food_made_price;
    }
}
