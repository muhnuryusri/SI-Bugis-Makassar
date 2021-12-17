package com.example.bugismakassar.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.data.Admin
import com.example.bugismakassar.data.User
import com.example.bugismakassar.databinding.ActivityAdminLoginBinding
import com.example.bugismakassar.databinding.ActivityLoginBinding
import com.example.bugismakassar.user.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class AdminLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
//            binding.progressBar.visibility = View.VISIBLE

            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this@AdminLoginActivity, "Masukkan username dan password anda", Toast.LENGTH_SHORT).show()
            } else {
//                readData(username, password)

                database.child("Admin").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var isLoginMatch : Boolean? = false
                        var dataEmail : String? = null
                        var dataPassword : String? = null

                        for (adminSnapshot in dataSnapshot.children) {
                            val admin = adminSnapshot.getValue(Admin::class.java)

                            dataEmail = admin!!.adminEmail
                            dataPassword = admin.adminPassword

                            if (dataEmail == email && dataPassword == password) {
                                isLoginMatch = true
                                break
                            }
                        }

                        if (isLoginMatch == true) {
                            val intent = Intent(this@AdminLoginActivity, AdminActivity::class.java)
                            intent.putExtra("logged_in_username", dataEmail)
                            startActivity(intent)
                            Toast.makeText(this@AdminLoginActivity,"Login berhasil", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AdminLoginActivity,"Username atau password anda salah",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting user failed
                        Toast.makeText(this@AdminLoginActivity,"User tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.navLoginUser.setOnClickListener {
            val intent = Intent(this@AdminLoginActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}