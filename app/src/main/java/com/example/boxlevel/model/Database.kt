package com.example.boxlevel.model

import com.example.boxlevel.data.Dias
import com.example.boxlevel.data.Horas

// Singleton que va a simular una base de datos
object Database {

    // Lista con los días y horas registrados
    private val dias: List<Dias> = listOf(

        Dias(1,"24/11/2021", listOf(
            Horas(1,"13:00", "Jaime", false),
            Horas(2,"17:30", "Lucas", false),
            Horas(3,"20:00", "Carlos", true),
        )),

        Dias(2,"25/11/2021", listOf(
            Horas(4,"11:00", "Jaime", true),
            Horas(5,"16:30", "Lucas", false),
            Horas(6,"21:00", "Carlos", true)
        )),

        Dias(3,"26/11/2021", listOf(
            Horas(7,"9:30", "Jaime", true),
            Horas(8,"18:30", "Lucas", false),
            Horas(9,"20:30", "Carlos", false)
        )),

        Dias(4,"29/11/2021", listOf(
            Horas(10,"10:30", "Jaime", true),
            Horas(11,"16:00", "Lucas", true),
            Horas(12,"21:30", "Carlos", true)
        )),

        Dias(5,"30/11/2021", listOf(
            Horas(13,"12:30", "Jaime", false),
            Horas(14,"17:00", "Lucas", false),
            Horas(15,"20:00", "Carlos", true)
        )),

        Dias(6,"01/12/2021", listOf(
            Horas(16,"10:30", "Jaime", false),
            Horas(17,"17:30", "Lucas", false),
            Horas(18,"21:00", "Carlos", false)
        )),

        Dias(7,"02/12/2021", listOf(
            Horas(19,"10:00", "Jaime", false),
            Horas(20,"16:30", "Lucas", true),
            Horas(21,"20:30", "Carlos", true)
        )),

        Dias(8,"03/12/2021", listOf(
            Horas(22,"12:30", "Jaime", true),
            Horas(23,"21:30", "Carlos", false)
        ))
    )

    // Este método te devuelte toda la lista
    fun queryAll(): List<Dias> {
        return dias
    }

    // Este método te devuelve las horas de un día
    fun queryHorasFromDia(dia: String): List<Horas> {
        var listas = emptyList<Horas>()
        for(d in dias){
            if(d.date == dia){
                listas = d.horas
            }
        }
        return listas
    }

    // Este método te devuelve una hora dada un identificador de hora
    fun getHoraById(id: Long): Horas {
        for (dia in dias){
            for(hora in dia.horas){
                if(hora.id == id)
                    return hora
            }
        }
        return dias[0].horas[0]
    }
}
