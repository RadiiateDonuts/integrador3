package com.example.codoc.activity.dokter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.example.codoc.DatabaseHelper
import com.example.codoc.R
import com.google.android.material.textfield.TextInputLayout

class RegisterDokterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_dokter)

        // Para el menú desplegable de especialidades
        val spesialDropdown = findViewById<AutoCompleteTextView>(R.id.spesial_dropdown)
        val spesialOptions = arrayOf(
            "General",           // Umum
            "Otorrinolaringología", // THT
            "Dermatología",      // Kulit
            "Odontología",       // Gigi
            "Medicina interna",  // Penyakit Dalam
            "Ginecología",       // Kandungan
            "Neurología"         // Saraf
        )
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, spesialOptions)
        spesialDropdown.setAdapter(adapter)

        // Opcional: Agregar listener para manejar la selección de especialidad
        spesialDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = adapter.getItem(position).toString()
            // Aquí se puede hacer algo con la opción seleccionada
        }

        // Campos del formulario
        val txtEmail: TextInputLayout = findViewById(R.id.emailInput)
        val txtFullname: TextInputLayout = findViewById(R.id.fullnameInput)
        val txtSpecialis: TextInputLayout = findViewById(R.id.menu_spesial)
        val txtAlamat: TextInputLayout = findViewById(R.id.alamatInput)
        val txtNoHp: TextInputLayout = findViewById(R.id.nomorInput)
        val txtPassword: TextInputLayout = findViewById(R.id.inputPassword)

        // Instancia del botón de registro
        val btnRegister: Button = findViewById(R.id.buttonDaftar)

        btnRegister.setOnClickListener {
            // Crear objeto de la clase DatabaseHelper
            val databaseHelper = DatabaseHelper(this)

            // Obtener datos del formulario
            val email: String = txtEmail.editText?.text.toString().trim()
            val name: String = txtFullname.editText?.text.toString().trim()
            val specialis: String = txtSpecialis.editText?.text.toString().trim()
            val alamat: String = txtAlamat.editText?.text.toString().trim()
            val noHp: String = txtNoHp.editText?.text.toString().trim()
            val password: String = txtPassword.editText?.text.toString().trim()

            // Verificar si el correo ya está registrado
            val data: String = databaseHelper.checkDataDokter(email)

            // Si no está registrado
            if (data == "") {
                // Insertar datos
                databaseHelper.addAccountDokter(email, name, specialis, alamat, noHp, password)
                // Redirigir al Login
                val intentLogin = Intent(this@RegisterDokterActivity, LoginDokterActivity::class.java)
                startActivity(intentLogin)

            } else {
                // Si el correo ya está registrado
                Toast.makeText(
                    this@RegisterDokterActivity,
                    "Registro fallido. El correo ya está registrado.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
