package com.example.bugismakassar.admin.fragment.sejarah_bugis

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.ActivityEditRumahAdatBinding
import com.example.bugismakassar.databinding.ActivityEditSejarahBugisBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditSejarahBugisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSejarahBugisBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference

    private var mediaData: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSejarahBugisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Sejarah Bugis")
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
            binding.progressBar.visibility = View.VISIBLE
            mediaData?.let { it1 ->
                storage.putFile(it1).addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                    storage.downloadUrl.addOnSuccessListener {
                        if (editArticle != null) {
                            updateDataToFirebaseDatabase(it.toString(), editArticle)
                        }
                    }
                    Toast.makeText(this@EditSejarahBugisActivity,"Update Berhasil", Toast.LENGTH_SHORT).show()
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

    private fun updateDataToFirebaseDatabase(profileImageUrl: String, article: Article) {
        database = FirebaseDatabase.getInstance().reference.child("Sejarah Bugis")

        val title = binding.edtTitle.text.toString().trim()
        val source = binding.edtSource.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()

        val articleData = Article(article.id, title, profileImageUrl, source, description, article.type)

        article.id?.let {
            database.child(it).setValue(articleData).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@EditSejarahBugisActivity, "Update Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditSejarahBugisActivity, AdminActivity::class.java)
                startActivity(intent)
            }
                .addOnFailureListener {
                    Toast.makeText(this@EditSejarahBugisActivity,"Update Gagal", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}