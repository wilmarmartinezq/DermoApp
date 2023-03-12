package com.example.dermoapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dermoapp.ConsultationDetailsActivity
import com.example.dermoapp.R
import com.example.dermoapp.data.Consultation
import com.squareup.picasso.Picasso

class ConsultationRVAdapter(private var consultationList: ArrayList<Consultation>, ) : RecyclerView.Adapter<ConsultationRVAdapter.ConsultationViewHolder>() {

    var itemClick: ((Consultation) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ConsultationViewHolder { val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_consultation, parent, false)

        return ConsultationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConsultationViewHolder, position: Int) {
        val currentItem = consultationList[position]
        holder.idtv.text = currentItem.id
        holder.shapetv.text = currentItem.shape
        holder.numberofinjuriestv.text = currentItem.numberOfInjuries
        holder.distributiontv.text = currentItem.distribution
        holder.commenttv.text = currentItem.comment
        Picasso.get().load(currentItem.image).into(holder.imagetv)
        holder.creationdatetv.text = currentItem.creationDate
        holder.typeofinjurytv.text = currentItem.typeOfInjury

        holder.itemView.setOnClickListener{
            itemClick?.invoke(currentItem)
        }


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