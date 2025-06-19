package com.example.codoc.activity.pasien

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.R
import com.example.codoc.model.MyJanjiModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TambahJanjiActivity : AppCompatActivity() {

    private lateinit var selectDateEditText: TextInputEditText
    private lateinit var jamDropdown: AutoCompleteTextView
    private lateinit var btnRegistrar: Button
    private lateinit var keluhanEditText: TextInputEditText // 👈 nuevo campo

    private val calendar = Calendar.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val jamOptions = arrayOf("09:00", "09:20", "09:40", "10:00", "10:20", "10:40",
        "11:00", "11:20", "11:40", "12:00", "12:20", "12:40")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_janji)
        supportActionBar?.hide()

        val namaDokter: TextView = findViewById(R.id.namaDokter)
        val emailDokter: TextView = findViewById(R.id.emailDokter)
        val spesialisDokter: TextView = findViewById(R.id.spesialisDokter)
        val emailPasien: TextView = findViewById(R.id.emailPasien)

        selectDateEditText = findViewById(R.id.editTanggal)
        jamDropdown = findViewById(R.id.editJam)
        keluhanEditText = findViewById(R.id.editKeluhan) // 👈 aquí
        btnRegistrar = findViewById(R.id.buttonRegistrar)

        setupTanggal()
        setupJamDropdown()

        btnRegistrar.setOnClickListener {
            val tanggal = selectDateEditText.text.toString().trim()
            val jam = jamDropdown.text.toString().trim()
            val keluhan = keluhanEditText.text.toString().trim() // 👈 aquí

            val nama = namaDokter.text.toString().trim()
            val emailD = emailDokter.text.toString().trim()
            val spesialis = spesialisDokter.text.toString().trim()
            val emailP = emailPasien.text.toString().trim()

            if (tanggal.isEmpty() || jam.isEmpty() || keluhan.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firestore.collection("janji")
                .whereEqualTo("emailDokter", emailD)
                .whereEqualTo("tanggalJanji", tanggal)
                .whereEqualTo("jamJanji", jam)
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        val newDoc = firestore.collection("janji").document()
                        val janji = MyJanjiModel(
                            id_janji = newDoc.id,
                            namaDokter = nama,
                            emailPasien = emailP,
                            emailDokter = emailD,
                            tanggalJanji = tanggal,
                            jamJanji = jam,
                            spesialis = spesialis,
                            keluhan = keluhan // 👈 aquí
                        )

                        newDoc.set(janji)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Cita registrada exitosamente", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomePasienActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al registrar cita", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Este horario ya está ocupado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al validar horario", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setupJamDropdown() {
        val adapterJam = ArrayAdapter(this, R.layout.dropdown_item, jamOptions)
        jamDropdown.setAdapter(adapterJam)
    }

    private fun setupTanggal() {
        selectDateEditText.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    val selected = Calendar.getInstance()
                    selected.set(year, month, day)

                    if (selected >= Calendar.getInstance()) {
                        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        selectDateEditText.setText(formato.format(selected.time))
                    } else {
                        Toast.makeText(this, "Selecciona una fecha futura", Toast.LENGTH_SHORT).show()
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }
}
