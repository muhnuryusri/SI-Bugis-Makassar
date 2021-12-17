package com.example.bugismakassar.admin.fragment.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.FragmentAddArticleBinding
import com.example.bugismakassar.user.MainActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FragmentAddArticle : Fragment() {
    private lateinit var binding : FragmentAddArticleBinding
    private lateinit var database : DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var auth: FirebaseAuth
    private var mediaData: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddArticleBinding.inflate(layoutInflater)
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

        storage = FirebaseStorage.getInstance().reference.child("image")
        database = FirebaseDatabase.getInstance().reference.child("Hot Spot")
        auth = FirebaseAuth.getInstance()

        binding.btnUploadMedia.setOnClickListener {
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.setType("image/*")
            startActivityForResult(mediaIntent, 1)
        }

        binding.btnUpload.setOnClickListener {
            val mediaName : StorageReference = storage.child("image" + mediaData?.lastPathSegment)
            mediaData?.let { it1 ->
                mediaName.putFile(it1).addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                    mediaName.downloadUrl.addOnSuccessListener { OnSuccessListener<Uri> { uri ->
                        val hashMap : HashMap<String, String> = HashMap()
                        hashMap.put("media", uri.toString())
                        database.setValue(hashMap)
                    } }
                    Toast.makeText(context,"Upload berhasil", Toast.LENGTH_SHORT).show()
                })
            }

            val title = binding.edtTitle.text.toString()
            val description = binding.edtDescription.text.toString()
            val uploader = binding.edtUploader.text.toString()

            if (title.isEmpty() && description.isEmpty() && uploader.isEmpty()) {
                Toast.makeText(context, "Mohon lengkapi data anda", Toast.LENGTH_SHORT).show()
            } else {
                val content = Content(title, description, uploader)
                database.child(title).setValue(content).addOnSuccessListener {
                    Toast.makeText(context, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)

                }.addOnFailureListener{
                    Toast.makeText(context,"Registrasi gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}