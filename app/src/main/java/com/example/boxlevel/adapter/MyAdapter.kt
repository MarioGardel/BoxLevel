package com.example.boxlevel.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boxlevel.data.Horas
import com.example.boxlevel.databinding.ItemsBinding
import com.example.boxlevel.interfaces.OnItemClickListener

// Esta clase será el adaptador del recycler view en CalendarioActivity. Esta clase extiende de la Clase RecyclerView.Adapter
class MyAdapter: RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var data: List<Horas> = emptyList() // Almacena las horas registradas
    private var onItemClickListener: OnItemClickListener? = null // Creamos la variable que controla el hacer click en un item de la lista


    // Este método se encargará de hacer lo que se le pase por parámetro al hacer click en un item de la lista
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    // Este método me devuelve una hora dada su posición
    fun getItem(position: Int): Horas {
        return data[position]
    }

    // Este método se encarga de crear el View Holder del adaptador
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context) // Se obtiene el layoutInflater del padre de items layout
        val binding = ItemsBinding.inflate(layoutInflater, parent, false) // inflamos el binding
        return ViewHolder(binding) // Creamos el ViewHolder
    }

    // Este método encaja cada elemento de la lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    // Este método obtiene el tamaño de la lista
    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    // Este método se encarga de actualizar la lista
    fun submitList(newData: List<Horas>){
        data = newData
        notifyDataSetChanged() // Notifica ese cambio
    }

    // Es una clase interna que se va a encargar de manejar el inflado de la lista
    inner class ViewHolder(private val binding: ItemsBinding): RecyclerView.ViewHolder(binding.root) {
        // Método inicial
        init {
            // Cada vez que se hace click en un item de la lista, se obtiene la posición y se le aplica el método de OnItemClicklistener
            itemView.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    onItemClickListener?.onItemClick(position)
                }
            }
        }

        // Este método se encarga de posicionar y dar valor a los elementos de un item de la lista
        fun bind(dia: Horas){
            binding.time.text = dia.time
            binding.entrenador.text = dia.entrenador
            binding.view.setBackgroundColor(
                if(dia.reservado){
                    Color.parseColor("#ff7961")
                } else {
                    Color.parseColor("#62efff")
                }
            )
        }
    }
}