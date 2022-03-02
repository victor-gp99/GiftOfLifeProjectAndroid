package com.example.golproject.ui.carrito

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.golproject.R
import com.example.golproject.adapters.AdapterCar
import com.example.golproject.dao.Experience
import com.example.golproject.databinding.FragmentDashboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment() {
    private val listCarExp:ArrayList<Experience> = ArrayList()
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: DashboardViewModel
    private val database = Firebase.database
    val myRefUser = database.getReference("Usuario")
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupRecyclerView(binding.carRecyclerView)
        //inicializando viewmodel
        viewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)

        binding.button.setOnClickListener {
            if (!listCarExp.isEmpty()) {
                dialog()
                deleteAll()
            }else
                Toast.makeText(requireActivity(), "ERROR Agrega experiencias al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialog() {
        MaterialAlertDialogBuilder (requireContext())
            .setTitle("Comprado")
            .setMessage("""
                             Gracias por tu compra!
                            """.trimMargin().trim())
            .setPositiveButton("Ok"){ dialog, which ->
                findNavController().navigate(R.id.navigation_home)
            }
            .show()
    }

    private fun setupRecyclerView(expRecyclerView: RecyclerView) {
        listCarExp.clear()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                // val post = dataSnapshot.getValue<Post>()

                val user: FirebaseUser?= auth.currentUser
                val userID = myRefUser.child(user?.uid!!)

                dataSnapshot.child(userID.key.toString()).child("Carrito").children.forEach {
                   if (activity != null){
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
                           listCarExp.add(e)
                           expRecyclerView.adapter = AdapterCar(requireActivity(),R.layout.row_carrito ,listCarExp, viewModel)
                           expRecyclerView.layoutManager = GridLayoutManager(requireActivity(),1)


                       }


                       //observando el dato de interes
                       /*viewModel.data.observe(viewLifecycleOwner, Observer { data ->
                           binding.totalView.text = data.toString()
                       })*/

                   }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRefUser.addValueEventListener(postListener)
        deleteSwipe(expRecyclerView)
    }

    private fun deleteSwipe(recyclerView: RecyclerView) {
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                /*listCarExp.get(viewHolder.adapterPosition).id?.let {
                    myRef.child(it).setValue(null)
                    }*/
                val user: FirebaseUser?= auth.currentUser
                val userID = myRefUser.child(user?.uid!!)
                listCarExp.get(viewHolder.adapterPosition).id.let {
                    myRefUser.child(userID.key.toString()).child("Carrito").child(it!!).setValue(null)
                }

                listCarExp.removeAt(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun deleteAll(){
        val user: FirebaseUser?= auth.currentUser
        val userID = myRefUser.child(user?.uid!!)
        myRefUser.child(userID.key.toString()).child("Carrito").setValue(null)
        listCarExp.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    }


