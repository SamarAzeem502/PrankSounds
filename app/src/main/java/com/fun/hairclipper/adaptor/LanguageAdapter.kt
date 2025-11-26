package com.`fun`.hairclipper.adaptor

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.`fun`.hairclipper.R
import com.`fun`.hairclipper.databinding.LanguageItemBinding
import com.`fun`.hairclipper.helpers.AppPrefs
import com.`fun`.hairclipper.helpers.LanguageModel

class LanguageAdapter(
    private val activity: Activity,
    private val recyclerItems: List<Any>,
    var selectedPos: Int
) : RecyclerView.Adapter<LanguageAdapter.LanguageHolder>() {
    class LanguageHolder(val binding: LanguageItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LanguageHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LanguageHolder(
            LanguageItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = recyclerItems.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LanguageHolder, position: Int): Unit =
        with(holder.binding) {
            val isSelected = selectedPos == position
            val backgroundDrawableRes =
                if (isSelected) R.drawable.bg_red else R.drawable.bg_card
            val checkIconRes = if (isSelected) R.drawable.radio_check else R.drawable.un_check
            val backgroundDrawable = ContextCompat.getDrawable(root.context, backgroundDrawableRes)
            root.background = backgroundDrawable
            ivSelected.setImageResource(checkIconRes)
            if (AppPrefs.getAppTheme(activity,"app_theme") == AppCompatDelegate.MODE_NIGHT_YES
            ) {
                if (selectedPos == position) {
                    tvLangNAme.setTextColor(Color.WHITE)
                } else {
                    tvLangNAme.setTextColor(Color.WHITE)
                }
            } else {
                if (selectedPos == position) {
                    tvLangNAme.setTextColor(Color.WHITE)
                } else {
                    tvLangNAme.setTextColor(Color.BLACK)
                }
            }
            when (val recyclerItem = recyclerItems[position]) {
                is LanguageModel -> {
                    tvLangNAme.setText(recyclerItem.language)
                    root.setOnClickListener {
                        selectedPos = holder.adapterPosition
                        notifyDataSetChanged()
                    }
                }
            }
        }
}