package com.example.boxlevel

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.boxlevel.databinding.MainActivityBinding
import com.example.boxlevel.vistas.CalendarioActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater) // Crea el binding
        setContentView(binding.root)
        binding.btnEntrar.setOnClickListener { navegarCalendarioActivity() } // Cuando hace click llama al metodo navegarCalendarioActivity
    }

    // Este método es el que infla el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Este método es el que invisibiliza el tercer item del menú 'Cerrar Sesión' (cuando ya esta en la activity fuera de la sesión)
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(2)?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    // Este método controla lo que se va a hacer según en el item que clique
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sobreNosotros -> navegarAWebOficial() // Si hace click en "Sobre nosotros" navega a la web oficial
            R.id.ayuda -> navegarACorreo() // Si hace click en "Ayuda" navega al correo electrónico
        }
        return super.onOptionsItemSelected(item)
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

    private fun navegarCalendarioActivity() {
        // Creamos un intent para navegar a la pantalla de Calendario
        val intent = Intent(this, CalendarioActivity::class.java)
        startActivity(intent) // Se lanza la actividad
    }

}
