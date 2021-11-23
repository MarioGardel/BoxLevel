package com.example.boxlevel.vistas

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boxlevel.MainActivity
import com.example.boxlevel.R
import com.example.boxlevel.ReservarActivity
import com.example.boxlevel.adapter.MyAdapter
import com.example.boxlevel.data.Horas
import com.example.boxlevel.databinding.CalendarioActivityBinding
import com.example.boxlevel.interfaces.OnItemClickListener
import com.example.boxlevel.model.Database
import java.text.SimpleDateFormat
import java.util.*

class CalendarioActivity : AppCompatActivity() {

    private var dia: String = "" // Esta variable almacena el día que clique el usuario en el calendario
    private lateinit var binding: CalendarioActivityBinding
    // Esta variable se encargará de controlar la navegación a ReservarActivity y su regreso a esta actividad
    private val reservarActivityCall = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode == RESULT_OK && result.data != null){
            showHours() // Actualiza la lista
        }
    }
    // Adaptador del recycler view
    private val listAdapter: MyAdapter = MyAdapter().apply {
        // Al hacer click llama a un objeto singleton de tipo OnItemClickListener
        setOnItemClickListener(object : OnItemClickListener {
            // Sobreescribimos el método onItemClick
            override fun onItemClick(position: Int) {
                val horas = getItem(position) // Al hacer click obtenemos el elemento de la lista donde hemos hecho click
                // Comprobamos si no esta reservada esa hora
                if(!horas.reservado){
                    // Si no lo está, navega a ReservarActivity
                        reservarActivityCall.launch(
                            ReservarActivity.newIntent(
                        this@CalendarioActivity, horas.id))
                } else {
                    // Si lo está muestra un mensaje informativo
                    Toast.makeText(this@CalendarioActivity, getString(R.string.reservado), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private lateinit var listHours: List<Horas> // almacena las horas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarioActivityBinding.inflate(layoutInflater) // Crea el binding
        setContentView(binding.root)
        setupViews() // Configura las vistas
    }

    // Este método crea el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Este método controla lo que se va a hacer según en el item que clique
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sobreNosotros -> navegarAWebOficial() // Si hace click en "Sobre nosotros" navega a la web oficial
            R.id.ayuda -> navegarACorreo() // Si hace click en "Ayuda" navega al correo electrónico
            R.id.cerrarSesion -> cerrarSesion() // Vuelve a la pantalla principal
        }
        return super.onOptionsItemSelected(item)
    }

    // Este método se encarga de "cerrar sesión"
    private fun cerrarSesion() {
        val intent = Intent(this, MainActivity::class.java) // Creamos el intent para navegar hacia la actividad de inicio
        startActivity(intent) // Lanzamos la actividad
    }

    private fun navegarACorreo() {
        // Creamos el intent para navegar hacia la app de correo, pasandole la Uri con el correo al que se le va a enviar un mensaje
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:mariogardel3@outlook.es"))
        try {
            startActivity(intent) // Se lanza la actividad
        } catch (e: ActivityNotFoundException) {
            // Si no existe app de correo se muestra un toast informativo
            Toast.makeText(this, getString(R.string.no_encontrada), Toast.LENGTH_SHORT).show()
        }
    }

    private fun navegarAWebOficial() {
        // Creamos el intent para navegar hacia la web, pasandole la URL en forma de Uri
        val intent = Intent(Intent.ACTION_VIEW, "https://instagram.com/box.level?utm_medium=copy_link".toUri())
        try {
            startActivity(intent) // Se lanza la actividad
        } catch (e: ActivityNotFoundException) {
            // Si no existe la app de navegador se muestra un toast informativo
            Toast.makeText(this, getString(R.string.no_encontrada), Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        dia = format(binding.calendar.date) // Obtenemos el día actual
        listHours = Database.queryHorasFromDia(dia) // Obtenemos las horas existentes de ese día
        // Controlamos cuando el usuario clica en otro día
        binding.calendar.setOnDateChangeListener { _, year, month, day ->
            val cal: Calendar = Calendar.getInstance() // Obtenemos una instancia del calendario
            cal.set(year, month, day) // le indicamos el año, el mes y el dia
            dia = format(cal.time) // lo formateamos y lo añadimos a la variable dia
            listHours = Database.queryHorasFromDia(dia) // Obtenemos las horas existentes de ese dia
            showHours() // Actualizamos la lista
        }

        setupRecycler() // Configuramos el recycler view
    }

    @SuppressLint("SimpleDateFormat")
    private fun format(date: Date): String {
        // Formateamos la fecha seleccionada
        return SimpleDateFormat("dd/MM/yyyy").format(date)
    }

    @SuppressLint("SimpleDateFormat")
    private fun format(date: Long): String{
        // Formateamos la fecha seleccionada
        return SimpleDateFormat("dd/MM/yyyy").format(date)
    }

    private fun setupRecycler() {
        binding.recycler.run {
            setHasFixedSize(true) // Dejamos tamaño fijo
            layoutManager = LinearLayoutManager(binding.recycler.context) // Le ponemos disposición lineal
            itemAnimator = DefaultItemAnimator() // Se le aplica la animación por defecto
            addItemDecoration(DividerItemDecoration(context, RecyclerView.HORIZONTAL)) // se le añade un separador
            adapter = listAdapter // se le añade el adaptador
        }
        showHours() // Actualizamos las horas
    }

    private fun showHours() {
        listAdapter.submitList(listHours) // Añade al recyler la lista de las horas
        // Comprueba si esa lista esta vacia o no, en caso de que lo este va a mostrar un mensaje indicando que no hay horas
        // y en caso de que no este vacia, este mensaje se muestra invisible y se muestra la lista
        binding.emptyList.visibility = if(listHours.isEmpty()) View.VISIBLE else View.INVISIBLE
    }
}