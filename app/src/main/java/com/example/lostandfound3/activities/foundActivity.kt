package com.example.lostandfound3.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.lostandfound3.R
import com.example.lostandfound3.R.id
import com.example.lostandfound3.activities.models.User
import com.example.lostandfound3.activities.models.found
import com.example.lostandfound3.databinding.ActivityFoundBinding
import com.example.lostandfound3.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class foundActivity : AppCompatActivity() {
    private var mSelectedImageFileUri2:Uri?=null
    private var mProfileImageURL:String=""
    private lateinit var binding:ActivityFoundBinding

    private lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFoundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ADD2.setOnClickListener {
            if(mSelectedImageFileUri2!=null){
                val iv_user_image:de.hdodenhof.circleimageview.CircleImageView=findViewById(id.iv_board_image)
                iv_user_image.setImageURI(mSelectedImageFileUri2).toString()
                uploadUserImage()
            }
        }
        binding.ivBoardImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    MyProfileActivity.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        setupActionBar()
    }



    private fun setupActionBar(){
        val tool_my_profile_activity:androidx.appcompat.widget.Toolbar=findViewById(id.toolbar_board_activity2)
        setSupportActionBar(tool_my_profile_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true)
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.title="ADD_FOUND_ITEM_DETAILS"}
        tool_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()

        }
    }
    private fun showImageChooser(){
        var galleryIntent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, MyProfileActivity.PICK_IMAGE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK&&requestCode== MyProfileActivity.PICK_IMAGE_REQUEST_CODE &&data!!.data!=null)
        {
            val iv_user_image:de.hdodenhof.circleimageview.CircleImageView=findViewById(id.iv_board_image)
            mSelectedImageFileUri2=data.data
            try {
                Glide
                    .with(this@foundActivity)
                    .load(mSelectedImageFileUri2)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_user_image);
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
    private fun getFileExtension(uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }
    private fun uploadUserImage(){

        if(mSelectedImageFileUri2!=null){

            val sRef= FirebaseStorage.getInstance().reference.child("lost_image"+ System.currentTimeMillis()+"."+getFileExtension(mSelectedImageFileUri2))
            sRef.putFile(mSelectedImageFileUri2!!).addOnSuccessListener{
                    taskSnapshot->
                Log.i(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener{
                        uri->
                    Log.i("Downloadable Image URL", uri.toString())

                    mProfileImageURL=uri.toString()
                    found().item_image2=uri.toString()
                    val item_name2=binding.etItemName2.text.toString()
                    val foundwhere=binding.foundWhere.text.toString()
                    val claim1=binding.claim1.text.toString()
                    database=FirebaseDatabase.getInstance().getReference("found")

                    val user= found(mProfileImageURL,item_name2, foundwhere, claim1)
                    database.child(item_name2).setValue(user).addOnSuccessListener{
                        binding.etItemName2.text?.clear()
                        binding.foundWhere.text?.clear()
                        binding.claim1.text?.clear()
                        finish()
                        Toast.makeText(this,"added successfully",Toast.LENGTH_SHORT).show()
                }}

            }.addOnFailureListener{
                    exception->
                Toast.makeText(
                    this@foundActivity,
                    exception.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}