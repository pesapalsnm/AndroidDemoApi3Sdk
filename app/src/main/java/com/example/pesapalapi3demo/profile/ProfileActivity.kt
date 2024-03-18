package com.example.pesapalapi3demo.profile


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.pesapalapi3demo.MainActivity
import com.example.pesapalapi3demo.R
import com.example.pesapalapi3demo.databinding.ActivityProfileBinding
import com.example.pesapalapi3demo.utils.PrefManager
import com.example.pesapalapi3demo.utils.PrefManager.PREF_EMAIL
import com.example.pesapalapi3demo.utils.PrefManager.PREF_FIRST_NAME
import com.example.pesapalapi3demo.utils.PrefManager.PREF_LAST_NAME
import com.example.pesapalapi3demo.utils.PrefManager.PREF_PHONE
import com.google.android.material.textfield.TextInputEditText
import com.example.pesapalapi3demo.utils.PrefUtil
import com.example.pesapalapi3demo.utils.PrefUtil.countriesList
import com.example.pesapalapi3demo.utils.PrefUtil.keAllowedCurrencies
import com.example.pesapalapi3demo.utils.PrefUtil.tzAllowedCurrencies
import com.example.pesapalapi3demo.utils.PrefUtil.ugAllowedCurrencies


class ProfileActivity: AppCompatActivity(), OnItemSelectedListener {

    private lateinit var binding: ActivityProfileBinding
    var testCurrency = listOf<String>()
    lateinit var currencyAdapter: ArrayAdapter<String>
    lateinit var adCountry: ArrayAdapter<String>
    lateinit var firstNameEt: TextInputEditText
    lateinit var lastNameEt: TextInputEditText
    lateinit var emailEt: TextInputEditText
    lateinit var phoneEt: TextInputEditText

    lateinit var spinnerCurrency: AppCompatSpinner
    lateinit var spinnerCountry: AppCompatSpinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater);
        setContentView(binding.root)
        R.layout.activity_profile
        initData()
    }


    private fun setCurrencyAdp(country: String){
        testCurrency = (changeCountryCurrency(country))
        currencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, testCurrency)
        spinnerCurrency.adapter = currencyAdapter
    }


    private fun initData(){
        firstNameEt = binding.tvUserName
        lastNameEt = binding.tvLastName
        emailEt = binding.tvEmail
        phoneEt = binding.tvPhone
        spinnerCountry = binding.spinnerCountry
        spinnerCurrency =   binding.spinnerCurrency

        firstNameEt.setText(PrefManager.getString(PREF_FIRST_NAME, ""))
        lastNameEt.setText(PrefManager.getString(PREF_LAST_NAME, ""))
        emailEt.setText(PrefManager.getString(PREF_EMAIL, ""))
        phoneEt.setText(PrefManager.getString(PREF_PHONE, ""))

        val default = PrefManager.getCountry()
        setCurrencyAdp(default)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adCountry = ArrayAdapter(this, android.R.layout.simple_spinner_item, countriesList)
        adCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCountry.adapter = adCountry

        val currencySelected: Int = currencyAdapter.getPosition(PrefManager.getCurrency())
        val countrySelected: Int = adCountry.getPosition(default)

        spinnerCurrency.setSelection(currencySelected)
        spinnerCountry.setSelection(countrySelected)

        binding.toggle.isChecked = PrefManager.getIsProduction()

        spinnerCountry.onItemSelectedListener = countryItemSelectedListener

        spinnerCurrency.onItemSelectedListener = this

        binding.toggle.setOnCheckedChangeListener { _, isChecked ->
            PrefManager.setIsProduction(isChecked)
        }
        handleClick()
    }

    private fun handleClick(){
        binding.btnSubmit.setOnClickListener {
            PrefManager.putString(PREF_FIRST_NAME, firstNameEt.text.toString())
            PrefManager.putString(PREF_LAST_NAME, lastNameEt.text.toString())
            PrefManager.putString(PREF_EMAIL, emailEt.text.toString())
            PrefManager.putString(PREF_PHONE, phoneEt.text.toString())
            PrefUtil.setData(spinnerCountry.selectedItemPosition)
            restart()
        }
    }

    private fun restart() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }


    private fun showMessage(message: String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    private val countryItemSelectedListener = object : OnItemSelectedListener{
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            setCurrencyAdp((countriesList[p2]))
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

    }

    private fun changeCountryCurrency(country: String): List<String> {
        return when(country){
            "Kenya" -> {
                keAllowedCurrencies
            }
            "Uganda" -> {
                ugAllowedCurrencies
            }
            "Tanzania" -> {
                tzAllowedCurrencies
            }
            else -> keAllowedCurrencies

        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val currency = testCurrency[p2]
        PrefManager.setCurrency(currency)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


}