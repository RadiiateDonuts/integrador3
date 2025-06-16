package com.example.codoc.activity.dokter

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.codoc.R
import com.example.codoc.activity.pasien.PasienBookingActivity
import com.example.codoc.activity.pasien.HomePasienActivity
import com.example.codoc.model.ProfileDokterModel

class DetailDokterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_dokter)

        // Botón para regresar a la pantalla anterior
        val backButton: ImageView = findViewById(R.id.back)
        backButton.setOnClickListener {
            val intent = Intent(this, HomePasienActivity::class.java)
            startActivity(intent)
        }

        // Botón para agendar una cita
        val book_appointment: AppCompatButton = findViewById(R.id.book_appointment)
        book_appointment.setOnClickListener {
            val intent = Intent(this, PasienBookingActivity::class.java)
            startActivity(intent)
        }

        // Obtener los datos enviados desde la actividad anterior
        val dokterId = intent.getIntExtra("dokterId", 0)

        val user = intent.getParcelableExtra<ProfileDokterModel>("DokterModel")

        // Asignar los valores a los elementos de la vista
        val nams = findViewById<TextView>(R.id.namadokter)
        val spes = findViewById<TextView>(R.id.spesialis)
        val alamat = findViewById<TextView>(R.id.doctor_address)
        nams.text = user?.nama
        spes.text = user?.spesialis
        alamat.text = user?.alamat

        // Evento al presionar el ícono de correo electrónico
        val emailIdView = findViewById<ImageView>(R.id.email_id)
        emailIdView.setOnClickListener {
            sendEmail(user?.email)
        }

        // Evento al presionar el ícono de llamada telefónica
        val phoneCallView = findViewById<ImageView>(R.id.phone_call)
        phoneCallView.setOnClickListener {
            dialPhoneNumber(user?.noHp)
        }
    }

    // Función para enviar correo electrónico
    private fun sendEmail(email: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")
        startActivity(intent)
    }

    // Función para realizar llamada telefónica
    private fun dialPhoneNumber(phoneNumber: String?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }
}
