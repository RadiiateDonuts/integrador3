package com.example.codoc.activity.pasien

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.Button
import android.widget.Toast
import com.example.codoc.DatabaseHelper
import com.example.codoc.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterPasienActivity : AppCompatActivity() {

    private lateinit var selectDateEditText: TextInputEditText
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pasien)

        // Para el calendario
        selectDateEditText = findViewById(R.id.select_date)

        // Al hacer clic se abre el selector de fecha
        selectDateEditText.setOnClickListener {
            mostrarDialogoFecha()
        }

        // Inputs del formulario
        val txtFullname: TextInputLayout = findViewById(R.id.fullnameInput)
        val txtDateOfBirth: TextInputLayout = findViewById(R.id.jadwalInput)
        val txtEmail: TextInputLayout = findViewById(R.id.emailInput)
        val txtNoHp: TextInputLayout = findViewById(R.id.nomorInput)
        val txtPassword: TextInputLayout = findViewById(R.id.inputPassword)

        // Botón de registrar
        val btnRegister: Button = findViewById(R.id.buttonDaftar)

        // Aplicar formato automático a número telefónico
        val inputNomorEdit: TextInputEditText = findViewById(R.id.InputNomorEdit)
        inputNomorEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        // Acción del botón registrar
        btnRegister.setOnClickListener {
            val databaseHelper = DatabaseHelper(this)

            val email: String = txtEmail.editText?.text.toString().trim()
            val name: String = txtFullname.editText?.text.toString().trim()
            val dateOfBirth: String = txtDateOfBirth.editText?.text.toString().trim()
            val noHp: String = txtNoHp.editText?.text.toString().trim()
            val password: String = txtPassword.editText?.text.toString().trim()

            val data: String = databaseHelper.checkDataPasien(email)

            // Si el correo aún no ha sido registrado
            if (data == "") {
                databaseHelper.addAccountPasien(email, name, dateOfBirth, noHp, password)

                // Ir a pantalla de login
                val intentLogin = Intent(this@RegisterPasienActivity, LoginPasienActivity::class.java)
                startActivity(intentLogin)
            } else {
                // Si ya está registrado
                Toast.makeText(
                    this@RegisterPasienActivity,
                    "Registro fallido. Este correo ya está registrado.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Mostrar calendario
    private fun mostrarDialogoFecha() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                if (selectedDate < Calendar.getInstance()) {
                    actualizarFechaEnVista(selectedDate)
                } else {
                    Toast.makeText(this, "Verifica nuevamente tu fecha de nacimiento", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    // Actualizar campo con fecha seleccionada
    private fun actualizarFechaEnVista(calendar: Calendar) {
        val formato = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(formato, Locale.getDefault())
        selectDateEditText.setText(sdf.format(calendar.time))
    }
}
