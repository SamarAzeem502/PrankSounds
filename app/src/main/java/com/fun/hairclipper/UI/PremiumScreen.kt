package com.`fun`.hairclipper.UI

import android.os.Bundle
import com.android.billingclient.api.ProductDetails
import com.`fun`.hairclipper.admobHelper.MyApplication
import com.`fun`.hairclipper.databinding.ActivityPremiumBinding
import com.`fun`.hairclipper.helpers.PaymentSubscriptionKotlin

class PremiumScreen : BaseClass(), PaymentSubscriptionKotlin.ItemDetailsListener {
    private val binding by lazy { ActivityPremiumBinding.inflate(layoutInflater) }
    private lateinit var subscription: PaymentSubscriptionKotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        subscription = (applicationContext as MyApplication).paymentSubscription
        subscription.setItemDetailsListener(this)
        subscription.productDetails?.let {
            onItemDetails(subscription.productDetails!!)
        }
        binding.btnContinuePurchase.setOnClickListener {
            subscription.purchaseNow(this@PremiumScreen)
        }
        binding.ivCross.setOnClickListener {
            finish()
        }
        binding.tvContinueAds.setOnClickListener {
            finish()
        }
    }

    override fun onItemDetails(skuDetails: ProductDetails) {
        val oneTimePurchaseOfferDetails =
            subscription.productDetails?.oneTimePurchaseOfferDetails
        oneTimePurchaseOfferDetails?.formattedPrice?.also {
            binding.tvPrice.text = it
        }
    }

    override fun handleBackPressed() {

    }
}