package com.example.golproject.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.golproject.R
import com.example.golproject.dao.Experience
import com.example.golproject.ui.carrito.DashboardViewModel
import com.example.golproject.ui.home.ExperienceDetailActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.nio.charset.Charset

class AdapterExp (var c: Context, private val layout: Int, private var expList: ArrayList<Experience> ):
    RecyclerView.Adapter<AdapterExp.ExpViewHolder>(){

    inner class  ExpViewHolder(v: View): RecyclerView.ViewHolder(v) {

        private val imgView = itemView.findViewById<ImageView>(R.id.imgRowProducto)
        private val nombreView = itemView.findViewById<TextView>(R.id.textRowProducto)
        private val precioView = itemView.findViewById<TextView>(R.id.textRowPrecio)

        @SuppressLint("SetTextI18n")
        fun  bind(e : Experience){

           /* val img= e.imagen?.toByteArray(Charset.defaultCharset())//convirtiendo string a bytearray
            img?.let {
                val bmp = BitmapFactory.decodeByteArray(it,0,it.size)
                imgView.setImageBitmap(bmp)
            }*/
            Glide.with(c).load(e.url).into(imgView)
            nombreView.text= e.nombre
            precioView.text= "Precio: ${e.precio}"

            itemView.setOnClickListener {
                val intent = Intent(c, ExperienceDetailActivity::class.java)
                intent.putExtra("key", e.id)
                c.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpViewHolder {
        val view = LayoutInflater.from(c).inflate(layout, null)
        return ExpViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpViewHolder, position: Int) {
        holder.bind(expList[position])
    }

    override fun getItemCount(): Int {
        return  expList.size
    }

}