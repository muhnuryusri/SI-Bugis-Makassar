package com.example.bugismakassar.admin.fragment.adat_pernikahan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.data.User
import com.example.bugismakassar.databinding.ActivityEditAdatPernikahanBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditAdatPernikahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdatPernikahanBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private var mediaData: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdatPernikahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Adat Pernikahan")
        storage = FirebaseStorage.getInstance().reference.child("image")

        val editArticle = intent.getParcelableExtra<Article>(EXTRA_ARTICLE)
        if (editArticle != null) {
            showArticleData(editArticle)
        }

        binding.btnUploadMedia.setOnClickListener {
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.setType("image/*")
            startActivityForResult(mediaIntent, 1)
        }

        binding.btnUpdate.setOnClickListener {
            val media = mediaData?.let { it1 ->
                storage.putFile(it1).addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                    storage.downloadUrl.addOnSuccessListener {
                        updateDataToFirebaseDatabase(it.toString())
                    }
                    Toast.makeText(this@EditAdatPernikahanActivity,"Upload berhasil", Toast.LENGTH_SHORT).show()
                })
            }
        }
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

    private fun showArticleData(article: Article) {
        binding.edtTitle.setText(article.title)
        Glide.with(this)
            .load(article.media)
            .into(binding.tvImage)
        binding.edtSource.setText(article.source)
        binding.edtDescription.setText(article.description)
    }

    private fun updateDataToFirebaseDatabase(profileImageUrl: String) {
        database = FirebaseDatabase.getInstance().reference.child("Adat Pernikahan")
        val id = "-Mrm1Id5CzZgR-xM7ne6"
        val title = binding.edtTitle.text.toString().trim()
        val source = binding.edtSource.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val type = 0

        val articleData = Article(id, title, profileImageUrl, source, description, type)

        database.child(id).setValue(articleData).addOnSuccessListener {
            Toast.makeText(this@EditAdatPernikahanActivity, "Update Berhasil", Toast.LENGTH_SHORT).show()
            finish()
        }
            .addOnFailureListener {
                Toast.makeText(this@EditAdatPernikahanActivity,"Update Gagal", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}