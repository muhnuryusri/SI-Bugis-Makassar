package com.example.bugismakassar.admin.fragment.hotspot

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.bugismakassar.R
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.ListItemContentEditBinding
import com.example.bugismakassar.databinding.ListItemContentEditWithVideoBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditHotSpotAdapter (val context: Context, private val listContent: ArrayList<Content>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private lateinit var database: DatabaseReference
    private val TYPE_IMAGE: Int = 0
    private val TYPE_VIDEO: Int = 1

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
                    .into(tvImage)
                tvDescription.text = content.description
                tvUploader.text = content.uploader
                editContent.setOnClickListener {
                    showUpdateDialog(content)
                }
                deleteContent.setOnClickListener {
                    showDeleteDialog(content)
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
                tvDescription.text = content.description
                tvUploader.text = content.uploader
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

    fun showUpdateDialog(content: Content) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Update")

        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.update_dialog_content, null)

        val textTitle = view.findViewById<EditText>(R.id.edt_title)
        val imgMedia = view.findViewById<ImageView>(R.id.tv_image)
        val textDescription = view.findViewById<EditText>(R.id.edt_description)
        val textUploader = view.findViewById<EditText>(R.id.edt_uploader)

        textTitle.setText(content.title)
        com.bumptech.glide.Glide.with(view.context)
            .load(content.media)
            .into(imgMedia)
        textUploader.setText(content.uploader)
        textDescription.setText(content.description)

        builder.setView(view)

        builder.setPositiveButton("Update") { dialog, which ->

            database = FirebaseDatabase.getInstance().reference.child("Hot Spot")

            val title = textTitle.text.toString().trim()
            val description = textDescription.text.toString().trim()
            val uploader = textUploader.text.toString().trim()

            val content = Content(content.id, title, content.media, description, uploader, 0)

            content.id?.let {
                database.child(it).setValue(content).addOnCompleteListener {
                    Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
                }
            }

            setData(listContent)

        }

        builder.setNegativeButton("Batal") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()

    }

    fun showDeleteDialog(content: Content) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Hapus Konten")
        builder.setMessage("Anda yakin ingin menghapus konten ini?")

        database = FirebaseDatabase.getInstance().reference.child("Hot Spot")

        builder.setPositiveButton("Ya") { dialog, which ->
            content.id?.let { database.child(it).removeValue() }
            setData(listContent)
        }

        builder.setNegativeButton("Tidak") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()
    }
}