package com.example.bugismakassar.admin.fragment.musik_tradisional

import android.app.Activity
import android.app.ProgressDialog
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
import com.example.bugismakassar.databinding.ActivityEditMusikTradisionalBinding
import com.example.bugismakassar.databinding.ActivityEditMusikTradisionalVideoBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditMusikTradisionalActivityVideo : AppCompatActivity() {
    private lateinit var binding: ActivityEditMusikTradisionalVideoBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference

    private var mediaData: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMusikTradisionalVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Musik Tradisional")
        storage = FirebaseStorage.getInstance().reference.child("video").child("VID"+System.currentTimeMillis())

        val progressDialog = ProgressDialog(this)

        val editArticle = intent.getParcelableExtra<Article>(EXTRA_ARTICLE)
        if (editArticle != null) {
            showArticleData(editArticle)
        }

        binding.btnUploadMedia.setOnClickListener {
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.setType("video/*")
            startActivityForResult(mediaIntent, 2)
        }

        binding.btnUpdate.setOnClickListener {
            if (mediaData !== null) {
                binding.progressBar.visibility = View.VISIBLE
                progressDialog.setTitle("Uploading")
                progressDialog.setMessage("Video sedang diupload, mohon tunggu")
                progressDialog.show()

                mediaData?.let { it1 ->
                    storage.putFile(it1).addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                        storage.downloadUrl.addOnSuccessListener {
                            if (editArticle != null) {
                                progressDialog.dismiss()
                                updateDataToFirebaseDatabase(it.toString(), editArticle)
                            }
                        }
                        Toast.makeText(this@EditMusikTradisionalActivityVideo,"Update Berhasil", Toast.LENGTH_SHORT).show()
                    }).addOnProgressListener { snapshot ->
                        val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount);
                        progressDialog.setMessage("Uploaded $progress%");
                    }
                }
            } else {
                if (editArticle != null) {
                    updateDataToFirebaseDatabaseWithoutVideo(editArticle)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2) {
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
        database = FirebaseDatabase.getInstance().reference.child("Musik Tradisional")

        val title = binding.edtTitle.text.toString().trim()
        val source = binding.edtSource.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()

        val articleData = Article(article.id, title, profileImageUrl, source, description, article.type)

        article.id?.let {
            database.child(it).setValue(articleData).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@EditMusikTradisionalActivityVideo, "Update Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditMusikTradisionalActivityVideo, AdminActivity::class.java)
                startActivity(intent)
            }
                .addOnFailureListener {
                    Toast.makeText(this@EditMusikTradisionalActivityVideo,"Update Gagal", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateDataToFirebaseDatabaseWithoutVideo(article: Article) {
        database = FirebaseDatabase.getInstance().reference.child("Musik Tradisional")

        val title = binding.edtTitle.text.toString().trim()
        val source = binding.edtSource.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()

        val articleData = Article(article.id, title, article.media, source, description, article.type)

        article.id?.let {
            database.child(it).setValue(articleData).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@EditMusikTradisionalActivityVideo, "Update Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@EditMusikTradisionalActivityVideo, AdminActivity::class.java)
                startActivity(intent)
            }
                .addOnFailureListener {
                    Toast.makeText(this@EditMusikTradisionalActivityVideo,"Update Gagal", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}