package com.example.bugismakassar.user.fragment

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
import com.example.bugismakassar.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class FragmentHome : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        super.onViewCreated(view, savedInstanceState)
        binding.btnSejarahBugis.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_sejarah_bugis)
        }
        binding.btnLontara.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_lontara)
        }
        binding.btnRumahAdat.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_rumah_adat)
        }
        binding.btnPakaianAdat.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_pakaian_adat2)
        }
        binding.btnAdatPernikahan.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_adat_pernikahan2)
        }
        binding.btnTempatWisata.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_tempat_wisata)
        }
        binding.btnMusikTradisional.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_musik_tradisional)
        }
        binding.btnTarianTradisional.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_tarian_tradisional)
        }
        binding.btnInfoAplikasi.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_info)
        }
        binding.btnHotNews.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_hot_news)
        }
        binding.btnHotSpot.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_hot_spot)
        }
        binding.btnLogOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(context,"Logout berhasil", Toast.LENGTH_SHORT).show()
            true
        }
    }
}