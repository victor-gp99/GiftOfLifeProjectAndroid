package com.example.golproject.ui.perfil
import com.firebase.ui.auth.AuthUI
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.golproject.databinding.FragmentNotificationsBinding
import com.example.golproject.ui.login.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val database = Firebase.database
    val user = Firebase.auth.currentUser


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textNotifications
        /*notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        getUser()

        binding.btnCerrar.setOnClickListener {
            signOut()
        }

        val btnNewExp: Button = binding.btnExp
        btnNewExp.setOnClickListener {
            activity?.let {
                val intent = Intent(it, ExpNewActivity::class.java)
                it.startActivityFromFragment(this, intent, 1)
            }
        }

        return root
    }

    private fun signOut() {
        binding.progressBar1.visibility = View.VISIBLE
       binding.btnCerrar.isEnabled = false
        AuthUI.getInstance().signOut(requireActivity()).addOnSuccessListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            Toast.makeText(requireActivity(),"Hasta pronto",Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }.addOnFailureListener {
            binding.btnCerrar.isEnabled = true
            binding.progressBar1.visibility = View.GONE
            Toast.makeText(requireActivity(),"Ocurrio un error ${it.message}",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUser() {
        val myRef = database.getReference("Usuario").child(user?.uid.toString())
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                //val post = dataSnapshot.getValue<Post>()
                dataSnapshot.let {
                    binding.nickView.text = it.child("User").getValue<String>()
                    binding.nombreView.text = it.child("Name").getValue<String>()
                    binding.birthView.text = it.child("Birth").getValue<String>()
                    binding.correoView.text = user?.email
                    Glide.with(requireActivity())
                        .load("https://innovation.com.mx/assets/images/avatar.png")
                        .into(binding.imageView)
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













