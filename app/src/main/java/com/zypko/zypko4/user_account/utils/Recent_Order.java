package com.zypko.zypko4.user_account.utils;

public class Recent_Order {

    private int order_table_id;
    private String partially_paid_amount;
    private String amount_left;
    private String percent_paid;
    private String hotel_name;
    private String hotel_address;
    private String station_name;
    private String paid_amount;
    private String total_items;
    private String pay_by_cash_online;
    private String user_id;

    public int getOrder_table_id() {
        return order_table_id;
    }

    public void setOrder_table_id(int order_table_id) {
        this.order_table_id = order_table_id;
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

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public String getPay_by_cash_online() {
        return pay_by_cash_online;
    }

    public void setPay_by_cash_online(String pay_by_cash_online) {
        this.pay_by_cash_online = pay_by_cash_online;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getPartially_paid_amount() {
        return partially_paid_amount;
    }

    public void setPartially_paid_amount(String partially_paid_amount) {
        this.partially_paid_amount = partially_paid_amount;
    }

    public String getAmount_left() {
        return amount_left;
    }

    public void setAmount_left(String amount_left) {
        this.amount_left = amount_left;
    }

    public String getPercent_paid() {
        return percent_paid;
    }

    public void setPercent_paid(String percent_paid) {
        this.percent_paid = percent_paid;
    }
}
