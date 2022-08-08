package com.app.currencyalert;

public class Btc {

    private String time;
    private String price;

    public Btc() {
    }

    public Btc(String time, String price) {
        this.time = time;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
