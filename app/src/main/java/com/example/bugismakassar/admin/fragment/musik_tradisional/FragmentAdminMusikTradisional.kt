package com.example.bugismakassar.admin.fragment.musik_tradisional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.R
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.FragmentAdminMusikTradisionalBinding
import com.google.firebase.database.*

class FragmentAdminMusikTradisional : Fragment() {
    private lateinit var binding : FragmentAdminMusikTradisionalBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: EditMusikTradisionalAdapter

    private var listArticle = ArrayList<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminMusikTradisionalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Musik Tradisional")

        listArticle = arrayListOf<Article>()

        adapter = context?.let { EditMusikTradisionalAdapter(it, listArticle) }!!
        adapter.setData(listArticle)
        binding.rvAdminMusikTradisional.layoutManager = LinearLayoutManager(activity)
        binding.rvAdminMusikTradisional.setHasFixedSize(true)

        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val article = userSnapshot.getValue(Article::class.java)
                        listArticle.add(article!!)
                    }
                    binding.rvAdminMusikTradisional.adapter = EditMusikTradisionalAdapter(context!!, listArticle)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_musik_tradisional_to_nav_add_article)
        }
    }
}