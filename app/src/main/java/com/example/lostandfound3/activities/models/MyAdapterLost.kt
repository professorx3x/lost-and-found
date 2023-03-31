package com.example.lostandfound3.activities.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lostandfound3.R

class MyAdapterLost(val userslist:ArrayList<FetchLost>,private val context:android.content.Context) : RecyclerView.Adapter<MyAdapterLost.MyViewHolder>(){

    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val imageView1:ImageView=itemView.findViewById(R.id.imageView1)
val text1:TextView=itemView.findViewById(R.id.text1)
        val text2:TextView=itemView.findViewById(R.id.text2)
        val text3:TextView=itemView.findViewById(R.id.text3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView=
           LayoutInflater.from(parent.context).inflate(R.layout.itemslist_lost,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val currentitem =userslist[position]
        holder.text1.text = currentitem.item_name1.toString()
        holder.text2.text = currentitem.lost_where
        holder.text3.text = currentitem.contact1.toString()
        Glide.with(context).load(currentitem.item_image1).into(holder.imageView1).toString()
    }

    override fun getItemCount(): Int {
        return userslist.size
    }


}