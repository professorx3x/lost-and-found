package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.lostandfound3.R
import com.example.lostandfound3.activities.MainActivity
import com.example.lostandfound3.firebase.FirebaseStore

class splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        Handler().postDelayed(
            {
                var currentUserID=FirebaseStore().getCurrentUserId()
                if (currentUserID.isNotEmpty()){
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else{
               startActivity(Intent(this, activities.intro::class.java))
                finish()}
            },2500)
    }
}