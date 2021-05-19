package com.loong.ihms.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewBaseAdapter : RecyclerView.Adapter<RecyclerViewBaseAdapter.MyViewHolder>() {
    abstract fun setBindViewHolder(viewHolder: MyViewHolder, position: Int)
    abstract fun getLayoutIdForPosition(position: Int): Int
    abstract fun getItemTotalCount(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        setBindViewHolder(viewHolder, position)
        viewHolder.startExecutePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    override fun getItemCount(): Int {
        return getItemTotalCount()
    }

    inner class MyViewHolder internal constructor(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun startExecutePendingBindings() {
            binding.executePendingBindings()
        }
    }
}