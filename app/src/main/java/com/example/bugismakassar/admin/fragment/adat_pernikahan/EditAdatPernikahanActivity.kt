package com.example.bugismakassar.admin.fragment.adat_pernikahan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import com.example.bugismakassar.R
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.ActivityEditAdatPernikahanBinding

class EditAdatPernikahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdatPernikahanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdatPernikahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editArticle = intent.getParcelableExtra<Article>(EXTRA_ARTICLE)
        if (editArticle != null) {
            showArticleData(editArticle)
        }
    }

    private fun showArticleData(article: Article) {
        val textTitle = findViewById<EditText>(R.id.edt_title)
        val imgMedia = findViewById<ImageView>(R.id.tv_image)
        val textSource = findViewById<EditText>(R.id.edt_source)
        val textDescription = findViewById<EditText>(R.id.edt_description)

        textTitle.setText(article.title)
        com.bumptech.glide.Glide.with(this)
            .load(article.media)
            .into(imgMedia)
        textSource.setText(article.source)
        textDescription.setText(article.description)
    }

    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}