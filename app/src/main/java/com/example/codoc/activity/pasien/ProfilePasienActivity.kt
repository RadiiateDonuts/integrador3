package com.example.codoc.activity.pasien

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.databinding.ActivityProfilePasienBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ProfilePasienActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePasienBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePasienBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosDesdeFirebase()
        configurarBotonFecha()
        configurarBotonGuardar()
    }

    private fun cargarDatosDesdeFirebase() {
        val sharedPref = getSharedPreferences("UserPasienData", MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        if (email != null) {
            val key = email.replace(".", "_")
            val ref = FirebaseDatabase.getInstance().getReference("akunpasien").child(key)

            ref.get().addOnSuccessListener { snapshot ->
                binding.editNama.setText(snapshot.child("name").value?.toString() ?: "")
                binding.editTanggal.setText(snapshot.child("ttl").value?.toString() ?: "")
                binding.emailuser.text = snapshot.child("email").value?.toString() ?: ""
                binding.editNoHp.setText(snapshot.child("nohp").value?.toString() ?: "")
            }.addOnFailureListener {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarBotonFecha() {
        binding.editTanggal.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, month, day)
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.editTanggal.setText(sdf.format(selectedDate.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun configurarBotonGuardar() {
        binding.btnGuardar.setOnClickListener {
            val sharedPref = getSharedPreferences("UserPasienData", MODE_PRIVATE)
            val email = sharedPref.getString("email", null)

            if (email != null) {
                val key = email.replace(".", "_")
                val ref = FirebaseDatabase.getInstance().getReference("akunpasien").child(key)

                val updatedData = mapOf(
                    "name" to binding.editNama.text.toString().trim(),
                    "ttl" to binding.editTanggal.text.toString().trim(),
                    "email" to email,
                    "nohp" to binding.editNoHp.text.toString().trim()
                )

                ref.setValue(updatedData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
