package com.example.pesapalapi3demo.utils

import com.pesapal.sdk.utils.PESAPALAPI3SDK

object PrefUtil {

    var keAllowedCurrencies = listOf(PESAPALAPI3SDK.CURRENCY_CODE_KES , "USD")
    var ugAllowedCurrencies = listOf(PESAPALAPI3SDK.CURRENCY_CODE_UGX, "USD")
    var tzAllowedCurrencies = listOf(PESAPALAPI3SDK.CURRENCY_CODE_TZS, "USD")
    var countriesList = listOf("Kenya", "Uganda", "Tanzania")

    fun setData(p2: Int){
        PrefManager.setCountry(countriesList[p2])
    }
}