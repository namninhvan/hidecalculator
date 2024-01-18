package com.techbmt.hidecalculator.feature.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techbmt.hidecalculator.core.OnItemClickListener
import com.techbmt.hidecalculator.databinding.ItemFeatureBinding

class MainFeatureAdapter(
    private val listFeature: ArrayList<FeatureItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MainFeatureAdapter.MainFeatureHolder>(){

    inner class MainFeatureHolder(private val binding: ItemFeatureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeatureItem) {
            binding.apply {
                ivFeature.setImageResource(item.img)
                tvFeature.text = item.name
            }
            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFeatureHolder {
        return MainFeatureHolder(ItemFeatureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listFeature.size
    }

    override fun onBindViewHolder(holder: MainFeatureHolder, position: Int) {
        holder.bind(listFeature[position])
    }

}