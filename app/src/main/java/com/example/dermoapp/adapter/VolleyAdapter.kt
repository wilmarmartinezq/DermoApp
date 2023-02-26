package com.example.dermoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dermoapp.R
import com.example.dermoapp.data.Consultation
import com.example.dermoapp.databinding.ItemConsultationBinding

class VolleyAdapter(private var data: List<Consultation>) : RecyclerView.Adapter<VolleyAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemConsultationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(consultation: Consultation) {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolleyAdapter.ViewHolder {
        return ViewHolder(
            ItemConsultationBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_consultation, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: VolleyAdapter.ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}