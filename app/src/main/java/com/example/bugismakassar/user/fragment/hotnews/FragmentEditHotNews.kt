package com.example.bugismakassar.user.fragment.hotnews

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.data.Admin
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.data.User
import com.example.bugismakassar.databinding.FragmentAddHotNewsBinding
import com.example.bugismakassar.databinding.FragmentEditHotNewsBinding
import com.example.bugismakassar.user.MainActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FragmentEditHotNews : Fragment() {
    private lateinit var binding : FragmentEditHotNewsBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var auth: FirebaseAuth
    private var mediaData: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditHotNewsBinding.inflate(layoutInflater)
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

        database = FirebaseDatabase.getInstance().reference.child("Hot News")
        storage = FirebaseStorage.getInstance().reference.child("image")
        auth = FirebaseAuth.getInstance()

        val titleIntent = activity?.intent?.getStringExtra("title")
        database.child("1").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(Content::class.java)
                    val title = binding.edtTitle
                    val description = binding.edtDescription
                    val uploader = binding.edtUploader
                    if (userData != null) {
                        title.setText(userData.title)
                        description.setText(userData.description)
                        uploader.setText(userData.uploader)
                    };
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.btnUploadMedia.setOnClickListener {
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.setType("image/*")
            startActivityForResult(mediaIntent, 1)
        }

        binding.btnUpload.setOnClickListener {
            mediaData?.let { it1 ->
                storage.putFile(it1).addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                    storage.downloadUrl.addOnSuccessListener {
                        editData(it.toString())
                    }
                    Toast.makeText(context,"Upload berhasil", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    private fun editData(profileImageUrl: String) {
        val title = binding.edtTitle.text.toString()
        val description = binding.edtDescription.text.toString()
        val uploader = binding.edtUploader.text.toString()
        val type = 0

        database = FirebaseDatabase.getInstance().reference.child("Hot News")

        val content = Content(title, profileImageUrl, description, uploader, type)

        database.child(title).setValue(content)
            .addOnSuccessListener {
                Toast.makeText(context, "Upload Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, AdminActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(context,"Upload Gagal", Toast.LENGTH_SHORT).show()
            }
    }
}