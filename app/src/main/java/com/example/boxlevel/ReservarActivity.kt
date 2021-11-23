package com.example.boxlevel

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.example.boxlevel.data.Horas
import com.example.boxlevel.databinding.ReservarActivityBinding
import com.example.boxlevel.model.Database
import java.lang.RuntimeException

class ReservarActivity : AppCompatActivity() {
    private lateinit var hora: Horas // Variable que almacena la hora
    private lateinit var binding: ReservarActivityBinding
    private var id: Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReservarActivityBinding.inflate(layoutInflater) // Creamos el binding
        setContentView(binding.root)
        getIntentData() // Obtenemos la información del intent
        setupViews() // Configuramos las vistas
    }

    private fun setupViews(){
        // Cuando se haga click en el botón
        binding.button.setOnClickListener {
            hora.reservado = true // Se pone la hora en reservado
            Toast.makeText(this, getString(R.string.reservado), Toast.LENGTH_SHORT).show() // Mostramos un mensaje informativo del cambio
            setActivityResult() // Configuramos el regreso a CalendarioActivity
            finish() // volvemos atrás
        }

        // Cuando se hace un click largo al botón de reservar me muestra el pop up menú
        binding.button.setOnLongClickListener {
            configPopMenu()
            true
        }
    }


    private fun setActivityResult() {
        setResult(RESULT_OK, Intent()) // Crea un intent vacío con un resultado OK
    }

    private fun configPopMenu() {
        val popMenu = PopupMenu(this, binding.button) // creamos la variable que va a manejar el pop up
        popMenu.inflate(R.menu.options_menu) // infla el pop up
        // controla cada vez que hace click
        popMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.sobreNosotros -> navegarAWebOficial() // navega a la web oficial
                R.id.ayuda -> navegarACorreo() // navega al correo
                R.id.cerrarSesion -> cerrarSesion() // cierra sesión
            }
            true
        }
        popMenu.show() // muestra el pop up menú
    }

    // Este método es el que infla el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Este método controla lo que se va a hacer según en el item que clique
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sobreNosotros -> navegarAWebOficial()
            R.id.ayuda -> navegarACorreo()
            R.id.cerrarSesion -> cerrarSesion()
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

    // Este método se encargar de obtener la información del intent
    private fun getIntentData() {
        // Comprobamos que el intent no sea null y además tenga la información que deseo
        if(intent == null || !intent.hasExtra(EXTRA_ID)){
            throw RuntimeException("ReservaActivity needs to receive id as extras")
        }

        id = intent.getLongExtra(EXTRA_ID, 1) // obtener el id pasado por el intent

        hora = Database.getHoraById(id) // obtenemos la hora con es id

        // Rellenamos las vistas con la información
        binding.textView.text = hora.time
        binding.textView2.text = hora.entrenador
        binding.button.isEnabled = !hora.reservado
    }

    // Singleton de ReservarActivity
    companion object{
        const val EXTRA_ID = "EXTRA_ID" // EXTRA que necesito

        // Realizo el intent
        fun newIntent(context: Context, id: Long): Intent {
            return Intent(context, ReservarActivity::class.java)
                .putExtras(bundleOf(EXTRA_ID to id))
        }
    }
}