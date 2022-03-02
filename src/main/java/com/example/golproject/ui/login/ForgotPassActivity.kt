package com.example.golproject.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.golproject.databinding.ActivityForgotPassBinding
import com.example.golproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPassBinding
    private  lateinit var progressbar: ProgressBar
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        progressbar = binding.progressBar2

        auth= FirebaseAuth.getInstance()
        binding.sendPass.setOnClickListener {
            send()
        }
    }
    private fun send (){
        val email = binding.txtEmailF.text.toString()

        if (!TextUtils.isEmpty(email)){
            progressbar.visibility = View.VISIBLE
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    task->
                    if (task.isSuccessful)
                        finish()
                    else
                        Toast.makeText(this, "Error al enviar el email de recupereción", Toast.LENGTH_SHORT).show()
                }

        }
    }

}