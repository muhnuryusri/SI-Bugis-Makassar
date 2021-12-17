package com.example.bugismakassar.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bugismakassar.data.User
import com.example.bugismakassar.databinding.ActivityRegisterBinding
import com.example.bugismakassar.user.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        binding.btnSignUp.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()

            if (name.isEmpty() && email.isEmpty() && username.isEmpty() && password.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Mohon lengkapi data anda", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(name, email, username, password)
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val userAuth = auth.currentUser

                            database.child(username).setValue(user).addOnSuccessListener {
                                Toast.makeText(this,"Registrasi berhasil",Toast.LENGTH_SHORT).show()
                                updateUI(userAuth)
                            }.addOnFailureListener{
                                Toast.makeText(this,"Registrasi gagal",Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            }
        }


        binding.navLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}