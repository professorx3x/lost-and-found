package com.example.lostandfound3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class signup(val toolbar: androidx.appcompat.widget.Toolbar?) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setupactionbar()
    }
      private  fun setupactionbar(){
            val toolbar1:Toolbar=findViewById(R.id.toolbar)
            setSupportActionBar(toolbar1)
          val actionBar=supportActionBar
          if(actionBar!=null){
              actionBar.setDisplayHomeAsUpEnabled(true)
              actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
          }
        }
    }


