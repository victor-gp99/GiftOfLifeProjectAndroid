package com.example.golproject.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.golproject.ui.PrincipalActivity
import com.example.golproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  lateinit var progressbar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ocultar el action bar
        supportActionBar?.hide();

        progressbar = binding.progressBar1
        auth = FirebaseAuth.getInstance()

        binding.logBtn.setOnClickListener {
            loginUser()
        }
        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.fogotPass.setOnClickListener {
            val intent = Intent(this, ForgotPassActivity::class.java)
            startActivity(intent)
        }

    }



    private fun loginUser(){
        val user = binding.editUser.text.toString()
        val pass = binding.editContra.text.toString()

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass) ){
            progressbar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(user,pass)
                .addOnCompleteListener (this) {
                    task ->
                    if (task.isSuccessful){
                        val intent =Intent(this, PrincipalActivity::class.java)
                        intent.putExtra("sesion",user)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Error en la autenticación", Toast.LENGTH_SHORT).show()
                    }
                }

        }


    }
}