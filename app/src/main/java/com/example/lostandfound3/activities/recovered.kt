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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.lostandfound3.R
import com.example.lostandfound3.activities.models.lost
import com.example.lostandfound3.activities.models.recoveredData
import com.example.lostandfound3.databinding.ActivityLostBinding
import com.example.lostandfound3.databinding.ActivityRecoveredBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException

class recovered : BaseActivity() {
    private var mSelectedImageFileUri2: Uri?=null
    private var mProfileImageURL:String=""
    private lateinit var binding:ActivityRecoveredBinding

    private lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRecoveredBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ADD3.setOnClickListener {
            showProgressDialog("please wait")
            if(mSelectedImageFileUri2!=null){
                val iv_user_image:de.hdodenhof.circleimageview.CircleImageView=findViewById(R.id.iv_board_image3)
                iv_user_image.setImageURI(mSelectedImageFileUri2).toString()
                uploadUserImage()
            }
        }
        binding.ivBoardImage3.setOnClickListener {
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
    private fun showImageChooser(){
        var galleryIntent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, MyProfileActivity.PICK_IMAGE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK&&requestCode== MyProfileActivity.PICK_IMAGE_REQUEST_CODE &&data!!.data!=null)
        {
            val iv_user_image:de.hdodenhof.circleimageview.CircleImageView=findViewById(R.id.iv_board_image3)
            mSelectedImageFileUri2=data.data
            try {
                Glide
                    .with(this@recovered)
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

            val sRef= FirebaseStorage.getInstance().reference.child("recovered_image"+ System.currentTimeMillis()+"."+getFileExtension(mSelectedImageFileUri2))
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
                    val item_name3=binding.etItemName3.text.toString()
                    val claimedby=binding.claimedby.text.toString()
                    val recovered3=binding.recovered3.text.toString()
                    database=FirebaseDatabase.getInstance().getReference("recovered")

                    val user= recoveredData(mProfileImageURL,item_name3,claimedby,recovered3)
                    database.child(item_name3).setValue(user).addOnSuccessListener{
                        binding.etItemName3.text?.clear()
                        binding.claimedby.text?.clear()
                        binding.recovered3.text?.clear()
                        finish()
                        Toast.makeText(this,"added successfully",Toast.LENGTH_SHORT).show()
                    }}

            }.addOnFailureListener{
                    exception->
                Toast.makeText(
                    this@recovered,
                    exception.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}