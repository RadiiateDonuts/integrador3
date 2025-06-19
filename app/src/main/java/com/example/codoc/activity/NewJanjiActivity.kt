package com.example.codoc.activity.pasien

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class NewJanjiActivity : AppCompatActivity() {

    private lateinit var etFecha: TextInputEditText
    private lateinit var etHora: AutoCompleteTextView
    private lateinit var etDoctor: AutoCompleteTextView
    private lateinit var etEspecialidad: AutoCompleteTextView
    private lateinit var btnGuardar: Button

    private val calendar = Calendar.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_janji)
        supportActionBar?.hide()

        etFecha = findViewById(R.id.etFecha)
        etHora = findViewById(R.id.etHora)
        etDoctor = findViewById(R.id.etDoctor)
        etEspecialidad = findViewById(R.id.etEspecialidad)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Configurar selects
        setupDropdowns()
        etFecha.setOnClickListener { showDatePicker() }

        btnGuardar.setOnClickListener {
            guardarCita()
        }
    }

    private fun setupDropdowns() {
        val doctores = listOf("Dr. María Soto", "Dr. Juan Pérez", "Dr. Luis Ortega")
        val especialidades = listOf("Ginecología", "Pediatría", "Medicina General", "Dermatología")
        val horarios = listOf("09:00 - 11:00", "11:00 - 13:00", "13:00 - 15:00", "15:00 - 17:00")

        etDoctor.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, doctores))
        etEspecialidad.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, especialidades))
        etHora.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, horarios))
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // ← corregido
                etFecha.setText(formato.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }


    private fun guardarCita() {
        val fecha = etFecha.text.toString().trim()
        val hora = etHora.text.toString().trim()
        val doctor = etDoctor.text.toString().trim()
        val especialidad = etEspecialidad.text.toString().trim()

        if (fecha.isEmpty() || hora.isEmpty() || doctor.isEmpty() || especialidad.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaCita = hashMapOf(
            "id_janji" to UUID.randomUUID().toString(),
            "uid_pasien" to "uid_prueba",
            "emailPasien" to "paciente@correo.com",
            "emailDokter" to "doctor@correo.com",
            "namaDokter" to doctor,
            "spesialis" to especialidad,
            "tanggalJanji" to fecha,
            "jamJanji" to hora
        )

        firestore.collection("janji")
            .add(nuevaCita)
            .addOnSuccessListener {
                Toast.makeText(this, "Cita guardada correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomePasienActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar la cita", Toast.LENGTH_SHORT).show()
            }
    }
}
