package com.example.lostandfound3.activities
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.example.lostandfound3.activities.models.User
import com.example.lostandfound3.firebase.FirebaseStore
import com.example.lostandfound3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Calendar
class newsignup :BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsignup)
setupActionBar()
        val myCalendar= Calendar.getInstance()
        val datePicker=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)

            }
        val btn_sign_up:Button=findViewById(R.id.btn5)
        val btn6:Button=findViewById(R.id.btn6)
        btn_sign_up.setOnClickListener {
            registerUser()
        }
        btn6.setOnClickListener {
            startActivity(Intent(this,signin::class.java))
        }
    }
    fun userRegisteredSuccess(){
        Toast.makeText(this," you have"+" successfully registered ",Toast.LENGTH_LONG).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
    private fun registerUser(){
        val et_name:androidx.appcompat.widget.AppCompatEditText=findViewById(R.id.et_name)
        val et_email:androidx.appcompat.widget.AppCompatEditText=findViewById(R.id.et_email)
        val et_password:androidx.appcompat.widget.AppCompatEditText=findViewById(R.id.pass)

        val et_mobile:androidx.appcompat.widget.AppCompatEditText=findViewById(R.id.contact)

        val name:String = et_name.text.toString().trim{ it <= ' '}
        val email:String = et_email.text.toString().trim{ it <= ' '}
        val password:String = et_password.text.toString().trim{ it <= ' '}

        val mobile:String = et_mobile.text.toString().trim{ it <= ' '}

        if (validateForm(name,email,password,mobile)){
            showProgressDialog("please wait")
           FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
               {
                   task->
                   hideProgressDialog()
                  if(task.isSuccessful){
                      val firebaseUser:FirebaseUser=task.result!!.user!!
                      val registeredEmail=firebaseUser.email!!
                     val user= User(firebaseUser.uid,name,registeredEmail,mobile,)
                      FirebaseStore().registerUser(this,user)
                  }else{
                      Toast.makeText(this,"Registration failed",Toast.LENGTH_SHORT).show()
                  }

               }
           )
        }
    }
    private fun validateForm(
        name: String,
        email: String,
        mobile: String,
        password: String
    ):Boolean {

        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("please enter your name")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("please enter your email")
                false
            }
            TextUtils.isEmpty(mobile) -> {
                showErrorSnackBar("please enter your mobile no.")
                false
            }TextUtils.isEmpty(password) -> {
                showErrorSnackBar("please enter a password")
                false
            }
            else->{
                true
            }
        }

    }
    private fun setupActionBar(){
        val tool_my_profile_activity:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar_my_signup_activity)
        setSupportActionBar(tool_my_profile_activity)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            actionBar.title="SIGN_UP"}
        tool_my_profile_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}