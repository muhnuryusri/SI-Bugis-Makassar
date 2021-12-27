package com.example.bugismakassar.user.fragment.hotspot

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.ActivityEditHotSpotBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditHotSpotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditHotSpotBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private var mediaData: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditHotSpotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("Hot Spot")
        storage = FirebaseStorage.getInstance().reference.child("image")

        val editContent = intent.getParcelableExtra<Content>(EXTRA_CONTENT)
        if (editContent != null) {
            showContentData(editContent)
        }

        binding.btnUploadMedia.setOnClickListener {
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.setType("image/*")
            startActivityForResult(mediaIntent, 1)
        }

        binding.btnUpdate.setOnClickListener {
            mediaData?.let { it1 ->
                storage.putFile(it1).addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                    storage.downloadUrl.addOnSuccessListener {
                        if (editContent != null) {
                            updateDataToFirebaseDatabase(it.toString(), editContent)
                        }
                    }
                    Toast.makeText(this@EditHotSpotActivity,"Update Berhasil", Toast.LENGTH_SHORT).show()
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

    private fun showContentData(content: Content) {
        binding.edtTitle.setText(content.title)
        Glide.with(this)
            .load(content.media)
            .into(binding.tvImage)
        binding.edtDescription.setText(content.description)
        binding.edtUploader.setText(content.uploader)
    }

    private fun updateDataToFirebaseDatabase(profileImageUrl: String, content: Content) {
        database = FirebaseDatabase.getInstance().reference.child("Hot Spot")
        val title = binding.edtTitle.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val uploader = binding.edtUploader.text.toString().trim()

        val contentData = Content(content.id, title, profileImageUrl, description, uploader, content.type)

        content.id?.let {
            database.child(it).setValue(contentData).addOnSuccessListener {
                Toast.makeText(this@EditHotSpotActivity, "Update Berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
                .addOnFailureListener {
                    Toast.makeText(this@EditHotSpotActivity,"Update Gagal", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        const val EXTRA_CONTENT = "extra_content"
    }
}