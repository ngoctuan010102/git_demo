package com.ngoctuan.randomcolor.payment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amazon.device.drm.LicensingService
import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.*
import com.ngoctuan.randomcolor.databinding.ActivityPurchaseBinding
import com.ngoctuan.randomcolor.db.Account
import com.ngoctuan.randomcolor.db.AccountViewModel

class SubBuyAct : AppCompatActivity() {

    private lateinit var binding: ActivityPurchaseBinding
    private lateinit var currentUserId: String
    private lateinit var currentMarketplace: String
    private lateinit var quizPref: QuizPref
    val sub5 = "com.ngoctuan.randomcolor.buy5times"
    val sub10 = "com.ngoctuan.randomcolor.buy15times"
    val sub50 = "com.ngoctuan.randomcolor.buy50times"
  /*  val skuPremium = "com.ngoctuan.randomcolor.buy1000times"
    <Button
    android:id="@+id/bt_week"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Get 10000 times"
    android:textColor="#000000"
    android:textSize="14sp"
    app:backgroundTint="#FF9800" />*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        QuizPref.init(this)
        quizPref = QuizPref.instance!!
        setupIAPOnCreate()
    }

    private val accountViewModel: AccountViewModel by lazy {
        ViewModelProvider(
            this,
            AccountViewModel.AccountViewModelFactory(this.application)
        )[AccountViewModel::class.java]
    }

    private fun setupIAPOnCreate() {
        val purchasingListener: PurchasingListener = object : PurchasingListener {
            override fun onUserDataResponse(response: UserDataResponse) {
                when (response.requestStatus!!) {
                    UserDataResponse.RequestStatus.SUCCESSFUL -> {
                        currentUserId = response.userData.userId
                        currentMarketplace = response.userData.marketplace
                        quizPref.currentUserId(currentUserId)
                        Log.v("IAP SDK", "loaded userdataResponse")
                    }

                    UserDataResponse.RequestStatus.FAILED, UserDataResponse.RequestStatus.NOT_SUPPORTED ->                         // Fail gracefully.
                        Log.v("IAP SDK", "loading failed")
                }
            }

            override fun onProductDataResponse(productDataResponse: ProductDataResponse) {
                when (productDataResponse.requestStatus) {
                    ProductDataResponse.RequestStatus.SUCCESSFUL -> {

                        val products = productDataResponse.productData
                        for (key in products.keys) {
                            val product = products[key]
                            Log.v(
                                "Product:", String.format(
                                    "Product: %s\n Type: %s\n SKU: %s\n Price: %s\n Description: %s\n",
                                    product!!.title,
                                    product.productType,
                                    product.sku,
                                    product.price,
                                    product.description
                                )
                            )
                        }
                        //get all unavailable SKUs
                        for (s in productDataResponse.unavailableSkus) {
                            Log.v("Unavailable SKU:$s", "Unavailable SKU:$s")
                        }
                    }

                    ProductDataResponse.RequestStatus.FAILED -> Log.v("FAILED", "FAILED")
                    else -> {}
                }
            }

            override fun onPurchaseResponse(purchaseResponse: PurchaseResponse) {
                when (purchaseResponse.requestStatus) {
                    PurchaseResponse.RequestStatus.SUCCESSFUL -> {
                        PurchasingService.notifyFulfillment(
                            purchaseResponse.receipt.receiptId,
                            FulfillmentResult.FULFILLED
                        )
                      /*  if (purchaseResponse.receipt.sku.equals(skuPremium)) {
                            accountViewModel.updateAccount(Account(1, 20000))
                            finish()
                        }*/
                        if (purchaseResponse.receipt.sku.equals(sub5)) {
                            accountViewModel.updateAccount(Account(1, 5))
                            finish()
                        }
                        if (purchaseResponse.receipt.sku.equals(sub10)) {
                            accountViewModel.updateAccount(Account(1, 15))
                            finish()
                        }
                        if (purchaseResponse.receipt.sku.equals(sub50)) {
                            accountViewModel.updateAccount(Account(1, 50))
                            finish()
                        }
                        Log.v("FAILED", "FAILED")
                    }

                    PurchaseResponse.RequestStatus.FAILED -> {}
                    else -> {}
                }
            }

            override fun onPurchaseUpdatesResponse(response: PurchaseUpdatesResponse) {
                // Process receipts
                when (response.requestStatus) {
                    PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL -> {
                        for (receipt in response.receipts) {
                            if (!receipt.isCanceled) {
                                quizPref.isPremium = true
                            } else {
                                quizPref.isPremium = false
                            }
                        }
                        if (response.hasMore()) {
                            PurchasingService.getPurchaseUpdates(false)
                        }
                    }

                    PurchaseUpdatesResponse.RequestStatus.FAILED -> Log.d("FAILED", "FAILED")
                    else -> {}
                }
            }
        }
        PurchasingService.registerListener(this.applicationContext, purchasingListener)
        Log.d(
            "DetailBuyAct",
            "Appstore SDK Mode: " + LicensingService.getAppstoreSDKMode()
        )
    }

    override fun onStart() {
        super.onStart()
        val productSkus: MutableSet<String> = HashSet()
      //  productSkus.add(skuPremium)
        productSkus.add(sub5)
        productSkus.add(sub10)
        productSkus.add(sub50)
        PurchasingService.getProductData(productSkus)
    }

    override fun onResume() {
        super.onResume()
        PurchasingService.getUserData()
        //binding.btWeek.setOnClickListener { PurchasingService.purchase(skuPremium) }
        binding.btSubA.setOnClickListener { PurchasingService.purchase(sub5) }
        binding.btSubB.setOnClickListener { PurchasingService.purchase(sub10) }
        binding.btSubC.setOnClickListener { PurchasingService.purchase(sub50) }
        binding.btExit.setOnClickListener { finish() }
        PurchasingService.getPurchaseUpdates(false)
    }
}