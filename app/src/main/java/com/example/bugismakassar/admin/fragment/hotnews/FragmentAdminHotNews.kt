package com.example.bugismakassar.admin.fragment.hotnews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.fragment.adapter.EditContentAdapter
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.FragmentAdminHotNewsBinding
import com.google.firebase.database.*

class FragmentAdminHotNews : Fragment() {
    private lateinit var binding : FragmentAdminHotNewsBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: EditContentAdapter

    private var listContent = ArrayList<Content>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminHotNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Hot News")

        listContent = arrayListOf<Content>()

        adapter = EditContentAdapter(listContent)
        binding.rvAdminHotNews.layoutManager = LinearLayoutManager(activity)
        binding.rvAdminHotNews.setHasFixedSize(true)

        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val content = userSnapshot.getValue(Content::class.java)
                        listContent.add(content!!)
                    }
                    binding.rvAdminHotNews.adapter = EditContentAdapter(listContent)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_hot_news_to_nav_add_content)
        }
    }
}