package com.example.lostandfound3.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.lostandfound3.R
import com.example.lostandfound3.activities.models.User
import com.google.firebase.auth.FirebaseAuth

class signin : BaseActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        setupActionBar()
        auth=FirebaseAuth.getInstance()
    val registerbutton:Button=findViewById(R.id.button6)
        val signinbutton:Button=findViewById(R.id.btn4)
        registerbutton.setOnClickListener {
            startActivity(Intent(this, newsignup::class.java))
        }
        signinbutton.setOnClickListener {
            signInRegisteredUser()
        }
}

    fun signInRegisteredUser(){

       val semail:androidx.appcompat.widget.AppCompatEditText=findViewById(R.id.et_email_sign_in)
       val spassword:androidx.appcompat.widget.AppCompatEditText=findViewById(R.id.password_sign_in)
       val email:String = semail.text.toString().trim{ it <= ' '}
       val password:String = spassword.text.toString().trim{ it <= ' '}
       if(validateForms(email,password)){
showProgressDialog("please wait")
           auth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(this) { task ->
                   hideProgressDialog()

                   if (task.isSuccessful) {
                       // Sign in success, update UI with the signed-in user's information
                       Log.d("sign in ", "createUserWithEmail:success")
                       val user= auth.currentUser
                       startActivity(Intent(this,MainActivity::class.java))
                   } else {
                       // If sign in fails, display a message to the user.
                       Log.w("sign in ", "createUserWithEmail:failure", task.exception)
                       Toast.makeText(baseContext, "Authentication failed.",
                           Toast.LENGTH_SHORT).show()
                   }
               }
       }
   }




    private fun validateForms(email:String,password:String):Boolean {

        return when {

            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("please enter your email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("please enter a password")
                false
            }
            else->{
                true
            }
        }
    }

    fun signInSuccess(loggedInUser: User?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    private fun setupActionBar(){
        val tool_my_profile_activity:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar_my_signin_activity)
        setSupportActionBar(tool_my_profile_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar?.title="SIGN_IN"}
        tool_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()

        }
    }
}
