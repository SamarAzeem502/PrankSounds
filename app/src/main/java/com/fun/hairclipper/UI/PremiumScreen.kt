package com.`fun`.hairclipper.UI

import android.os.Bundle
import com.android.billingclient.api.ProductDetails
import com.`fun`.hairclipper.databinding.ActivityPremiumBinding
import com.`fun`.hairclipper.tools.PaymentSubscription

class PremiumScreen : BaseClass(), PaymentSubscription.ItemDetailsListener {
    private val binding by lazy { ActivityPremiumBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        paymentSubscription.setItemDetailsListener(this)
        paymentSubscription.productDetails?.let {
            onItemDetails(paymentSubscription.productDetails)
        }
        binding.btnContinuePurchase.setOnClickListener {
            paymentSubscription.purchaseNow(this@PremiumScreen)
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
            paymentSubscription.productDetails.oneTimePurchaseOfferDetails
        oneTimePurchaseOfferDetails?.formattedPrice?.also {
            binding.tvPrice.text = it
        }
    }

    override fun handleBackPressed() {

    }
}