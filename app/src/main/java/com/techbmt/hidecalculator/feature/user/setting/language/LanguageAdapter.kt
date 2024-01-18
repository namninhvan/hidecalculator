package com.techbmt.hidecalculator.feature.user.setting.language

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.techbmt.hidecalculator.R
import com.techbmt.hidecalculator.core.OnItemClickListener
import com.techbmt.hidecalculator.databinding.ItemLanguageBinding

class LanguageAdapter(
    private val context: Context,
    private var listLanguages: List<Language>,
    private var listener: (Language, Int) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    private var selectedPos = RecyclerView.NO_POSITION

    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bind(language: Language) {
            binding.apply {
                rdStatus.isChecked = adapterPosition == selectedPos
                when(language.type) {
                    TypeOfLanguage.VIETNAM -> {
                        tvLanguage.text = "Việt Nam"
                        flag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vietnam))
                    }
                    TypeOfLanguage.ENGLISH -> {
                        tvLanguage.text = "English"
                        flag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_english))
                    }
                    else -> {
                        tvLanguage.text = "Việt Nam"
                        flag.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vietnam))
                    }
                }
                rdStatus.setOnClickListener {
                    if(selectedPos != adapterPosition) {
                        selectedPos = adapterPosition
                        notifyDataSetChanged()
                    }
                    listener.invoke(language, selectedPos)
                }
                itemView.setOnClickListener {
                    if(selectedPos != adapterPosition) {
                        selectedPos = adapterPosition
                        notifyDataSetChanged()
                    }
                    listener.invoke(language, selectedPos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        return LanguageViewHolder(ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listLanguages.size

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(listLanguages[position])
    }

}