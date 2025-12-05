package com.`fun`.hairclipper.adaptor

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.UI.IntroActivity
import com.`fun`.hairclipper.UI.NewMainMenu
import com.`fun`.hairclipper.admobHelper.AdConstants
import com.`fun`.hairclipper.databinding.FullNativeContainerBinding
import com.`fun`.hairclipper.databinding.IntroItemBinding
import com.`fun`.hairclipper.helpers.AdsManager
import com.google.android.gms.ads.nativead.NativeAdView

class IntroAdapterFullNative(private val activity: IntroActivity) :
    RecyclerView.Adapter<IntroAdapterFullNative.IntroAdapterFullNativeViewHolder>() {
    private var itemsList: ArrayList<Any> = ArrayList()

    init {
        itemsList = if (AdConstants.INTRO_NATIVE_AD != null /*&& prefs.getFirstIntro() == true*/) {
            ArrayList(listOf(1, 2, "ad", 3, 4))
        } else {
            ArrayList(listOf(1, 2, 3, 4))
        }
    }

    class IntroAdapterFullNativeViewHolder(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): IntroAdapterFullNativeViewHolder {
        val binding = if (viewType == 1) {
            IntroItemBinding.inflate(activity.layoutInflater, parent, false)
        } else {
            FullNativeContainerBinding.inflate(activity.layoutInflater, parent, false)
        }
        return IntroAdapterFullNativeViewHolder(binding)
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: IntroAdapterFullNativeViewHolder, position: Int) {
        val item = itemsList[position]

        if (item is Int) {
            val binding = holder.binding as IntroItemBinding

            when (item) {
                1 -> {
                    binding.tvTitle.text = activity.getString(R.string.prank_sounds)
                    binding.tvPageDetail.text = ""
                    Glide.with(binding.root).load(R.drawable.intro_one)
                        .into(binding.ivHowToDownload)
                    binding.tvNext.setOnClickListener {
                        activity.binding.screenViewpager.currentItem += 1
                    }
                    binding.tvSkip.setOnClickListener {
                        activity.startActivity(Intent(activity, NewMainMenu::class.java))
                        activity.finish()
                    }
                    setDots(
                        0,
                        ArrayList(
                            listOf(
                                binding.ivDot1,
                                binding.ivDot2,
                                binding.ivDot3,
                                binding.ivDot4
                            )
                        )
                    )
                    binding.tvNext.text = activity.getString(R.string._next)
                }

                2 -> {
                    binding.tvTitle.text = activity.getString(R.string.fart_sounds)
                    binding.tvPageDetail.text = ""
                    Glide.with(binding.root).load(R.drawable.intro_two)
                        .into(binding.ivHowToDownload)

                    binding.tvNext.setOnClickListener {
                        activity.binding.screenViewpager.currentItem += 1
                    }
                    binding.tvSkip.setOnClickListener {
                        activity.startActivity(Intent(activity, NewMainMenu::class.java))
                        activity.finish()
                    }
                    setDots(
                        1,
                        ArrayList(
                            listOf(
                                binding.ivDot1,
                                binding.ivDot2,
                                binding.ivDot3,
                                binding.ivDot4
                            )
                        )
                    )
                    binding.tvNext.text = activity.getString(R.string._next)
                }

                3 -> {
                    binding.tvTitle.text = activity.getString(R.string.hair_clipper)
                    binding.tvPageDetail.text = ""
                    Glide.with(binding.root).load(R.drawable.intro_three)
                        .into(binding.ivHowToDownload)

                    binding.tvNext.setOnClickListener {
                        activity.binding.screenViewpager.currentItem += 1
                    }
                    binding.tvSkip.setOnClickListener {
                        activity.startActivity(Intent(activity, NewMainMenu::class.java))
                        activity.finish()
                    }
                    setDots(
                        2,
                        ArrayList(
                            listOf(
                                binding.ivDot1,
                                binding.ivDot2,
                                binding.ivDot3,
                                binding.ivDot4
                            )
                        )
                    )
                    binding.tvNext.text = activity.getString(R.string._next)
                }

                4 -> {
                    binding.tvTitle.text = activity.getString(R.string.prank_sounds_1)
                    binding.tvPageDetail.text = ""
                    Glide.with(binding.root).load(R.drawable.intro_four)
                        .into(binding.ivHowToDownload)

                    binding.tvNext.setOnClickListener {
                        activity.startActivity(Intent(activity, NewMainMenu::class.java))
                        activity.finish()
                    }
                    binding.tvSkip.setOnClickListener {
                        activity.startActivity(Intent(activity, NewMainMenu::class.java))
                        activity.finish()
                    }
                    setDots(
                        3,
                        ArrayList(
                            listOf(
                                binding.ivDot1,
                                binding.ivDot2,
                                binding.ivDot3,
                                binding.ivDot4
                            )
                        )
                    )
                    binding.tvNext.text = activity.getString(R.string._finish)
                }
            }
        } else {
            val binding = holder.binding as FullNativeContainerBinding
            val adView = LayoutInflater.from(activity)
                .inflate(R.layout.full_screen_native, null) as NativeAdView
            AdsManager.populateUnifiedNativeAdView(AdConstants.INTRO_NATIVE_AD!!, adView)
            binding.nativeContainer.removeAllViews()
            binding.nativeContainer.addView(adView)
        }
    }

    private fun setDots(position: Int, dotsImageViews: ArrayList<ImageView>) {
        dotsImageViews.forEachIndexed { index, imageView ->
            if (index == position) {
                imageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        activity,
                        R.drawable.fill
                    )
                )
            } else if (index < position) {
                imageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        activity,
                        R.drawable.fill
                    )
                )
            } else {
                imageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        activity,
                        R.drawable.ic_unselected_dot
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int = itemsList.size

    override fun getItemViewType(position: Int): Int {
        return if (itemsList[position] is Int) {
            1
        } else {
            0
        }
    }
}