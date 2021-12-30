package com.example.bugismakassar.user.fragment.hotspot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugismakassar.R
import com.example.bugismakassar.admin.fragment.hotspot.EditHotSpotAdapter
import com.example.bugismakassar.data.Content
import com.example.bugismakassar.databinding.FragmentHotSpotBinding
import com.example.bugismakassar.user.fragment.adapter.ContentAdapter
import com.google.firebase.database.*

class FragmentHotSpot : Fragment() {
    private lateinit var binding : FragmentHotSpotBinding
    private lateinit var database : DatabaseReference
    private lateinit var adapter: EditHotSpotAdapterUser

    private var listContent = ArrayList<Content>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHotSpotBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Hot Spot")

        listContent = arrayListOf<Content>()

        adapter = context?.let { EditHotSpotAdapterUser(it, listContent) } !!
        binding.rvHotSpot.layoutManager = LinearLayoutManager(activity)
        binding.rvHotSpot.setHasFixedSize(true)

        binding.progressBar.visibility = View.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBar.visibility = View.GONE
                    for (userSnapshot in snapshot.children){
                        val content = userSnapshot.getValue(Content::class.java)
                        listContent.add(content!!)
                    }
                    binding.rvHotSpot.adapter = EditHotSpotAdapterUser(context, listContent)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_nav_hot_spot_to_nav_add_content)
        }
    }
}