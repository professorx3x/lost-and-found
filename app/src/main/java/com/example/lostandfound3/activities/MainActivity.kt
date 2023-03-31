package com.example.lostandfound3.activities

import activities.intro
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.lostandfound3.activities.bottomNavigation.fragments.Fragments.foundFragment
import com.example.lostandfound3.activities.bottomNavigation.fragments.Fragments.lostFragment
import com.example.lostandfound3.activities.bottomNavigation.fragments.Fragments.recoveredFragment
import com.example.lostandfound3.activities.models.User
import com.example.lostandfound3.firebase.FirebaseStore
import com.example.lostandfound3.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
class MainActivity :AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foundFragment=foundFragment()
        val lostFragment=lostFragment()
        val recoveredFragment= recoveredFragment()

        makeCurrentFragment(foundFragment)
        val bottomnavigation:BottomNavigationView=findViewById(R.id.bottom_navigation)
        bottomnavigation.setOnItemSelectedListener{
           when(it.itemId){
               R.id.ic_found->makeCurrentFragment(foundFragment)
               R.id.ic_lost->makeCurrentFragment(lostFragment)
               R.id.items_recovered->makeCurrentFragment(recoveredFragment)
           }
            true
        }
        setupActionBar()

        FirebaseStore().loadUserData(this)
        val nav_view:NavigationView=findViewById(R.id.nav_view)
        nav_view.setNavigationItemSelectedListener(this)

    }
    private fun makeCurrentFragment(fragment: Fragment) {
val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper,fragment)
        fragmentTransaction.commit()
    }
    private fun setupActionBar(){
        val tool_main_activity:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar_main_activity)
        setSupportActionBar(tool_main_activity)
        tool_main_activity.title="Lost and Found"
        tool_main_activity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        tool_main_activity.setNavigationOnClickListener{
//Toggle drawer

            toggleDrawer()
        }
    }
    private fun toggleDrawer(){
        val drawer_layout:androidx.drawerlayout.widget.DrawerLayout=findViewById(R.id.drawer_layout)
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.nav_my_profile->{
             startActivity(Intent(this,MyProfileActivity::class.java))
            }
            R.id.nav_sign_out->{
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this,intro::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.addlostitem->{
                startActivity(Intent(this,lostActivity::class.java))
            }
            R.id.addfounditem->{
                startActivity(Intent(this,foundActivity::class.java))
            }
            R.id.addrecovereditem->{
                startActivity(Intent(this,recovered::class.java))
            }

        }
        val drawer_layout:androidx.drawerlayout.widget.DrawerLayout=findViewById(R.id.drawer_layout)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user:User) {
        val nav_user_image:de.hdodenhof.circleimageview.CircleImageView=findViewById(R.id.nav_user_image)
        val tv_username:TextView=findViewById(R.id.tv_username)
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(nav_user_image);
             tv_username.text=user.name
    }
}
