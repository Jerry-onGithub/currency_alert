package com.app.currencyalert.interfaces;


import com.app.currencyalert.Btc;

public interface CustomCallback {

    void onSuccess(Btc btc);
    void onFailure(String val);

}
