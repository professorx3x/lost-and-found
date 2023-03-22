package com.example.lostandfound3.activities.models

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lostandfound3.R

class MyAdapterFound(val userslist:ArrayList<Fetchfound>,private val context:android.content.Context) : RecyclerView.Adapter<MyAdapterFound.MyViewHolder>(){

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView1: ImageView =itemView.findViewById(R.id.imageView1)
        val text1: TextView =itemView.findViewById(R.id.text1)
        val text2: TextView =itemView.findViewById(R.id.text2)
        val text3: TextView =itemView.findViewById(R.id.text3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_found,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem =userslist[position]
        holder.text1.text = currentitem.item_name2.toString()
        holder.text2.text = currentitem.foundwhere.toString()

        holder.text3.text = currentitem.claim1.toString()
       Glide.with(context).load(currentitem.item_image2).into(holder.imageView1).toString()
    }

    override fun getItemCount(): Int {

        return userslist.size
    }


}