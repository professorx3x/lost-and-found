package com.example.lostandfound3.activities.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lostandfound3.R

class MyAdapterRecovered(val userslist:ArrayList<FetchRecovered>,private val context:android.content.Context) : RecyclerView.Adapter<MyAdapterRecovered.MyViewHolder>(){

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView3: ImageView =itemView.findViewById(R.id.imageView3)
        val text1: TextView =itemView.findViewById(R.id.text1)
        val text2: TextView =itemView.findViewById(R.id.text2)
        val text3: TextView =itemView.findViewById(R.id.text3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_recovered_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem =userslist[position]
        holder.text1.text = currentitem.etItemName.toString()
        holder.text2.text = currentitem.claimedby.toString()

        holder.text3.text = currentitem.recovered3.toString()
        Glide.with(context).load(currentitem.item_image3).into(holder.imageView3).toString()
    }

    override fun getItemCount(): Int {

        return userslist.size
    }


}