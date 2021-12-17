package com.example.bugismakassar.user.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bugismakassar.R
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.ListItemArticleBinding
import com.example.bugismakassar.databinding.ListItemArticleWithVideoBinding
import com.example.bugismakassar.databinding.ListItemContentBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

class ArticleAdapter (private val listArticle: ArrayList<Article>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val TYPE_IMAGE: Int = 0
    private val TYPE_VIDEO: Int = 1

    fun setData(data: List<Article>) {
        listArticle.clear()
        listArticle.addAll(data)
        this.notifyItemChanged(0)
    }

    private inner class ImageViewHolder(private val binding: ListItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                val article = listArticle[position]
                tvTitle.text = article.title
                com.bumptech.glide.Glide.with(itemView.context)
                    .load(article.media)
                    .centerCrop()
                    .apply(RequestOptions().override(520, 300))
                    .into(tvImage)
                tvSource.text = article.source
                tvDescription.text = article.description
            }
        }
    }

    private inner class VideoViewHolder(private val binding: ListItemArticleWithVideoBinding): RecyclerView.ViewHolder(binding.root) {
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
            }
        }

        private fun createMediaItem(article: Article): MediaItem? {
            val mediaUri = article.media
            return mediaUri?.let { MediaItem.fromUri(it) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_IMAGE) {
            val binding = ListItemArticleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            return ImageViewHolder(binding)
        }
        val binding = ListItemArticleWithVideoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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
}