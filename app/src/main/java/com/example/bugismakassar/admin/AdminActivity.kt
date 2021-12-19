package com.example.bugismakassar.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bugismakassar.R
import com.example.bugismakassar.databinding.ActivityAdminBinding
import com.example.bugismakassar.databinding.ActivityMainBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMainAdmin.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main_admin)
        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.nav_admin_home, R.id.nav_admin_sejarah_bugis, R.id.nav_admin_lontara, R.id.nav_admin_rumah_adat, R.id.nav_admin_pakaian_adat,
                R.id.nav_admin_adat_pernikahan, R.id.nav_admin_tempat_wisata, R.id.nav_admin_musik_tradisional, R.id.nav_admin_tarian_tradisional,
                R.id.nav_admin_info, R.id.nav_admin_hot_news, R.id.nav_admin_hot_spot
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfig)
        binding.navViewAdmin.setupWithNavController(navController)

        val header = binding.navViewAdmin.getHeaderView(0)
        val navName = header.findViewById<TextView>(R.id.tv_name)

        navName.setText("Admin")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_admin)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}