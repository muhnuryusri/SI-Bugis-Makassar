package com.example.bugismakassar.admin.fragment.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.ListItemArticleEditBinding
import com.example.bugismakassar.databinding.ListItemArticleEditWithVideoBinding
import com.example.bugismakassar.databinding.ListItemContentEditBinding
import com.example.bugismakassar.databinding.ListItemContentEditWithVideoBinding
import com.example.bugismakassar.user.MainActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class EditContentAdapter (private val listContent: ArrayList<Content>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val TYPE_IMAGE: Int = 0
    private val TYPE_VIDEO: Int = 1
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Content)
    }
    fun setData(data: List<Content>) {
        listContent.clear()
        listContent.addAll(data)
        this.notifyItemChanged(0)
    }

    private inner class ImageViewHolder(private val binding: ListItemContentEditBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                val content = listContent[position]
                tvTitle.text = content.title
                com.bumptech.glide.Glide.with(itemView.context)
                    .load(content.media)
                    .centerCrop()
                    .apply(RequestOptions().override(520, 300))
                    .into(tvImage)
                tvUploader.text = content.uploader
                tvDescription.text = content.description
                editContent.setOnClickListener {
                    onItemClickCallback?.onItemClicked(listContent[position])
                }
            }
        }
    }

    private inner class VideoViewHolder(private val binding: ListItemContentEditWithVideoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                val content = listContent[position]
                tvTitle.text = content.title

                val exoPlayer: ExoPlayer = ExoPlayer.Builder(itemView.context).build()
                binding.tvVideo.player = exoPlayer

                val mediaItem = createMediaItem(content)

                if (mediaItem != null) {
                    exoPlayer.addMediaItem(mediaItem)
                }

                exoPlayer.prepare()
                exoPlayer.play()
                tvUploader.text = content.uploader
                tvDescription.text = content.description
            }
        }

        private fun createMediaItem(content: Content): MediaItem? {
            val mediaUri = content.media
            return mediaUri?.let { MediaItem.fromUri(it) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_IMAGE) {
            val binding = ListItemContentEditBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            return ImageViewHolder(binding)
        }
        val binding = ListItemContentEditWithVideoBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listContent[position].type === TYPE_IMAGE) {
            (holder as ImageViewHolder).bind(position)
        } else {
            (holder as VideoViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int = listContent.size

    override fun getItemViewType(position: Int): Int {
        return if (listContent[position].type == 0) {
            TYPE_IMAGE
        } else {
            TYPE_VIDEO
        }
    }
}