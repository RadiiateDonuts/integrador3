package com.example.codoc.activity.dokter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class LoginDokterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_dokter)
        supportActionBar?.hide()

        // Botones
        val btnMasuk: Button = findViewById(R.id.buttonMasuk)
        val btnDaftar: TextView = findViewById(R.id.textViewDaftar)

        // Campos de entrada
        val txtEmailLayout: TextInputLayout = findViewById(R.id.inputEmail)
        val txtPasswordLayout: TextInputLayout = findViewById(R.id.inputPassword)

        btnMasuk.setOnClickListener {
            val email = txtEmailLayout.editText?.text.toString().trim()
            val password = txtPasswordLayout.editText?.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val key = email.replace(".", "_")
            val ref = FirebaseDatabase.getInstance().getReference("akundokter")

            ref.child(key).get().addOnSuccessListener { snapshot ->
                val data = snapshot.value as? Map<*, *>
                if (data != null && data["password"] == password) {
                    // ✅ Guardar email en SharedPreferences para usar después
                    val sharedPref = getSharedPreferences("UserDokterData", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("email", email)
                        apply()
                    }

                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val intentLogin = Intent(this, HomeDokterActivity::class.java)
                    startActivity(intentLogin)
                    finish()
                }
                else {
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    txtEmailLayout.hint = "correo electrónico"
                    txtPasswordLayout.hint = "contraseña"
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al conectar con Firebase", Toast.LENGTH_SHORT).show()
            }
        }

        btnDaftar.setOnClickListener {
            val intentRegisterDokterActivity = Intent(this, RegisterDokterActivity::class.java)
            startActivity(intentRegisterDokterActivity)
        }
    }
}
