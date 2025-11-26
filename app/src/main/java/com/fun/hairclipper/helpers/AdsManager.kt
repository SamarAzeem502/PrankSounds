package com.`fun`.hairclipper.helpers

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.`fun`.hairclipper.R
import com.google.android.gms.ads.MediaContent
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class AdsManager(private val mContext: Context) {

    companion object {
        fun populateUnifiedNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
            val mediaView: MediaView = adView.findViewById(R.id.ad_media)
            adView.mediaView = mediaView

            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById<View>(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById<View>(R.id.ad_app_icon)
            adView.priceView = adView.findViewById<View>(R.id.ad_price)
            adView.starRatingView = adView.findViewById<View>(R.id.ad_stars)
            adView.storeView = adView.findViewById<View>(R.id.ad_store)
            adView.advertiserView = adView.findViewById<View>(R.id.ad_advertiser)

            // The headline is guaranteed to be in every UnifiedNativeAd.
            (adView.headlineView as TextView).text = nativeAd.headline

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.INVISIBLE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = nativeAd.body
                (adView.bodyView as TextView).setSelected(true)
            }
            if (nativeAd.callToAction == null && adView.callToActionView != null) {
                adView.callToActionView?.visibility = View.INVISIBLE
            } else if (adView.callToActionView != null) {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as TextView).text = nativeAd.callToAction
            }
            if (nativeAd.icon == null && adView.iconView != null) {
                adView.iconView?.visibility = View.GONE
            } else if (adView.iconView != null) {
                (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon!!.drawable
                )
                adView.iconView?.visibility = View.VISIBLE
            }
            if (nativeAd.price == null && adView.priceView != null) {
                adView.priceView?.visibility = View.GONE
            } else if (adView.priceView != null) {
                adView.priceView?.visibility = View.GONE
                (adView.priceView as TextView).text = nativeAd.price
            }
            if (nativeAd.store == null && adView.storeView != null) {
                adView.storeView?.visibility = View.GONE
            } else if (adView.storeView != null) {
                adView.storeView?.visibility = View.GONE
                (adView.storeView as TextView).text = nativeAd.store
            }
            if (nativeAd.starRating == null && adView.starRatingView != null) {
                adView.starRatingView?.visibility = View.INVISIBLE
            } else if (adView.starRatingView != null) {
                (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                adView.starRatingView?.visibility = View.VISIBLE
            }
            if (nativeAd.advertiser == null && adView.advertiserView != null) {
                adView.advertiserView?.visibility = View.INVISIBLE
            } else if (adView.advertiserView != null) {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView?.visibility = View.VISIBLE
            }
            if (nativeAd.headline == null && adView.headlineView != null) {
                adView.headlineView?.visibility = View.INVISIBLE
            } else if (adView.headlineView != null) {
                (adView.headlineView as TextView).text = nativeAd.headline
                adView.headlineView?.visibility = View.VISIBLE
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad. The SDK will populate the adView's MediaView
            // with the media content from this native ad.
            adView.setNativeAd(nativeAd)
        }

        fun showNativeAds(target: NativeAdView, source: NativeAd) {
            target.setNativeAd(source)
            val body = source.body
            val advertiser = source.advertiser
            val icon = source.icon
            val headline = source.headline
            val price = source.price
            val store = source.store
            val starRating = source.starRating
            val mediaContent: MediaContent? = source.mediaContent
            val storeView: TextView = target.storeView as TextView
            val priceView: TextView = target.priceView as TextView
            val advertiserView: TextView = target.advertiserView as TextView
            val bodyView: TextView = target.bodyView as TextView
            val headlineView: TextView = target.headlineView as TextView
            val imageView = target.imageView as ImageView
            val ratingBar: RatingBar = target.starRatingView as RatingBar
            if (mediaContent != null) {
                target.mediaView?.mediaContent = mediaContent
                target.mediaView?.visibility = View.VISIBLE
            } else {
                target.mediaView?.visibility = View.GONE
            }
            if (icon != null && imageView != null) {
                imageView.setImageDrawable(icon.drawable)
                imageView.setVisibility(View.VISIBLE)
            } else imageView.setVisibility(View.GONE)
            if (store != null) {
                storeView.text = store
                storeView.visibility = View.VISIBLE
            } else {
                storeView.visibility = View.GONE
            }
            if (price != null) {
                priceView.text = price
                priceView.visibility = View.VISIBLE
            } else {
                priceView.visibility = View.GONE
            }
            if (advertiser != null) {
                advertiserView.text = advertiser
                advertiserView.visibility = View.VISIBLE
            } else {
                advertiserView.visibility = View.GONE
            }
            if (body != null) {
                bodyView.text = body
                bodyView.visibility = View.VISIBLE
            } else {
                bodyView.visibility = View.GONE
            }
            if (headline != null) {
                headlineView.text = headline
                headlineView.visibility = View.VISIBLE
            } else {
                headlineView.visibility = View.GONE
            }
            if (starRating != null) {
                ratingBar.visibility = View.VISIBLE
                ratingBar.rating = (starRating * 1).toFloat()
            } else {
                ratingBar.visibility = View.GONE
            }
        }
    }
}