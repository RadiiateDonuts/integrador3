package com.example.codoc.activity.dokter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.codoc.DatabaseHelper
import com.example.codoc.R
import com.google.android.material.textfield.TextInputLayout

class LoginDokterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_dokter)

        supportActionBar?.hide()

        // Botones
        val btnMasuk: Button = findViewById(R.id.buttonMasuk)
        val btnDaftar: TextView = findViewById(R.id.textViewDaftar)

        // Instancias de los campos de texto
        val txtEmailLayout: TextInputLayout = findViewById(R.id.inputEmail)
        val txtPasswordLayout: TextInputLayout = findViewById(R.id.inputPassword)

        // Evento del botón "Ingresar / Login"
        btnMasuk.setOnClickListener {

            // Instancia de la base de datos
            val dbHelper = DatabaseHelper(this)

            // Obtener el texto de los campos
            val email = txtEmailLayout.editText?.text.toString().trim()
            val password = txtPasswordLayout.editText?.text.toString().trim()

            // Verificar inicio de sesión
            val result: Boolean = dbHelper.checkLoginDokter(email, password)
            if (result) {

                val intentLogin = Intent(this@LoginDokterActivity, HomeDokterActivity::class.java)
                startActivity(intentLogin)
            } else {
                Toast.makeText(this, "Inicio de sesión fallido. Intenta de nuevo", Toast.LENGTH_SHORT).show()
                txtEmailLayout.hint = "correo electrónico"
                txtPasswordLayout.hint = "contraseña"
            }
        }

        // Evento del texto "Registrarse"
        btnDaftar.setOnClickListener {
            val intentRegisterDokterActivity = Intent(this, RegisterDokterActivity::class.java)
            startActivity(intentRegisterDokterActivity)
        }
    }
}
