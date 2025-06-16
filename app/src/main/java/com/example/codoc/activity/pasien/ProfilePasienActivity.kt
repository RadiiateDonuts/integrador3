package com.example.codoc.activity.pasien

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.DatabaseHelper
import com.example.codoc.databinding.ActivityProfilePasienBinding

class ProfilePasienActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePasienBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePasienBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)

        configurarVista()
        configurarBotonEditar()
    }

    // Mostrar los datos del paciente en pantalla
    private fun configurarVista() {
        binding.apply {
            namauser.text = name
            tanggaluser.text = ttl
            emailuser.text = email
            noTelpUser.text = nohp
        }
    }

    // Abrir pantalla para editar el perfil
    private fun configurarBotonEditar() {
        binding.buttonEdit.setOnClickListener {
            val intent = Intent(baseContext, EditProfilePasienActivity::class.java)
            intent.putExtra("EMAIL", email)
            startActivity(intent)
        }
    }

    companion object {
        var name = "Tes nama"
        var ttl = "Tes nama"
        var email = "Tes nama"
        var nohp = "Tes nama"
    }
}
