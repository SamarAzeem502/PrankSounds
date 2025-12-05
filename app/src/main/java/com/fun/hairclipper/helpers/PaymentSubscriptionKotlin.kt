package com.`fun`.hairclipper.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.UI.SplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentSubscriptionKotlin(var context: Context) : BillingClientStateListener {
    private var itemDetailsListener: ItemDetailsListener? = null
    private val billingClient: BillingClient
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            "sp_pro_app",
            Context.MODE_PRIVATE
        )
    }

    private val handler = Handler(Looper.getMainLooper())
    var productDetails: ProductDetails? = null
        private set

    fun setItemDetailsListener(itemDetailsListener: ItemDetailsListener) {
        this.itemDetailsListener = itemDetailsListener
    }

    init {
        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult: BillingResult?, purchases: MutableList<Purchase>? ->
                if (billingResult!!.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                    Toast.makeText(context, "Canceled!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                }
            }
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .build()
        billingClient.startConnection(this)
    }

    fun purchaseNow(activity: Activity) {
        if (productDetails != null) {
            val billingFlowParams =
                BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        mutableListOf<ProductDetailsParams?>(
                            ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails!!)
                                .build()
                        )
                    )
                    .build()
            billingClient.launchBillingFlow(activity, billingFlowParams)
        } else {
            Toast.makeText(
                activity,
                activity.getString(R.string.purchase_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val isPurchased: Boolean
        get() = sharedPreferences.getBoolean("com.hairclipper.premium", false)

    override fun onBillingServiceDisconnected() {
    }

    override fun onBillingSetupFinished(result: BillingResult) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            CoroutineScope(Dispatchers.IO).launch {
                val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
                    .setProductList(
                        listOf(
                            QueryProductDetailsParams.Product.newBuilder()
                                .setProductId("com.hairclipper.premium")
                                .setProductType(BillingClient.ProductType.INAPP)
                                .build()
                        )
                    )
                    .build()

                val productDetailsResult =
                    billingClient.queryProductDetails(queryProductDetailsParams)
                if (productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    productDetails = productDetailsResult.productDetailsList?.firstOrNull()
                    productDetails?.let { itemDetailsListener?.onItemDetails(it) }
                }


                billingClient.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                ) { billingResult, purchasesList ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        val purchased = purchasesList.isNotEmpty()
                        sharedPreferences.edit { putBoolean("com.hairclipper.premium", purchased) }
                    } else {
                        Log.e("Billing", "Error checking purchases: ${billingResult.debugMessage}")
                    }
                }
            }
        }
    }

    fun handlePurchase(purchase: Purchase) {
        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        val listener =
            ConsumeResponseListener { billingResult: BillingResult?, purchaseToken: String? ->
                if (billingResult!!.responseCode == BillingClient.BillingResponseCode.OK) {
                    sharedPreferences.edit { putBoolean("com.hairclipper.premium", true) }
                    handler.post {
                        Toast.makeText(
                            context,
                            context.getString(R.string.purchase_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        //                    Toast.makeText(context, context.getString(R.string.restart_your_app), Toast.LENGTH_SHORT).show();
                        context.startActivity(
                            Intent(context, SplashScreen::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        )
                    }
                }
            }
        billingClient.consumeAsync(consumeParams, listener)
    }

    interface ItemDetailsListener {
        fun onItemDetails(skuDetails: ProductDetails)
    }
}