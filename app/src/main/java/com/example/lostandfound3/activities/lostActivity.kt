package com.example.lostandfound3.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import com.example.lostandfound3.R
import com.example.lostandfound3.activities.models.found
import com.example.lostandfound3.activities.models.lost
import com.example.lostandfound3.data.model.lostModel
import com.example.lostandfound3.databinding.ActivityFoundBinding
import com.example.lostandfound3.databinding.ActivityLostBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class lostActivity : BaseActivity() {
    private lateinit var binding: ActivityLostBinding
    private lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ADD1.setOnClickListener {
            val item_name1=binding.etItemName1.text.toString()
            val lost_where=binding.lostWhere.text.toString()
            val contact1=binding.contact1.text.toString()
            database=FirebaseDatabase.getInstance().getReference("lost")
            val user=lost(item_name1,lost_where, contact1)
            database.child(item_name1).setValue(user).addOnSuccessListener{
                binding.etItemName1.text?.clear()
                binding.lostWhere.text?.clear()
                binding.contact1.text?.clear()
                Toast.makeText(this,"added successfully",Toast.LENGTH_SHORT).show()

            }
        }
        setupActionBar()
    }

    private fun setupActionBar(){
        val tool_my_profile_activity:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar_board_activity)
        setSupportActionBar(tool_my_profile_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true)
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.title="ADD_LOST_ITEM_DETAILS"}
        tool_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()

        }
    }
}
