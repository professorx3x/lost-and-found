package com.example.lostandfound3.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.lostandfound3.activities.models.User
import com.example.lostandfound3.activities.utils.constants
import com.example.lostandfound3.firebase.FirebaseStore
import com.example.lostandfound3.R
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException


class MyProfileActivity : BaseActivity() {
    companion object{
        internal const val READ_STORAGE_PERMISSION_CODE=1
        internal const val PICK_IMAGE_REQUEST_CODE=2
    }
    private var mSelectedImageFileUri:Uri?=null
    private var mProfileImageURL:String=""
    private lateinit var mUserDetails:User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()
        FirebaseStore().loadUserData(this)
        val iv_profile_user_image: de.hdodenhof.circleimageview.CircleImageView =
            findViewById(R.id.iv_profile_user_image)
        iv_profile_user_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        val btn_update:Button=findViewById(R.id.btn_update)
        btn_update.setOnClickListener {
            showProgressDialog("please wait")
            if(mSelectedImageFileUri!=null){

                val iv_user_image:de.hdodenhof.circleimageview.CircleImageView=findViewById(R.id.iv_profile_user_image)
                iv_user_image.setImageURI(mSelectedImageFileUri).toString()
                uploadUserImage()
            } else{

                updateUserProfileData()
            }
        }
        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == READ_STORAGE_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showImageChooser()
                }
            }else{
                Toast.makeText(
                    this,
                    "Oops,you just denied the permission for storage.you can allow it from settings",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun showImageChooser(){
        var galleryIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
            }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK&&requestCode== PICK_IMAGE_REQUEST_CODE&&data!!.data!=null)
        {
            val iv_user_image:de.hdodenhof.circleimageview.CircleImageView=findViewById(R.id.iv_profile_user_image)
            mSelectedImageFileUri=data.data
           try {
               Glide
                   .with(this@MyProfileActivity)
                   .load(mSelectedImageFileUri)
                   .centerCrop()
                   .placeholder(R.drawable.ic_user_place_holder)
                   .into(iv_user_image);
           } catch (e:IOException){
               e.printStackTrace()
           }
        }
    }
   private fun setupActionBar(){
       val tool_my_profile_activity:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar_my_profile_activity)
        setSupportActionBar(tool_my_profile_activity)
     val actionBar=supportActionBar
     if (actionBar!=null){
        actionBar.setDefaultDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
       actionBar?.title="My Profile"}
      tool_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()

        }
   }
    fun setUserDataInUI(user: User){
        mUserDetails=user
        val et_name:AppCompatEditText=findViewById(R.id.et_name)
        val et_mobile:androidx.appcompat.widget.AppCompatEditText=findViewById(R.id.et_mobile)
        val iv_user_image:de.hdodenhof.circleimageview.CircleImageView=findViewById(R.id.iv_profile_user_image)
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_user_image);
        et_name.setText(user.name)
        et_mobile.setText(user.mobile)

    }
     private fun updateUserProfileData(){
        val et_name:AppCompatEditText=findViewById(R.id.et_name)
        val userHashMap=HashMap<String,Any>()
        if(mProfileImageURL.isNotEmpty()&& mProfileImageURL!=mUserDetails.image){
            userHashMap[constants.IMAGE]=mProfileImageURL}
        if (et_name.text.toString()!=mUserDetails.name){
            userHashMap[constants.NAME]=et_name.text.toString()
        }
        val et_mobile:AppCompatEditText=findViewById(R.id.et_mobile)
        if (et_mobile.text.toString()!=mUserDetails.mobile){
            userHashMap[constants.MOBILE]=et_mobile.text.toString()
        }
        FirebaseStore().updateUserProfileData(this,userHashMap)
    }

    private fun uploadUserImage(){
        if(mSelectedImageFileUri!=null){
            val sRef= FirebaseStorage.getInstance().reference.child("User_image"+ System.currentTimeMillis()+"."+getFileExtension(mSelectedImageFileUri))
                sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener{
                    taskSnapshot->
                    Log.i(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener{
                        uri->
                        Log.i("Downloadable Image URL", uri.toString())
                        mProfileImageURL=uri.toString()
                        updateUserProfileData()
                    }

                }.addOnFailureListener{
                    exception->
                    Toast.makeText(
                        this@MyProfileActivity,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
    private fun getFileExtension(uri:Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }
    fun profileUpdateSuccess(){
        finish()
    }
}