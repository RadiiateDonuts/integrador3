package com.example.codoc.activity.dokter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.codoc.DatabaseHelper
import com.example.codoc.databinding.ActivityProfileDokterBinding

class ProfileDokterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDokterBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDokterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        setupViews()
    }

    // Configura los datos del perfil del doctor en la vista
    private fun setupViews() {
        binding.apply {
            namauser.text = name
            spesialisDokter.text = spesialis
            alamat.text = alamatRs
            emailDokter.text = email
            noHpDokter.text = nohp
        }
    }

    companion object {
        var name = "Ejemplo de nombre"        // Texto de prueba para nombre
        var spesialis = "Ejemplo de especialidad"   // Texto de prueba para especialidad
        var alamatRs = "Ejemplo de dirección"    // Texto de prueba para dirección
        var email = "correo@ejemplo.com"       // Texto de prueba para correo
        var nohp = "000-000-0000"        // Texto de prueba para teléfono
    }
}
