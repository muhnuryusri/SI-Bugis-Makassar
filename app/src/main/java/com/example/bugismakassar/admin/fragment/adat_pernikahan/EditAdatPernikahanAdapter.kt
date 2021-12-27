package com.example.bugismakassar.admin.fragment.adat_pernikahan

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.ListItemArticleEditBinding
import com.example.bugismakassar.databinding.ListItemArticleEditWithVideoBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.core.content.ContextCompat.startActivity

class EditAdatPernikahanAdapter (val context: Context?, private val listArticle: ArrayList<Article>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private lateinit var database: DatabaseReference
    private val TYPE_IMAGE: Int = 0
    private val TYPE_VIDEO: Int = 1

    fun setData(data: List<Article>) {
        listArticle.clear()
        listArticle.addAll(data)
        this.notifyItemChanged(0)
    }

    private inner class ImageViewHolder(private val binding: ListItemArticleEditBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                val article = listArticle[position]
                tvTitle.text = article.title
                com.bumptech.glide.Glide.with(itemView.context)
                    .load(article.media)
                    .into(tvImage)
                tvSource.text = article.source
                tvDescription.text = article.description
                deleteArticle.setOnClickListener {
                    showDeleteDialog(article)
                }
            }
        }
        init {
            binding.editArticle.setOnClickListener {
                val intent = Intent(context, EditAdatPernikahanActivity::class.java)
                intent.putExtra(EditAdatPernikahanActivity.EXTRA_ARTICLE, listArticle.get(position))
                context?.startActivity(intent)
            }
        }
    }

    private inner class VideoViewHolder(private val binding: ListItemArticleEditWithVideoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                val article = listArticle[position]
                tvTitle.text = article.title

                val exoPlayer: ExoPlayer = ExoPlayer.Builder(itemView.context).build()
                binding.tvVideo.player = exoPlayer

                val mediaItem = createMediaItem(article)

                if (mediaItem != null) {
                    exoPlayer.addMediaItem(mediaItem)
                }

                exoPlayer.prepare()
                exoPlayer.play()
                tvSource.text = article.source
                tvDescription.text = article.description
                deleteArticle.setOnClickListener {
                    showDeleteDialog(article)
                }
            }
        }
        init {
            binding.editArticle.setOnClickListener {
                val intent = Intent(context, EditAdatPernikahanActivity::class.java)
                intent.putExtra(EditAdatPernikahanActivity.EXTRA_ARTICLE, listArticle.get(position))
                context?.startActivity(intent)
            }
        }

        private fun createMediaItem(article: Article): MediaItem? {
            val mediaUri = article.media
            return mediaUri?.let { MediaItem.fromUri(it) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_IMAGE) {
            val binding = ListItemArticleEditBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            return ImageViewHolder(binding)
        }
        val binding = ListItemArticleEditWithVideoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listArticle[position].type === TYPE_IMAGE) {
            (holder as ImageViewHolder).bind(position)
        } else {
            (holder as VideoViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int = listArticle.size

    override fun getItemViewType(position: Int): Int {
        return if (listArticle[position].type == 0) {
            TYPE_IMAGE
        } else {
            TYPE_VIDEO
        }
    }

    fun showDeleteDialog(article: Article) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Hapus Artikel")
        builder.setMessage("Anda yakin ingin menghapus artikel ini?")

        database = FirebaseDatabase.getInstance().reference.child("Adat Pernikahan")

        builder.setPositiveButton("Ya") { dialog, which ->
            article.id?.let { database.child(it).removeValue() }
            setData(listArticle)
        }

        builder.setNegativeButton("Tidak") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()
    }
}