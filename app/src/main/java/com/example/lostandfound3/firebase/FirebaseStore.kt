package com.example.lostandfound3.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.lostandfound3.activities.MainActivity
import com.example.lostandfound3.activities.MyProfileActivity
import com.example.lostandfound3.activities.models.User
import com.example.lostandfound3.activities.newsignup
import com.example.lostandfound3.activities.signin
import com.example.lostandfound3.activities.utils.constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class FirebaseStore{
    private val mFireStore=FirebaseFirestore.getInstance()
    fun registerUser(activity:newsignup,userInfo:User){
     mFireStore.collection(constants.USERS)
         .document(getCurrentUserId()) .set(userInfo, SetOptions.merge())
         .addOnSuccessListener {
             activity.userRegisteredSuccess()
         }.addOnFailureListener{
             e->
             Log.e(activity.javaClass.simpleName,"Error")
         }
    }
    fun updateUserProfileData(activity: MyProfileActivity,userHashMap: HashMap<String,Any>){
        mFireStore.collection(constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName,"Profile Data updated successfully")
                Toast.makeText(activity,"Profile updated successfully",Toast.LENGTH_LONG).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener {
                e->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating",
                    e
                )
                Toast.makeText(activity,"profile update unsuccessfull",Toast.LENGTH_LONG).show()
            }
    }
    fun loadUserData(activity: Activity){
        mFireStore.collection(constants.USERS)
            .document(getCurrentUserId()) .get()
            .addOnSuccessListener {document->
               val loggedInUser=document.toObject(User::class.java)!!
                if (loggedInUser!=null)
                    when(activity) {
                        is signin -> {
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity->{
                            activity.updateNavigationUserDetails(loggedInUser)
                        }is MyProfileActivity->{
                            activity.setUserDataInUI(loggedInUser)
                        }
                    }
            }.addOnFailureListener{
                    e->
                Log.e(activity.javaClass.simpleName,"Error writing document")
            }
    }
    fun getCurrentUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}