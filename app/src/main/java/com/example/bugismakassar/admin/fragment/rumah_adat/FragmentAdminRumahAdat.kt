package com.example.bugismakassar.admin.fragment.rumah_adat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.R
import com.example.bugismakassar.data.Article
import com.example.bugismakassar.databinding.FragmentAdminRumahAdatBinding
import com.google.firebase.database.*

class FragmentAdminRumahAdat : Fragment() {
    private lateinit var binding : FragmentAdminRumahAdatBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: EditRumahAdatAdapter

    private var listArticle = ArrayList<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminRumahAdatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Rumah Adat")

        listArticle = arrayListOf<Article>()

        adapter = context?.let { EditRumahAdatAdapter(it, listArticle) }!!
        adapter.setData(listArticle)
        binding.rvAdminRumahAdat.layoutManager = LinearLayoutManager(activity)
        binding.rvAdminRumahAdat.setHasFixedSize(true)

        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val article = userSnapshot.getValue(Article::class.java)
                        listArticle.add(article!!)
                    }
                    binding.rvAdminRumahAdat.adapter = EditRumahAdatAdapter(context, listArticle)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_admin_rumah_adat_to_nav_add_article)
        }
    }
}