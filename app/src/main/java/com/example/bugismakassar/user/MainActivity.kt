package com.example.bugismakassar.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bugismakassar.R
import com.example.bugismakassar.authentication.LoginActivity
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.data.User
import com.example.bugismakassar.databinding.ActivityMainBinding
import com.example.bugismakassar.user.fragment.FragmentHome
import com.example.bugismakassar.user.fragment.adapter.ArticleAdapter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var database : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        setSupportActionBar(binding.appBarMain.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_sejarah_bugis, R.id.nav_lontara, R.id.nav_rumah_adat, R.id.nav_pakaian_adat,
                R.id.nav_adat_pernikahan, R.id.nav_tempat_wisata, R.id.nav_musik_tradisional, R.id.nav_tarian_tradisional,
                R.id.nav_info, R.id.nav_hot_news, R.id.nav_hot_spot
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfig)
        binding.navView.setupWithNavController(navController)

        val name = intent.getStringExtra("EXTRA_NAME")

        database.child("user").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(User::class.java)
                    val header = binding.navView.getHeaderView(0)
                    val navName = header.findViewById<TextView>(R.id.tv_name)

                    if (userData != null) {
                        navName.text = userData.name
                    };
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}