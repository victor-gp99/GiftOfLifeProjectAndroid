package com.example.golproject.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.golproject.R
import com.example.golproject.adapters.AdapterExp
import com.example.golproject.dao.Experience
import com.example.golproject.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val database = Firebase.database

    private val listExperience:ArrayList<Experience> = ArrayList()
    val myRef = database.getReference("Experiencia")

    override fun onCreateView( inflater: LayoutInflater,  container: ViewGroup?,   savedInstanceState: Bundle? ): View? {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
       /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/


        setupRecyclerView(binding.videogameRecyclerView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.editBuscar.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    if(it.length > 2) {
                       val newListExp : ArrayList<Experience> = ArrayList()
                        listExperience.forEach {
                            if (it.nombre?.toLowerCase()?.contains(p0.toString()) == true){
                                newListExp.add(it)
                            }
                            binding.videogameRecyclerView.adapter = AdapterExp( requireActivity(), R.layout.row_experiencia,newListExp)
                            binding.videogameRecyclerView.layoutManager = GridLayoutManager(requireActivity(),2)

                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }



    private fun setupRecyclerView(expRecyclerView: RecyclerView) {
        listExperience.clear()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
               // val post = dataSnapshot.getValue<Post>()
                dataSnapshot.children.forEach {
                    if(activity!= null){
                    val e:Experience? =
                        Experience(
                            it.child("id").getValue<String>() as String,
                            it.child("nombre").getValue<String>() as String,
                            it.child("precio").getValue<String>() as String,
                            it.child("cantidadMaxPer").getValue<String>() as String,
                            it.child("descripcion").getValue<String>() as String,
                            it.child("imagen").getValue<String>(),
                            it.child("categoria").getValue<String>() as String,
                            it.child("lugar").getValue<String>() as String,
                            it.child("horario").getValue<String>() as String,
                            it.child("url").getValue<String>() as String
                        )

                    e?.let { e ->
                        listExperience.add(e)


                        expRecyclerView.adapter = AdapterExp( requireActivity(), R.layout.row_experiencia,listExperience)
                        expRecyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
                        }
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}