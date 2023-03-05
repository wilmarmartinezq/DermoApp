package com.example.dermoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dermoapp.R
import com.example.dermoapp.data.Consultation
import com.squareup.picasso.Picasso

class ConsultationRVAdapter(
    private var consultationList: ArrayList<Consultation>,
) : RecyclerView.Adapter<ConsultationRVAdapter.ConsultationViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConsultationRVAdapter.ConsultationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_consultation,
            parent, false
        )
        return ConsultationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConsultationRVAdapter.ConsultationViewHolder, position: Int) {
        holder.idtv.text = consultationList.get(position).id
        holder.shapetv.text = consultationList.get(position).shape
        holder.numberofinjuriestv.text = consultationList.get(position).numberOfInjuries
        holder.distributiontv.text = consultationList.get(position).distribution
        holder.commenttv.text = consultationList.get(position).comment
        Picasso.get().load(consultationList.get(position).image).into(holder.imagetv)
        holder.creationdatetv.text = consultationList.get(position).creationDate
        holder.typeofinjurytv.text = consultationList.get(position).typeOfInjury
    }

    override fun getItemCount(): Int {
        return consultationList.size
    }

    class ConsultationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idtv: TextView = itemView.findViewById(R.id.id)
        val shapetv: TextView = itemView.findViewById(R.id.shape)
        val numberofinjuriestv: TextView = itemView.findViewById(R.id.numberOfInjuries)
        val distributiontv: TextView = itemView.findViewById(R.id.distribution)
        val commenttv: TextView = itemView.findViewById(R.id.comment)
        val imagetv: ImageView = itemView.findViewById(R.id.imageview)
        val creationdatetv: TextView = itemView.findViewById(R.id.creationDate)
        val typeofinjurytv: TextView = itemView.findViewById(R.id.typeOfInjury)
    }
}