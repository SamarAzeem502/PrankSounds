package com.`fun`.hairclipper.tools

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.`fun`.hairclipper.R
import com.google.android.gms.ads.MediaContent
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class AdsManager(private val mContext: Context) {
    private val TAG = "AdsManager"
    private var adLoadListener: NativeAdLoadListener? = null
    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
        adView.mediaView = adView.findViewById<View>(R.id.ad_media) as MediaView
        // Set other ad assets.
        adView.headlineView = adView.findViewById<View>(R.id.ad_headline)
        adView.bodyView = adView.findViewById<View>(R.id.ad_body)
        adView.callToActionView = adView.findViewById<View>(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById<View>(R.id.ad_app_icon)
        adView.priceView = adView.findViewById<View>(R.id.ad_price)
        adView.starRatingView = adView.findViewById<View>(R.id.ad_stars)
        adView.storeView = adView.findViewById<View>(R.id.ad_store)
        adView.advertiserView = adView.findViewById<View>(R.id.ad_advertiser)

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView?.mediaContent = nativeAd.mediaContent

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView?.visibility = View.INVISIBLE
        } else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView?.visibility = View.INVISIBLE
        } else {
            adView.priceView?.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) {
            adView.storeView?.visibility = View.INVISIBLE
        } else {
            adView.storeView?.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView?.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView?.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView?.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        /* VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            videoStatus.setText(String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    vc.getAspectRatio()));

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    refresh.setEnabled(true);
                    videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
            videoStatus.setText("Video status: Ad does not contain a video asset.");
            refresh.setEnabled(true);
        }*/
    }

    fun setOnNativeAdLoadListener(adLoadListener: NativeAdLoadListener?) {
        this.adLoadListener = adLoadListener
    }

    companion object {
        fun populateUnifiedNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
            // Set the media view. Media content will be automatically populated in the media view once
            // adView.setNativeAd() is called.
            val mediaView: MediaView = adView.findViewById<MediaView>(R.id.ad_media)
            adView.mediaView = mediaView

            // Set other ad assets.
            adView.headlineView = adView.findViewById<View>(R.id.ad_headline)
            adView.bodyView = adView.findViewById<View>(R.id.ad_body)
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