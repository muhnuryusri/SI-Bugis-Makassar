package com.example.bugismakassar.user.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.FragmentSejarahBugisBinding
import com.example.bugismakassar.user.fragment.adapter.ArticleAdapter
import com.google.firebase.database.*

class FragmentSejarahBugis : Fragment() {
    private lateinit var binding : FragmentSejarahBugisBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: ArticleAdapter

    private var listArticle = ArrayList<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSejarahBugisBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Sejarah Bugis")

        listArticle = arrayListOf<Article>()

        adapter = ArticleAdapter(listArticle)
        adapter.setData(listArticle)
        binding.rvSejarahBugis.layoutManager = LinearLayoutManager(activity)
        binding.rvSejarahBugis.setHasFixedSize(true)

        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val article = userSnapshot.getValue(Article::class.java)
                        listArticle.add(article!!)
                    }
                    binding.rvSejarahBugis.adapter = ArticleAdapter(listArticle)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}