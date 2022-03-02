package com.example.golproject.ui.perfil

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.example.golproject.Tools.TimePicker
import com.example.golproject.dao.Experience
import com.example.golproject.databinding.ActivityExpNewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*


class ExpNewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityExpNewBinding
    private var imagen : ByteArray? = null
    private val database = Firebase.database
    val myRef = database.getReference("Experiencia")
    //val myRef2 = database.getReference("Experiencia")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Nueva experiencia")

        binding.editTimePiker.setOnClickListener {
            seleccionarHora()
        }

        binding.fabUploadImg.setOnClickListener {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, 1)
        }
    }

    fun seleccionarHora() {
        val hora = TimePicker{hora, dia -> mostrarResultado(hora, dia)}
        hora.show(supportFragmentManager,"timePicker")
    }

    private fun mostrarResultado(hora: Int, min: Int) {
        val editTime = binding.editTimePiker
        editTime.setText("$hora:$min")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1) {
            data?.let {
                val FileUri = it.data

                saveDatabaseFire(FileUri)

                try {
                    val baos = ByteArrayOutputStream()
                    it.data?.let {
                        val fis = contentResolver.openInputStream(it)

                        fis?.let {
                            val buf = ByteArray(1024)
                            do {
                                val n = it.read(buf)
                                if(n != -1) {
                                    baos.write(buf, 0, n)
                                } else break
                            } while (true)

                            imagen = baos.toByteArray()
                        }
                    }

                    // Actualizar imageView
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, FileUri)
                    binding.imgExp.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun saveDatabaseFire (FileUri : Uri?){

        var guardar = true
        binding.fabAddExp.setOnClickListener {

            // Validar nombre del producto
            if(binding.editNombre.text == null) {
                guardar = false
                binding.editNombre.error = "Debe ingresar el nombre de la experiencia"
            }
            if(binding.editPrecio.text == null) {
                guardar = false
                binding.editPrecio.error = "Debe ingresar el precio de la experiencia"
            }
            if(binding.editDes.text == null) {
                guardar = false
                binding.editDes.error = "Debe ingresar la descricion de la experiencia"
            }

            if(binding.editTimePiker.text == null) {
                guardar = false
                binding.editTimePiker.error = "Debe ingresar la hora de la experiencia"
            }
            if (guardar) {
                try {
                    val Folder: StorageReference = FirebaseStorage.getInstance().reference.child("ExpStore")
                    val file_name: StorageReference = Folder.child("file" + FileUri!!.lastPathSegment)

                    file_name.putFile(FileUri).addOnSuccessListener { taskSnapshot ->
                        file_name.downloadUrl.addOnSuccessListener { uri ->
                            //val hashMap = HashMap<String, String>()
                            //hashMap["link"] = java.lang.String.valueOf(uri)

                            val e = Experience(
                                UUID.randomUUID().toString(),
                                binding.editNombre.text.toString(),
                                binding.editPrecio.text.toString(),
                                binding.spinnerCan.selectedItem.toString(),
                                binding.editDes.text.toString(),
                                imagen.toString(),//bytecode
                                binding.spinerCat.selectedItem.toString(),
                                binding.editLugar.text.toString(),
                                binding.editTimePiker.text.toString(),
                                uri.toString()
                            )
                            myRef.child(e.id.toString()).setValue(e)
                            Log.d("Mensaje", "Se subió correctamente")
                            println("Se subió correctamente FB")
                        }
                    }
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Snackbar.make(binding.imgExp, "Error al guardar en FB", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}