package com.example.lostandfound3.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lostandfound3.R
import com.example.lostandfound3.activities.models.lost
import com.example.lostandfound3.activities.models.recoveredData
import com.example.lostandfound3.databinding.ActivityLostBinding
import com.example.lostandfound3.databinding.ActivityRecoveredBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class recovered : AppCompatActivity() {
    private lateinit var binding: ActivityRecoveredBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRecoveredBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ADD3.setOnClickListener {
            val etItemName3=binding.etItemName3.text.toString()
            val claimedby=binding.claimedby.text.toString()
            val recovered3=binding.recovered3.text.toString()
            database= FirebaseDatabase.getInstance().getReference("recovered")
            val user= recoveredData(etItemName3,claimedby,recovered3)
            database.child(etItemName3).setValue(user).addOnSuccessListener{
                binding.etItemName3.text?.clear()
                binding.claimedby.text?.clear()
                binding.recovered3.text?.clear()
                Toast.makeText(this,"added successfully", Toast.LENGTH_SHORT).show()

            }
        }
        setupActionBar()
    }
    private fun setupActionBar(){
        val tool_my_profile_activity:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar_board_activity3)
        setSupportActionBar(tool_my_profile_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true)
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.title="ADD_RECOVERED_ITEM_DETAILS"}
        tool_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()

        }
    }
}