package com.example.codoc.activity.pasien

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.Button
import android.widget.Toast
import com.example.codoc.R
import com.example.codoc.firebase.FirebaseHelper // ⬅️ Importamos el helper para Firebase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterPasienActivity : AppCompatActivity() {

    // Campo para seleccionar la fecha
    private lateinit var selectDateEditText: TextInputEditText
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pasien)

        // Instancia del input de fecha
        selectDateEditText = findViewById(R.id.select_date)

        // Al dar clic, se abre el DatePicker
        selectDateEditText.setOnClickListener { mostrarDialogoFecha() }

        // Inputs del formulario
        val txtFullname: TextInputLayout = findViewById(R.id.fullnameInput)
        val txtDateOfBirth: TextInputLayout = findViewById(R.id.jadwalInput)
        val txtEmail: TextInputLayout = findViewById(R.id.emailInput)
        val txtNoHp: TextInputLayout = findViewById(R.id.nomorInput)
        val txtPassword: TextInputLayout = findViewById(R.id.inputPassword)
        val btnRegister: Button = findViewById(R.id.buttonDaftar)

        // Aplica formato telefónico automático
        val inputNomorEdit: TextInputEditText = findViewById(R.id.InputNomorEdit)
        inputNomorEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        // Acción al presionar el botón "Registrar"
        btnRegister.setOnClickListener {
            // Obtener los valores escritos por el usuario
            val email = txtEmail.editText?.text.toString().trim()
            val name = txtFullname.editText?.text.toString().trim()
            val dateOfBirth = txtDateOfBirth.editText?.text.toString().trim()
            val noHp = txtNoHp.editText?.text.toString().trim()
            val password = txtPassword.editText?.text.toString().trim()

            // Verificar que ningún campo esté vacío
            if (email.isEmpty() || name.isEmpty() || dateOfBirth.isEmpty() || noHp.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Usamos el email como clave (reemplazamos puntos por guiones bajos)
            val key = email.replace(".", "_")
            val ref = FirebaseDatabase.getInstance().getReference("akunpasien")

            // Validamos si el correo ya está registrado en Firebase
            ref.child(key).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Ya existe: no permitimos duplicado
                    Toast.makeText(this, "Este correo ya está registrado.", Toast.LENGTH_SHORT).show()
                } else {
                    // Si no existe, registramos el paciente
                    FirebaseHelper.registrarPaciente(email, name, dateOfBirth, noHp, password) { exito ->
                        if (exito) {
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginPasienActivity::class.java))
                            finish() // Cerramos la actividad actual
                        } else {
                            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnFailureListener {
                // Si hay error al acceder a Firebase
                Toast.makeText(this, "Error al conectar con Firebase", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Muestra el selector de fecha
    private fun mostrarDialogoFecha() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
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

    // Muestra la fecha en el campo
    private fun actualizarFechaEnVista(calendar: Calendar) {
        val formato = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(formato, Locale.getDefault())
        selectDateEditText.setText(sdf.format(calendar.time))
    }
}
