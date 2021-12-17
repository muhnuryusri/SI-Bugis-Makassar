package com.example.bugismakassar.admin.fragment.sejarah_bugis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.fragment.adapter.EditArticleAdapter
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.FragmentAdminSejarahBugisBinding
import com.example.bugismakassar.user.fragment.adapter.ArticleAdapter
import com.google.firebase.database.*

class FragmentAdminSejarahBugis : Fragment() {
    private lateinit var binding : FragmentAdminSejarahBugisBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: EditArticleAdapter

    private var listArticle = ArrayList<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminSejarahBugisBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Sejarah Bugis")

        listArticle = arrayListOf<Article>()

        adapter = EditArticleAdapter(listArticle)
        adapter.setData(listArticle)
        binding.rvAdminSejarahBugis.layoutManager = LinearLayoutManager(activity)
        binding.rvAdminSejarahBugis.setHasFixedSize(true)

        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val article = userSnapshot.getValue(Article::class.java)
                        listArticle.add(article!!)
                    }
                    binding.rvAdminSejarahBugis.adapter = ArticleAdapter(listArticle)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_sejarah_bugis_to_nav_add_article)
        }
    }
}