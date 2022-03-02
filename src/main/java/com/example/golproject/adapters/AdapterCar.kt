package com.example.golproject.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.String

 class AdapterCar(
     var c: Context,
     private val layout: Int,
     private var expList: ArrayList<Experience>,
     val viewModel: DashboardViewModel
 ):
    RecyclerView.Adapter<AdapterCar.ExpViewHolder>(){

    inner class  ExpViewHolder(v: View): RecyclerView.ViewHolder(v) {

        private val imgView = itemView.findViewById<ImageView>(R.id.imgRowCarrito)
        private val nombreView = itemView.findViewById<TextView>(R.id.textRowProductoC)
        private val precioView = itemView.findViewById<TextView>(R.id.textRowPrecioC)
        private val cantidadView = itemView.findViewById<TextView>(R.id.cantidadView)
        private val fabMenos = itemView.findViewById<FloatingActionButton>(R.id.fabSubCarrito)
        private val faboPlus = itemView.findViewById<FloatingActionButton>(R.id.fabPlusCarrito)



        @SuppressLint("SetTextI18n")
        fun  bind(e : Experience){
            val cantidadP = e.cantidadMaxPer.toString().toInt()
            val precioP = e.precio.toString().toInt()
            var valor=1

           /* val img= e.imagen?.toByteArray(Charset.defaultCharset())//convirtiendo string a bytearray
            img?.let {
                val bmp = BitmapFactory.decodeByteArray(it,0,it.size)
                imgView.setImageBitmap(bmp)
            }*/

            Glide.with(c).load(e.url).into(imgView)
            nombreView.text = e.nombre
            precioView.text = "Precio:$${e.precio}"
            cantidadView.text = valor.toString()


            itemView.setOnClickListener {
                val intent = Intent(c, ExperienceDetailActivity::class.java)
                val habilitar = false
                intent.putExtra("key", e.id)
                intent.putExtra("boton", habilitar)
                c.startActivity(intent)
            }

            faboPlus.setOnClickListener(View.OnClickListener {
                String.valueOf(++valor).also {
                    cantidadView.text = it

                    if (valor>=cantidadP) {
                        valor = cantidadP
                        cantidadView.text=valor.toString()
                    }
                    precioView.text = "Precio: ${(valor*precioP)}"
                }


            })
            fabMenos.setOnClickListener(View.OnClickListener {
                String.valueOf(--valor).also {
                    cantidadView.text = it
                    if (valor<=1) {
                        valor = 1
                        cantidadView.text=valor.toString()
                    }
                    precioView.text = "Precio:$${(valor*precioP)}"
                }

            })

        totalizar()

        }

       fun totalizar() {

            var total=0
            for (ex in expList) {
                total += ex.precio.toString().toInt()
            }

           String.valueOf(total).also {
               viewModel.setData(it.toInt())
           }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpViewHolder {
        val view = LayoutInflater.from(c).inflate(layout, null)
        return ExpViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpViewHolder, position: Int) {

        return holder.bind(expList[position])
    }

    override fun getItemCount(): Int {
        return  expList.size
    }


}