package com.example.codoc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log // 👈 Import necesario para usar Log
import android.widget.ImageView
import com.example.codoc.R
import com.example.codoc.activity.dokter.LoginDokterActivity
import com.example.codoc.activity.pasien.LoginPasienActivity
import com.google.firebase.FirebaseApp

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Inicializar Firebase
        FirebaseApp.initializeApp(this)

        // ✅ Mostrar en Logcat si Firebase está funcionando
        Log.d("FIREBASE", "Firebase iniciado correctamente")

        setContentView(R.layout.activity_first)

        val btnMasukpasien: ImageView = findViewById(R.id.pasienImage)
        val btnMasukdokter: ImageView = findViewById(R.id.dokterImage)

        btnMasukpasien.setOnClickListener {
            val intentpasien = Intent(this, LoginPasienActivity::class.java)
            startActivity(intentpasien)
        }

        btnMasukdokter.setOnClickListener {
            val intentdokter = Intent(this, LoginDokterActivity::class.java)
            startActivity(intentdokter)
        }
    }
}
