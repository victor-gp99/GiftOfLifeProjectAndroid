package com.example.golproject.dao

import java.io.Serializable

data class Experience(
    val id: String?=null,
    val nombre: String?=null,
    val precio: String?=null,
    val cantidadMaxPer: String?=null,
    val descripcion: String?=null,
    val imagen: String?=null ,
    val categoria: String?=null,
    val lugar: String?=null,
    val horario: String?=null,
    val url: String?=null
){

}
//val imagen : ByteArray?