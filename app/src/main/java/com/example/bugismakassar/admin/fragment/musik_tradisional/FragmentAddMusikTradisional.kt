package com.example.bugismakassar.admin.fragment.musik_tradisional

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.FragmentAddHotNewsBinding
import com.example.bugismakassar.databinding.FragmentAddMusikTradisionalBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FragmentAddMusikTradisional : Fragment() {
    private lateinit var binding : FragmentAddMusikTradisionalBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var auth: FirebaseAuth
    private var mediaData: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMusikTradisionalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                mediaData = data?.data
                binding.tvImage.setImageURI(mediaData)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference.child("Musik Tradisional")
        storage = FirebaseStorage.getInstance().reference.child("image")
        auth = FirebaseAuth.getInstance()

        binding.btnUploadMedia.setOnClickListener {
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.setType("image/*")
            startActivityForResult(mediaIntent, 1)
        }

        binding.btnUpload.setOnClickListener {
            mediaData?.let { it1 ->
                storage.putFile(it1).addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                    storage.downloadUrl.addOnSuccessListener {
                        saveDataToFirebaseDatabase(it.toString())
                    }
                    Toast.makeText(context,"Upload berhasil", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    private fun saveDataToFirebaseDatabase(profileImageUrl: String) {
        database = FirebaseDatabase.getInstance().reference.child("Musik Tradisional")

        val id = database.push().key
        val title = binding.edtTitle.text.toString()
        val description = binding.edtDescription.text.toString()
        val source = binding.edtSource.text.toString()
        val type = 0

        val article = Article(id, title, profileImageUrl, source, description, type)

        if (id != null) {
            database.child(id).setValue(article).addOnSuccessListener {
                Toast.makeText(context, "Upload Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, AdminActivity::class.java)
                startActivity(intent)
            }
                .addOnFailureListener {
                    Toast.makeText(context,"Upload Gagal", Toast.LENGTH_SHORT).show()
                }
        }
    }
}