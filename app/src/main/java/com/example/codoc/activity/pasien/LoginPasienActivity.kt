package com.example.codoc.activity.pasien

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.codoc.R
import com.example.codoc.firebase.FirebaseHelper // 👈 Importa el helper que creamos
import com.google.android.material.textfield.TextInputLayout

class LoginPasienActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_pasien)

        // Oculta la barra superior
        supportActionBar?.hide()

        // Botones y campos de texto
        val btnMasuk: Button = findViewById(R.id.buttonMasuk)
        val btnDaftar: TextView = findViewById(R.id.textViewDaftar)
        val txtEmailLayout: TextInputLayout = findViewById(R.id.inputEmail)
        val txtPasswordLayout: TextInputLayout = findViewById(R.id.inputPassword)

        // Evento de clic en "Ingresar"
        btnMasuk.setOnClickListener {
            val email = txtEmailLayout.editText?.text.toString().trim()
            val password = txtPasswordLayout.editText?.text.toString().trim()

            // Validación básica de campos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, llena ambos campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar las credenciales usando Firebase
            FirebaseHelper.loginPaciente(email, password) { loginExitoso ->
                if (loginExitoso) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomePasienActivity::class.java))
                    finish() // Cerramos pantalla de login
                } else {
                    Toast.makeText(this, "Inicio de sesión fallido. Verifica tu correo y contraseña", Toast.LENGTH_SHORT).show()
                    txtEmailLayout.hint = "Correo electrónico"
                    txtPasswordLayout.hint = "Contraseña"
                }
            }
        }

        // Redirección a registro si no tiene cuenta
        btnDaftar.setOnClickListener {
            val intentRegister = Intent(this, RegisterPasienActivity::class.java)
            startActivity(intentRegister)
        }
    }
}
