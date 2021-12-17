package com.example.bugismakassar.admin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.bugismakassar.R
import com.example.bugismakassar.authentication.LoginActivity
import com.example.bugismakassar.databinding.FragmentAdminHomeBinding
import com.example.bugismakassar.databinding.FragmentHomeBinding

class FragmentAdminHome : Fragment() {
    private lateinit var binding: FragmentAdminHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSejarahBugis.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_sejarah_bugis)
        }
        binding.btnLontara.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_lontara)
        }
        binding.btnRumahAdat.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_rumah_adat)
        }
        binding.btnPakaianAdat.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_pakaian_adat)
        }
        binding.btnAdatPernikahan.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_adat_pernikahan)
        }
        binding.btnTempatWisata.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_tempat_wisata)
        }
        binding.btnMusikTradisional.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_musik_tradisional)
        }
        binding.btnTarianTradisional.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_tarian_tradisional)
        }
        binding.btnInfoAplikasi.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_info)
        }
        binding.btnHotNews.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_hot_news)
        }
        binding.btnHotSpot.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_home_to_nav_admin_hot_spot)
        }
        binding.btnLogOut.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(context,"Logout berhasil", Toast.LENGTH_SHORT).show()
            true
        }
    }
}