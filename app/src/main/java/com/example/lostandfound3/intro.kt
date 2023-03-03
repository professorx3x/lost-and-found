package com.example.lostandfound3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        val btn_signup:Button=findViewById(R.id.button_2)
        btn_signup.setOnClickListener {
            startActivity(Intent(this,signup::class.java))
        }

}}