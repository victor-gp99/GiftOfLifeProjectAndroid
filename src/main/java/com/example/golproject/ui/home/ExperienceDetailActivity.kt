package com.example.golproject.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.golproject.dao.Experience
import com.example.golproject.databinding.ActivityExperienceDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ExperienceDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExperienceDetailBinding
    private  lateinit var  myRefUser : DatabaseReference
    private  lateinit var  db : FirebaseDatabase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExperienceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        db = FirebaseDatabase.getInstance()
        myRefUser = db.reference.child("Usuario")
        auth = FirebaseAuth.getInstance()

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

        val key = intent.getStringExtra("key")
        val boton = intent.getBooleanExtra("boton", true)

        val database = Firebase.database
        val myRefExp = key?.let {
                database.getReference("Experiencia").child(it)
            }


        if (!boton){
            binding.addCarBtn.isEnabled = false
            binding.addCarBtn.setBackgroundColor(Color.GRAY)
        }else{
            binding.addCarBtn.isEnabled = true
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        myRefExp?.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val e:Experience? = dataSnapshot.getValue(Experience::class.java)
                if (e != null) {
                    binding.nameTextView.text = e.nombre
                    binding.descriptionTextView.text = e.descripcion
                    binding.viewPrice.text = " Price: \$${e.precio}"
                    binding.personasView.text = "Personas Max: ${e.cantidadMaxPer}"
                    binding.lugarView.text = "Lugar: ${e.lugar}"
                    binding.horarioView.text = "Horario: ${e.horario}"
                    images(e.url.toString())

                    binding.addCarBtn.setOnClickListener {
                        val user: FirebaseUser?= auth.currentUser
                        val userID = myRefUser.child(user?.uid!!)
                        userID.child("Carrito").child(e.id.toString()).setValue(e)
                        Toast.makeText(this@ExperienceDetailActivity, "Agregado correctamente al carrito", Toast.LENGTH_SHORT).show()
                        finish()
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })


    }

    private  fun images(url: String){
        Glide.with(this)
            .load(url)
            .into(binding.posterImgeView)

        Glide.with(this)
            .load(url)
            .into(binding.backgroundImageView)
    }
}