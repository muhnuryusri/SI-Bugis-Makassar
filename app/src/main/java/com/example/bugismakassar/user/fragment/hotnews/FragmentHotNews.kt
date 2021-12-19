package com.example.bugismakassar.user.fragment.hotnews

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.fragment.hotnews.EditHotNewsAdapter
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.FragmentHotNewsBinding
import com.google.firebase.database.*

class FragmentHotNews : Fragment() {
    private lateinit var binding : FragmentHotNewsBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: EditHotNewsAdapter

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

        adapter = context?.let { EditHotNewsAdapter(it, listContent) } !!
        binding.rvHotNews.layoutManager = LinearLayoutManager(activity)
        binding.rvHotNews.setHasFixedSize(true)
        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val content = userSnapshot.getValue(Content::class.java)
                        listContent.add(content!!)
                    }
                    binding.rvHotNews.adapter = EditHotNewsAdapter(context!!, listContent)
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