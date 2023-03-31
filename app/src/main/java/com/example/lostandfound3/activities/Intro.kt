package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.lostandfound3.activities.newsignup
import com.example.lostandfound3.activities.signin
import com.example.lostandfound3.R


class intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
val btn_signin:Button=findViewById(R.id.button_1)

        btn_signin.setOnClickListener {
            startActivity(Intent(this, signin::class.java))
        }
        val btn_signup:Button=findViewById(R.id.button_2)
       btn_signup.setOnClickListener {
           startActivity(Intent(this, newsignup::class.java))
       }

    }}