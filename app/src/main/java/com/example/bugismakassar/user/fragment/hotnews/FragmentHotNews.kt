package com.example.bugismakassar.user.fragment.hotnews

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.AdminActivity
import com.example.bugismakassar.admin.fragment.adapter.EditContentAdapter
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.FragmentHotNewsBinding
import com.example.bugismakassar.user.fragment.adapter.ContentAdapter
import com.google.firebase.database.*

class FragmentHotNews : Fragment() {
    private lateinit var binding : FragmentHotNewsBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: EditContentAdapter

    private var listContent = ArrayList<Content>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHotNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Hot News")

        listContent = arrayListOf<Content>()

        adapter = EditContentAdapter(listContent)
        binding.rvHotNews.layoutManager = LinearLayoutManager(activity)
        binding.rvHotNews.setHasFixedSize(true)
        adapter.setOnItemClickCallback(object :
            EditContentAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Content) {
                val intent = Intent(context, ActivityEditHotNews::class.java)
                intent.putExtra("title", data.title)
                startActivity(intent)
            }
        })
        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val content = userSnapshot.getValue(Content::class.java)
                        listContent.add(content!!)
                    }
                    binding.rvHotNews.adapter = EditContentAdapter(listContent)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_hot_news_to_nav_add_content)
        }
    }
}