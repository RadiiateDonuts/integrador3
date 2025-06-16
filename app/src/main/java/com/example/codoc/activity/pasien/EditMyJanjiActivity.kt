package com.example.codoc.activity.pasien

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.codoc.DatabaseHelper
import com.example.codoc.FragmentDokterMyJanji
import com.example.codoc.R
import com.example.codoc.model.MyJanjiModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class EditMyJanjiActivity : AppCompatActivity() {

    private lateinit var selectDateEditText: TextInputEditText
    private lateinit var jamDropdown: AutoCompleteTextView
    private lateinit var btnUpdate: Button
    private val calendar = Calendar.getInstance()
    private val databaseHelper = DatabaseHelper(this)

    companion object {
        var idJanji = 1
        var tanggalJanji = "prueba"
        var jamJanji = "prueba"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_janji)

        // Ocultar la barra de título
        supportActionBar?.hide()

        // Instanciar vistas
        val textId: TextInputEditText = findViewById(R.id.idJanji)
        val namaDokter: TextView = findViewById(R.id.namaDokter)
        val emailPasien: TextView = findViewById(R.id.emailPasien)
        val emailDokter: TextView = findViewById(R.id.emailDokter)
        val textTanggal: TextInputEditText = findViewById(R.id.editTanggal)
        val textJam: AutoCompleteTextView = findViewById(R.id.editJam)
        val spesialisDokter: TextView = findViewById(R.id.spesialisDokter)
        btnUpdate = findViewById(R.id.buttonUpdate)

        // Inicializar el campo de fecha
        selectDateEditText = findViewById(R.id.editTanggal)

        // Configurar el menú desplegable de horario
        jamDropdown = findViewById(R.id.editJam)
        setupJamDropdown()

        // Configurar selección de fecha
        setupTanggal()

        // Obtener datos desde el intent
        val idJanji = intent.getStringExtra("ID_JANJI")
        val tanggalJanji = intent.getStringExtra("TANGGAL_JANJI")
        val jamJanji = intent.getStringExtra("JAM_JANJI")

        // Establecer valores en las vistas
        textId.setText(idJanji.toString())
        textTanggal.setText(tanggalJanji)

        // Seleccionar valor en el dropdown
        val jamOptions = arrayOf("09:00 - 11:00", "11:00 - 13:00", "13:00 - 15:00", "15:00 - 17:00")
        val selectedJamPosition = jamOptions.indexOf(jamJanji)
        textJam.setText(jamOptions[selectedJamPosition])

        // Configurar el adaptador del dropdown
        setupJamDropdown()

        btnUpdate.setOnClickListener {
            // Crear objeto de tipo DatabaseHelper
            val databaseHelper = DatabaseHelper(this)

            val idJanji: String = textId.text.toString().trim()
            val namaDokter: String = namaDokter.text.toString().trim()
            val emailPasien: String = emailPasien.text.toString().trim()
            val emailDokter: String = emailDokter.text.toString().trim()
            val tanggalJanji: String = textTanggal.text.toString().trim()
            val jamJanji: String = textJam.text.toString().trim()
            val spesialis: String = spesialisDokter.text.toString().trim()

            val janjiModel = MyJanjiModel(idJanji, namaDokter, emailPasien, emailDokter, tanggalJanji, jamJanji, spesialis)
            databaseHelper.updateJanji(janjiModel)

            // Ir a la pantalla principal del paciente
            val intent = Intent(this, HomePasienActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupJamDropdown() {
        val jamOptions = arrayOf("09:00 - 11:00", "11:00 - 13:00", "13:00 - 15:00", "15:00 - 17:00")
        val adapterJam = ArrayAdapter(this, R.layout.dropdown_item, jamOptions)
        jamDropdown.setAdapter(adapterJam)

        jamDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedOption = adapterJam.getItem(position).toString()
            // Hacer algo con la opción seleccionada
        }
    }

    private fun setupTanggal() {
        selectDateEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                if (selectedDate >= Calendar.getInstance()) {
                    updateDateInView(selectedDate)
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

    private fun updateDateInView(calendar: Calendar) {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        selectDateEditText.setText(sdf.format(calendar.time))
    }
}
