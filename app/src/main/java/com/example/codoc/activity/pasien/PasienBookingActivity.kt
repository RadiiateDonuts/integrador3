package com.example.codoc.activity.pasien

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.R
import com.example.codoc.DatabaseHelper
import com.example.codoc.activity.dokter.ProfileDokterActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class PasienBookingActivity : AppCompatActivity() {

    private lateinit var selectDateEditText: TextInputEditText
    private val calendar = Calendar.getInstance()
    private val databaseHelper = DatabaseHelper(this)
    private lateinit var jamDropdown: AutoCompleteTextView
    private lateinit var buttonBuatJanji: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        inicializarVistas()
        configurarDropdownHoras()
        configurarSeleccionFecha()
        configurarBotonCrearCita()
    }

    private fun inicializarVistas() {
        selectDateEditText = findViewById(R.id.select_date)
        jamDropdown = findViewById(R.id.jam_dropdown)
        buttonBuatJanji = findViewById(R.id.buttonbuatjanji)
    }

    private fun configurarDropdownHoras() {
        val opcionesHoras = arrayOf("09:00 - 11:00", "11:00 - 13:00", "13:00 - 15:00", "15:00 - 17:00")
        val adapterHoras = ArrayAdapter(this, R.layout.dropdown_item, opcionesHoras)
        jamDropdown.setAdapter(adapterHoras)

        jamDropdown.setOnItemClickListener { _, _, position, _ ->
            val opcionSeleccionada = adapterHoras.getItem(position).toString()
            // Hacer algo con la opción seleccionada
        }
    }

    private fun configurarSeleccionFecha() {
        selectDateEditText.setOnClickListener {
            mostrarSelectorDeFecha()
        }
    }

    private fun configurarBotonCrearCita() {
        buttonBuatJanji.setOnClickListener {
            crearCita()
        }
    }

    private fun mostrarSelectorDeFecha() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val fechaSeleccionada = Calendar.getInstance()
                fechaSeleccionada.set(year, monthOfYear, dayOfMonth)

                if (fechaSeleccionada >= Calendar.getInstance()) {
                    actualizarFechaEnVista(fechaSeleccionada)
                } else {
                    Toast.makeText(this, "Por favor selecciona una fecha futura", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun actualizarFechaEnVista(calendar: Calendar) {
        val formato = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(formato, Locale.getDefault())
        selectDateEditText.setText(sdf.format(calendar.time))
    }

    private fun crearCita() {
        val fechaSeleccionada = selectDateEditText.text.toString().trim()
        val horaSeleccionada = jamDropdown.text.toString().trim()

        if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val emailPasien = ProfilePasienActivity.email
        val emailDokter = ProfileDokterActivity.email
        val nombreDokter = ProfileDokterActivity.name
        val especialidadDokter = ProfileDokterActivity.spesialis

        if (databaseHelper.saveAppointment(nombreDokter, emailDokter, emailPasien, especialidadDokter, fechaSeleccionada, horaSeleccionada)) {
            Toast.makeText(this, "Cita creada exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al crear la cita", Toast.LENGTH_SHORT).show()
        }
    }
}
