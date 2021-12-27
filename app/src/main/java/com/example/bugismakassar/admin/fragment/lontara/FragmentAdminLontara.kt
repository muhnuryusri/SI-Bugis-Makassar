package com.example.bugismakassar.admin.fragment.lontara

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.R
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.FragmentAdminLontaraBinding
import com.google.firebase.database.*

class FragmentAdminLontara : Fragment() {
    private lateinit var binding : FragmentAdminLontaraBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: EditLontaraAdapter

    private var listArticle = ArrayList<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminLontaraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Lontara")

        listArticle = arrayListOf<Article>()

        adapter = context?.let { EditLontaraAdapter(it, listArticle) }!!
        adapter.setData(listArticle)
        adapter.notifyDataSetChanged()
        binding.rvAdminLontara.layoutManager = LinearLayoutManager(activity)
        binding.rvAdminLontara.setHasFixedSize(true)

        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val article = userSnapshot.getValue(Article::class.java)
                        listArticle.add(article!!)
                    }
                    binding.rvAdminLontara.adapter = EditLontaraAdapter(context, listArticle)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_lontara_to_nav_add_article)
        }
    }
}