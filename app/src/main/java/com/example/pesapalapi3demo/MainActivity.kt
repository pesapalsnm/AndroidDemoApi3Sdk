package com.example.pesapalapi3demo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pesapalapi3demo.databinding.ActivityMainBinding
import com.example.pesapalapi3demo.profile.ProfileActivity
import com.example.pesapalapi3demo.utils.PrefManager
import com.example.pesapalapi3demo.utils.PrefManager.IS_PRODUCTION
import com.pesapal.sdk.activity.PesapalSdkActivity
import com.pesapal.sdk.model.txn_status.TransactionStatusResponse
import com.pesapal.sdk.utils.PESAPALAPI3SDK
import com.pesapal.sdkdemo.adapter.DemoCartAdapter
import com.pesapal.sdkdemo.model.CatalogueModel
import com.pesapal.sdkdemo.model.UserModel
import com.example.pesapalapi3demo.utils.PrefUtil
import com.pesapal.sdk.model.card.CustomerData
import com.squareup.picasso.Picasso
import java.math.BigDecimal
import java.util.*


class MainActivity : AppCompatActivity(),DemoCartAdapter.clickedListener {

    private lateinit var binding:ActivityMainBinding
    private var currency = ""
    private var total = BigDecimal.ZERO
    private lateinit var demoCartAdapter: DemoCartAdapter
    private lateinit var catalogueModelList: MutableList<CatalogueModel.ProductsBean>
    private lateinit var itemModelList: MutableList<CatalogueModel.ProductsBean>
    private var orderId = ""
    private lateinit var userModel: UserModel
    private var PAYMENT_REQUEST = 112233


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

    }

    private fun initSdk(){
        if(PrefManager.getCountry() == ""){
            PrefUtil.setData(0)
        }
    }

    private fun initData(){
        initRecyclerData()
        handleClicks()
    }


    private fun initRecyclerData(){
        catalogueModelList = arrayListOf()
        itemModelList = arrayListOf()
        orderId = createTransactionID()
        catalogueModelList.addAll(
            listOf(
                CatalogueModel.ProductsBean("Chips",R.drawable.chips, BigDecimal(1).setScale(2)),
                CatalogueModel.ProductsBean("Burgers",R.drawable.burgers, BigDecimal(5).setScale(2)),
                CatalogueModel.ProductsBean("Milkshakes",R.drawable.burgers, BigDecimal(500).setScale(2)),
                CatalogueModel.ProductsBean("Family meal",R.drawable.chips, BigDecimal(5000).setScale(2)),
            ))
        demoCartAdapter = DemoCartAdapter(this)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = demoCartAdapter
        demoCartAdapter.setData(catalogueModelList)
    }

    private fun handleClicks(){
        binding.btnCheckOut.setOnClickListener {
            startPayment()
        }

        binding.civProfile.setOnClickListener {
            startProfile()
        }
    }

    private fun startProfile(){
        startActivity(Intent(this, ProfileActivity::class.java))
    }


    private fun createTransactionID(): String {
        return UUID.randomUUID().toString().uppercase().substring(0,8)
    }

    private fun updateBasketList(){
        total = BigDecimal.ZERO
        for(catelog in itemModelList){
            total += catelog.price
        }
        binding.totalPrice.text = currency+" ${total.setScale(2)}"
        binding.tvOrderId.text = orderId
    }

    private fun showMessage(message: String){
        Toast.makeText(this@MainActivity,message, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        initData()
        initSdk()
        currency = PrefManager.getCurrency()

        updateBasketList()
    }

    private fun startPayment(){
        val firstName   = PrefManager.getString(PrefManager.PREF_FIRST_NAME, "")
        val lastName    = PrefManager.getString(PrefManager.PREF_LAST_NAME, "")
        val email       = PrefManager.getString(PrefManager.PREF_EMAIL, "")
        val phone       = PrefManager.getString(PrefManager.PREF_PHONE, "")

        val photoUrl: String? = null
        val time: String? = null
        userModel = UserModel("",firstName,lastName,email,photoUrl,time,phone)

        initPayment()
    }

    private fun initPayment(){
        val consumerKey    = ""                // Mandatory data personal to each merchant
        val consumerSecret = ""                // Mandatory data personal to each merchant
        val account        = ""                // Mandatory data personal to each merchant
        val callBack       = ""                // Mandatory data personal to each merchant
        val ipn            = ""                // Mandatory data personal to each merchant


        PESAPALAPI3SDK.init(this,
            consumerKey,
            consumerSecret,
            account,
            callBack,
            ipn,
            PrefManager.getBoolean(IS_PRODUCTION, false)
        )

        val line = "John"
        val countryCode = "254"
        val line2 = "John"
        val emailAddress = userModel.email
        val city= "Nairobi"
        val lastName= userModel.lastName
        val phoneNumber= "700000000"
        val state= ""
        val middleName= ""
        val postalCode= "00000"
        val firstName= "John"
        val zipCode= "00000"

        val customerData = CustomerData(
            line,
            countryCode,
            line2, emailAddress,
            city, lastName, phoneNumber,
            state, middleName,postalCode, firstName, zipCode)

        val myIntent = Intent(this, PesapalSdkActivity::class.java)
        myIntent.putExtra(PESAPALAPI3SDK.AMOUNT     , total.toDouble())
        myIntent.putExtra(PESAPALAPI3SDK.ORDER_ID   ,orderId)
        myIntent.putExtra(PESAPALAPI3SDK.CURRENCY   ,currency)
        myIntent.putExtra(PESAPALAPI3SDK.COUNTRY    ,translateCountryToEnum())
        myIntent.putExtra(PESAPALAPI3SDK.USER_DATA  ,customerData)
        startActivityForResult(myIntent             ,PAYMENT_REQUEST)
    }

    private fun setImage(photoUrl: String){
        Picasso.get().load(photoUrl).into(binding.civProfile);
    }



    /**
     * Converts the country chosen to an to the relevant ENUM
     */
    private fun translateCountryToEnum(): PESAPALAPI3SDK.COUNTRIES_ENUM{
        return when(PrefManager.getCountry()){
            "Uganda" -> {
                PESAPALAPI3SDK.COUNTRIES_ENUM.COUNTRY_UG
            }
            "Tanzania" ->{
                PESAPALAPI3SDK.COUNTRIES_ENUM.COUNTRY_TZ
            }
            else -> {
                PESAPALAPI3SDK.COUNTRIES_ENUM.COUNTRY_KE
            }
        }
    }

    override fun Clicked(isAdd: Boolean, catalogueModel: CatalogueModel.ProductsBean) {
        if(isAdd){
            itemModelList.add(catalogueModel)
            updateBasketList()
        }else{
            itemModelList.remove(catalogueModel)
            updateBasketList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT_REQUEST) {
            orderId = createTransactionID()
            binding.tvOrderId.text = "Order ID $orderId"
            if (resultCode == RESULT_OK) {
                // A transaction that was processed by Pesapal's API3 ecosytme. It may be completed or failed
                val transactionStatusResponse = data?.getSerializableExtra("data") as TransactionStatusResponse?
                transactionStatusResponse?.let {
                    val statusCode = transactionStatusResponse.statusCode
                    when(statusCode){
                        0 -> {
                            // Handle INVALID state
                        }
                        1 -> {
                            handleCompletedTxn(transactionStatusResponse)
                        }
                        2 -> {
                            // Handle FAILED state
                        }
                        3 -> {
                            // HANDLE REVERSED state
                        }
                    }
                }
            }
            else {
                // A failed payment
                val transactionStatusResponse = data?.getSerializableExtra("data") as TransactionStatusResponse?
                transactionStatusResponse?.let {response ->
                    response.error?.errorType.let {errorType ->
                        val errorDef = when(errorType){
                            PESAPALAPI3SDK.ERR_INIT -> {
                                "Error occurred during data initialization. Check on PESAPALAPI3SDK.init() and Intent Extras"
                            }
                            PESAPALAPI3SDK.ERR_SECURITY -> {
                                "Security warning"
                            }
                            PESAPALAPI3SDK.ERR_NETWORK -> {
                                "Network failure"
                            }
                            PESAPALAPI3SDK.ERR_GENERAL ->{
                                "User cancelled the transaction."
                            }
                            else -> {
                                "Other error"
                            }
                        }
                        showMessage(errorDef)

                    }
                }
            }

        }
    }

    private fun handleCompletedTxn(transactionStatusResponse: TransactionStatusResponse){
        itemModelList.clear()
        demoCartAdapter.notifyDataSetChanged()
        if(transactionStatusResponse.description != null) {
            showMessage(transactionStatusResponse.description!!)
        }else{
            showMessage(transactionStatusResponse.message!!)
        }
    }


}