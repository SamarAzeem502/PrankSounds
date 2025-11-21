package com.fun.hairclipper.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.fun.hairclipper.R;

import java.util.Collections;

public class PaymentSubscription implements BillingClientStateListener {
    Context context;
    ItemDetailsListener itemDetailsListener;
    private final BillingClient billingClient;
    private final SharedPreferences sharedPreferences;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ProductDetails productDetails;

    public void setItemDetailsListener(ItemDetailsListener itemDetailsListener) {
        this.itemDetailsListener = itemDetailsListener;
    }

    public PaymentSubscription(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("sp_pro_app", Context.MODE_PRIVATE);
        PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                Toast.makeText(context, "Canceled!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
            }
        };
        billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(this);
    }

    public void purchaseNow(Activity activity) {
        if (productDetails != null) {
            BillingFlowParams billingFlowParams =
                    BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(
                                    Collections.singletonList(
                                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                                    .setProductDetails(productDetails)
                                                    .build()
                                    )
                            )
                            .build();
            billingClient.launchBillingFlow(activity, billingFlowParams);
        } else {
            Toast.makeText(activity, activity.getString(R.string.purchase_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isPurchased() {
        return sharedPreferences.getBoolean("com.hairclipper.premium", false);
    }

    @Override
    public void onBillingServiceDisconnected() {

    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult result) {
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder().setProductList(
                        Collections.singletonList(QueryProductDetailsParams.Product.newBuilder()
                                .setProductId("com.hairclipper.premium")
                                .setProductType(BillingClient.ProductType.INAPP)
                                .build())).build();
        billingClient.queryProductDetailsAsync(queryProductDetailsParams, (billingResult, productList) -> {
            if (!productList.isEmpty()) {
                productDetails = productList.get(0);
                itemDetailsListener.onItemDetails(getProductDetails());
            }
        });
    }

    void handlePurchase(Purchase purchase) {
        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
        ConsumeResponseListener listener = (billingResult, purchaseToken) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                sharedPreferences.edit().putBoolean("com.hairclipper.premium", true).apply();
                handler.post(() -> {
                    Toast.makeText(context, context.getString(R.string.purchase_success), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, context.getString(R.string.restart_the_app_to_complete_setup), Toast.LENGTH_SHORT).show();
                });
            }
        };
        billingClient.consumeAsync(consumeParams, listener);
    }


    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public interface ItemDetailsListener {
        void onItemDetails(@NonNull ProductDetails skuDetails);
    }
}