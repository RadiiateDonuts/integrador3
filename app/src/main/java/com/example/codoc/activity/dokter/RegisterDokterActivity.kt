package com.example.codoc.activity.dokter

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class RegisterDokterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_dokter)
        supportActionBar?.hide()

        // Menú desplegable de especialidades
        val spesialDropdown = findViewById<AutoCompleteTextView>(R.id.spesial_dropdown)
        val spesialOptions = arrayOf(
            "General",
            "Otorrinolaringología",
            "Dermatología",
            "Odontología",
            "Medicina interna",
            "Ginecología",
            "Neurología"
        )
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, spesialOptions)
        spesialDropdown.setAdapter(adapter)

        // Campos del formulario
        val txtEmail: TextInputLayout = findViewById(R.id.emailInput)
        val txtFullname: TextInputLayout = findViewById(R.id.fullnameInput)
        val txtSpecialis: TextInputLayout = findViewById(R.id.menu_spesial)
        val txtAlamat: TextInputLayout = findViewById(R.id.alamatInput)
        val txtNoHp: TextInputLayout = findViewById(R.id.nomorInput)
        val txtPassword: TextInputLayout = findViewById(R.id.inputPassword)
        val btnRegister: Button = findViewById(R.id.buttonDaftar)

        btnRegister.setOnClickListener {
            val email = txtEmail.editText?.text.toString().trim()
            val name = txtFullname.editText?.text.toString().trim()
            val specialis = txtSpecialis.editText?.text.toString().trim()
            val alamat = txtAlamat.editText?.text.toString().trim()
            val noHp = txtNoHp.editText?.text.toString().trim()
            val password = txtPassword.editText?.text.toString().trim()

            if (email.isEmpty() || name.isEmpty() || specialis.isEmpty()
                || alamat.isEmpty() || noHp.isEmpty() || password.isEmpty()
            ) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val key = email.replace(".", "_")
            val ref = FirebaseDatabase.getInstance().getReference("akundokter")

            ref.child(key).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    Toast.makeText(this, "Este correo ya está registrado.", Toast.LENGTH_SHORT).show()
                } else {
                    val dokterData = mapOf(
                        "name" to name,
                        "specialis" to specialis,
                        "alamat" to alamat,
                        "noHp" to noHp,
                        "password" to password
                    )
                    ref.child(key).setValue(dokterData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginDokterActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al conectar con Firebase", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
