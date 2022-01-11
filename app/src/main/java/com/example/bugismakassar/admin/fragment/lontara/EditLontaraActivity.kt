package com.example.bugismakassar.admin.fragment.lontara

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.ActivityEditAdatPernikahanBinding
import com.example.bugismakassar.databinding.ActivityEditLontaraBinding
import com.example.bugismakassar.databinding.FragmentAddHotSpotBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditLontaraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditLontaraBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference

    private var mediaData: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLontaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Lontara")
        storage = FirebaseStorage.getInstance().reference.child("image").child("IMG"+System.currentTimeMillis())

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
            if (mediaData !== null) {
                binding.progressBar.visibility = View.VISIBLE
                mediaData?.let { it1 ->
                    storage.putFile(it1).addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                        storage.downloadUrl.addOnSuccessListener {
                            if (editArticle != null) {
                                updateDataToFirebaseDatabase(it.toString(), editArticle)
                            }
                        }
                        Toast.makeText(this@EditLontaraActivity,"Update Berhasil", Toast.LENGTH_SHORT).show()
                    })
                }
            } else {
                if (editArticle != null) {
                    updateDataToFirebaseDatabaseWithoutImage(editArticle)
                }
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

    private fun updateDataToFirebaseDatabase(profileImageUrl: String, article: Article) {
        database = FirebaseDatabase.getInstance().reference.child("Lontara")

        val title = binding.edtTitle.text.toString().trim()
        val source = binding.edtSource.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()

        val articleData = Article(article.id, title, profileImageUrl, source, description, article.type)

        if (title.isNotEmpty() && profileImageUrl.isNotEmpty() && source.isNotEmpty() && description.isNotEmpty()) {
            article.id?.let {
                database.child(it).setValue(articleData).addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@EditLontaraActivity, "Update Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@EditLontaraActivity, AdminActivity::class.java)
                    startActivity(intent)
                }
                    .addOnFailureListener {
                        Toast.makeText(this@EditLontaraActivity,"Update Gagal", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun updateDataToFirebaseDatabaseWithoutImage(article: Article) {
        database = FirebaseDatabase.getInstance().reference.child("Lontara")

        val title = binding.edtTitle.text.toString().trim()
        val source = binding.edtSource.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()

        val articleData = Article(article.id, title, article.media, source, description, article.type)

        article.id?.let {
            database.child(it).setValue(articleData).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@EditLontaraActivity, "Update Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditLontaraActivity, AdminActivity::class.java)
                startActivity(intent)
            }
                .addOnFailureListener {
                    Toast.makeText(this@EditLontaraActivity,"Update Gagal", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}