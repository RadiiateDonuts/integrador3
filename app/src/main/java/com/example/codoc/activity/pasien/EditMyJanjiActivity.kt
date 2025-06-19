package com.example.codoc.activity.pasien

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditMyJanjiActivity : AppCompatActivity() {

    private lateinit var selectDateEditText: TextInputEditText
    private lateinit var jamDropdown: AutoCompleteTextView
    private lateinit var btnUpdate: Button
    private lateinit var keluhanEditText: TextInputEditText  // 👈 Nuevo campo
    private val calendar = Calendar.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_janji)
        supportActionBar?.hide()

        val textId: TextInputEditText = findViewById(R.id.idJanji)
        val namaDokter: TextView = findViewById(R.id.namaDokter)
        val emailPasien: TextView = findViewById(R.id.emailPasien)
        val emailDokter: TextView = findViewById(R.id.emailDokter)
        val textTanggal: TextInputEditText = findViewById(R.id.editTanggal)
        val textJam: AutoCompleteTextView = findViewById(R.id.editJam)
        val spesialisDokter: TextView = findViewById(R.id.spesialisDokter)
        keluhanEditText = findViewById(R.id.editKeluhan) // 👈 findViewById agregado
        btnUpdate = findViewById(R.id.buttonUpdate)

        selectDateEditText = findViewById(R.id.editTanggal)
        jamDropdown = findViewById(R.id.editJam)
        setupJamDropdown()
        setupTanggal()

        val idJanji = intent.getStringExtra("ID_JANJI")
        val tanggalJanji = intent.getStringExtra("TANGGAL_JANJI")
        val jamJanji = intent.getStringExtra("JAM_JANJI")
        val namaDokterVal = intent.getStringExtra("NAMA_DOKTER")
        val emailDokterVal = intent.getStringExtra("EMAIL_DOKTER")
        val emailPasienVal = intent.getStringExtra("EMAIL_PASIEN")
        val spesialisVal = intent.getStringExtra("SPESIALIS")
        val keluhanVal = intent.getStringExtra("KELUHAN")

        textId.setText(idJanji)
        textTanggal.setText(tanggalJanji)
        jamDropdown.setText(jamJanji)
        keluhanEditText.setText(keluhanVal) // 👈 mostrar keluhan

        btnUpdate.setOnClickListener {
            val idJanjiVal = textId.text.toString().trim()
            val namaDokterFinal = namaDokter.text.toString().trim()
            val emailPasienFinal = emailPasien.text.toString().trim()
            val emailDokterFinal = emailDokter.text.toString().trim()
            val tanggalJanjiFinal = textTanggal.text.toString().trim()
            val jamJanjiFinal = jamDropdown.text.toString().trim()
            val spesialisFinal = spesialisDokter.text.toString().trim()
            val keluhanFinal = keluhanEditText.text.toString().trim()

            // Validar disponibilidad
            firestore.collection("janji")
                .whereEqualTo("emailDokter", emailDokterFinal)
                .whereEqualTo("tanggalJanji", tanggalJanjiFinal)
                .whereEqualTo("jamJanji", jamJanjiFinal)
                .get()
                .addOnSuccessListener { result ->
                    val hayConflicto = result.any { it.id != idJanjiVal }
                    if (hayConflicto) {
                        Toast.makeText(this, "Ese horario ya está ocupado para este doctor", Toast.LENGTH_SHORT).show()
                    } else {
                        // Si no hay conflicto, actualizar la cita
                        val updatedData = mapOf(
                            "namaDokter" to namaDokterFinal,
                            "emailPasien" to emailPasienFinal,
                            "emailDokter" to emailDokterFinal,
                            "tanggalJanji" to tanggalJanjiFinal,
                            "jamJanji" to jamJanjiFinal,
                            "spesialis" to spesialisFinal,
                            "keluhan" to keluhanFinal // 👈 guardar keluhan
                        )
                        firestore.collection("janji")
                            .document(idJanjiVal)
                            .update(updatedData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Cita actualizada exitosamente", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomePasienActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al actualizar la cita", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al verificar disponibilidad", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setupJamDropdown() {
        val jamOptions = arrayOf(
            "09:00 - 09:20", "09:20 - 09:40", "09:40 - 10:00",
            "10:00 - 10:20", "10:20 - 10:40", "10:40 - 11:00",
            "11:00 - 11:20", "11:20 - 11:40", "11:40 - 12:00",
            "12:00 - 12:20", "12:20 - 12:40", "12:40 - 13:00"
        )
        val adapterJam = ArrayAdapter(this, R.layout.dropdown_item, jamOptions)
        jamDropdown.setAdapter(adapterJam)
    }

    private fun setupTanggal() {
        selectDateEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                if (selectedDate >= Calendar.getInstance()) {
                    updateDateInView(selectedDate)
                } else {
                    Toast.makeText(this, "Selecciona una fecha futura", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateInView(calendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        selectDateEditText.setText(sdf.format(calendar.time))
    }
}
