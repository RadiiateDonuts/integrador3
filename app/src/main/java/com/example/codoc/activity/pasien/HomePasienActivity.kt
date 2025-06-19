package com.example.codoc.activity.pasien

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.codoc.FragmentBerita
import com.example.codoc.FragmentPasienHome
import com.example.codoc.FragmentPasienMyJanji
import com.example.codoc.FragmentPasienSettings
import com.example.codoc.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePasienActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pasien)

        // Ocultar la barra de título
        supportActionBar?.hide()

        // Obtener el email del usuario logueado (si existe)
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        // Puedes usar ese email para mostrar mensajes o pasar al fragment
        // println("Usuario logueado: $email") // DEBUG opcional

        // Instanciar el menú de navegación inferior
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)

        // Crear instancias de los fragmentos
        val homeFragment = FragmentPasienHome()
        val settingsFragment = FragmentPasienSettings()
        val myJanjiFragment = FragmentPasienMyJanji()
        val beritaFragment = FragmentBerita()

        // Mostrar el fragmento principal por defecto
        currentFragment(homeFragment)

        // Manejar navegación entre fragmentos
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    currentFragment(homeFragment)
                    true
                }
                R.id.appointment -> {
                    currentFragment(myJanjiFragment)
                    true
                }
                R.id.settings -> {
                    currentFragment(settingsFragment)
                    true
                }
                R.id.stats -> {
                    currentFragment(beritaFragment)
                    true
                }
                else -> false
            }
        }
    }

    // Reemplazar el fragmento actual en pantalla
    private fun currentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }
}
