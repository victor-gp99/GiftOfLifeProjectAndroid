package com.example.golproject.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.golproject.Tools.DatePickerFragment
import com.example.golproject.dao.Usuario
import com.example.golproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private  lateinit var  dbref : DatabaseReference
    private  lateinit var  db : FirebaseDatabase
    private  lateinit var progressbar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        progressbar = binding.progressBar
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbref = db.reference.child("Usuario")

        binding.editDateBirth.setOnClickListener {
            showDatePickerDialog()
        }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }
    private fun onDateSelected (day:Int, month : Int, year : Int){
        binding.editDateBirth.setText("$day/$month/$year")
    }

    //se agrego el onclick en el layout como atributo del boton
    fun registerFireBase(view:View){
        createNewAccount()
    }

    private  fun createNewAccount(){
        val username = binding.editUser.text.toString()
        val email = binding.editEmail.text.toString()
        val password = binding.editPass.text.toString()
        val name = binding.editName.text.toString()
        val birth = binding.editDateBirth.text.toString()


        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) &&!TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(birth) ){
            progressbar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener (this) {
                    task ->
                    if (task.isComplete){
                        val user: FirebaseUser ?= auth.currentUser
                        val u = Usuario(
                            user?.uid!!,
                            username,
                            email,
                            name,
                            birth,
                            HashMap()
                        )
                        verifyEmail(user)
                        val userBD = dbref.child(u.id.toString())
                        userBD.child("User").setValue(u.nickname)
                        userBD.child("Name").setValue(u.nombre)
                        userBD.child("Email").setValue(u.correo)
                        userBD.child("Birth").setValue(u.cumple)
                        finish()
                    }else
                        Toast.makeText(this, "Error al registrarte", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun verifyEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task ->
                if (task.isComplete){
                    Toast.makeText(this, "Correo de verificacion enviado", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error al enviar el Correo de verificacion", Toast.LENGTH_SHORT).show()
                }
            }

    }

}
