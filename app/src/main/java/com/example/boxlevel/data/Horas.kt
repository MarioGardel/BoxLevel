package com.example.boxlevel.data

// Data class de Horas, que tendrá un identificador, la hora, el entrenador y si está o no reservada esa hora
data class Horas(val id: Long, val time: String, val entrenador: String, var reservado: Boolean)