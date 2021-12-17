package com.example.bugismakassar.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.data.User
import com.example.bugismakassar.databinding.ActivityLoginBinding
import com.example.bugismakassar.user.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
//            binding.progressBar.visibility = View.VISIBLE

            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Masukkan username dan password anda", Toast.LENGTH_SHORT).show()
            } else {

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(baseContext, "Login berhasil",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Username atau password anda salah",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

/*                database.child("Users").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var isLoginMatch : Boolean? = false
                        var dataName : String? = null
                        var dataUsername : String? = null
                        var dataPassword : String? = null

                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)

                            dataName = user!!.name
                            dataUsername = user.username
                            dataPassword = user.password

                            if (dataUsername == username && dataPassword == password) {
                                isLoginMatch = true
                                break
                            }
                        }

                        if (isLoginMatch == true) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("EXTRA_NAME", dataName)
                            startActivity(intent)
                            Toast.makeText(this@LoginActivity,"Login berhasil", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity,"Username atau password anda salah",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting user failed
                        Toast.makeText(this@LoginActivity,"User tidak ditemukan",Toast.LENGTH_SHORT).show()
                    }
                })*/
            }
        }

        binding.navSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.navLoginAdmin.setOnClickListener {
            val intent = Intent(this@LoginActivity, AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }
}