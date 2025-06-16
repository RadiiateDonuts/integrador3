package com.example.codoc.activity.pasien

import android.content.Intent
// Dependencias para recuperar datos en ProfileActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.codoc.DatabaseHelper
import com.example.codoc.R
import com.google.android.material.textfield.TextInputLayout

class LoginPasienActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_pasien)

        supportActionBar?.hide()

        // Botones
        val btnMasuk: Button = findViewById(R.id.buttonMasuk)
        val btnDaftar: TextView = findViewById(R.id.textViewDaftar)

        // Instancias de campos de texto
        val txtEmailLayout: TextInputLayout = findViewById(R.id.inputEmail)
        val txtPasswordLayout: TextInputLayout = findViewById(R.id.inputPassword)

        // Evento del botón "Ingresar"
        btnMasuk.setOnClickListener {

            // Instancia de base de datos
            val dbHelper = DatabaseHelper(this)

            // Acceder al EditText dentro de TextInputLayout
            val email = txtEmailLayout.editText?.text.toString().trim()
            val password = txtPasswordLayout.editText?.text.toString().trim()

            // Verificar credenciales de inicio de sesión
            val result: Boolean = dbHelper.checkLoginPasien(email, password)
            if (result) {

                val intentLogin = Intent(this@LoginPasienActivity, HomePasienActivity::class.java)
                startActivity(intentLogin)
            } else {
                Toast.makeText(this, "Inicio de sesión fallido. Inténtalo de nuevo", Toast.LENGTH_SHORT).show()
                txtEmailLayout.hint = "Correo electrónico"
                txtPasswordLayout.hint = "Contraseña"
            }
        }

        // Evento del botón "Registrarse"
        btnDaftar.setOnClickListener {
            val intentRegisterPasienActivity = Intent(this, RegisterPasienActivity::class.java)
            startActivity(intentRegisterPasienActivity)
        }
    }
}
