package com.example.pesapalapi3demo.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.pesapalapi3demo.App;
import com.pesapal.sdk.utils.PESAPALAPI3SDK;


public class PrefManager {

    public static final String CURRENCY= "CURRENCY";
    public static final String CONSUMER_KEY= "CONSUMER_KEY";
    public static final String CONSUMER_SECRET= "CONSUMER_SECRET";
    public static final String CALL_BACK_URL = "CALL_BACK_URL";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String IS_PRODUCTION = "IS_PRODUCTION";
    public static final String COUNTRY ="COUNTRY";
    public static final String PREF_FIRST_NAME ="PREF_FIRST_NAME";
    public static final String PREF_LAST_NAME ="PREF_LAST_NAME";
    public static final String PREF_EMAIL ="PREF_EMAIL";
    public static final String PREF_PHONE ="PREF_PHONE";


    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(App.getInstance()
                .getApplicationContext());
    }

    public static boolean getBoolean(String preferenceKey, boolean preferenceDefaultValue) {
        return getPreferences().getBoolean(preferenceKey, preferenceDefaultValue);
    }

    public static void putBoolean(String preferenceKey, boolean preferenceValue) {
        getPreferences().edit().putBoolean(preferenceKey, preferenceValue).apply();
    }

    public static String getString(String preferenceKey) {
        return getPreferences().getString(preferenceKey, "");
    }

    public static String getString(String preferenceKey, String prefDefault) {
        return getPreferences().getString(preferenceKey, prefDefault);
    }


    public static void putString(String preferenceKey, String preferenceValue) {
        getPreferences().edit().putString(preferenceKey, preferenceValue).apply();
    }

    public static void setCurrency(String currency){
        putString(CURRENCY,currency);
    }

    public static String getCurrency(){
        return getString(CURRENCY, PESAPALAPI3SDK.CURRENCY_CODE_KES);
    }

    public static Boolean getIsProduction(){
        return getBoolean(IS_PRODUCTION, false);
    }

    public static void setIsProduction(boolean isProd) {
        putBoolean(IS_PRODUCTION, isProd);
    }

    public static String getCountry(){
        return getString(COUNTRY);
    }
    public static void setCountry(String country){
         putString(COUNTRY, country);
    }


}
