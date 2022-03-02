package com.example.golproject.dao

data class Usuario(
    val id : String?=null,
    val nickname : String?=null,
    val correo: String?=null,
    val nombre : String?=null,
    val cumple: String?=null,
    val carrito : HashMap<String, Boolean>?=null
//                         idexperiencia, true
)
