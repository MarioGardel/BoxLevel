package com.example.boxlevel.data

// Data class de Días, que tendrá un identificador, el día y una lista de las horas registradas
data class Dias(val id: Long, val date: String, val horas: List<Horas>)