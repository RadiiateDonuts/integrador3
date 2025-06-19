package com.example.codoc.activity.dokter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codoc.databinding.ActivityProfileDokterBinding
import com.google.firebase.database.FirebaseDatabase

class ProfileDokterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDokterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDokterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosDoctor()
        binding.btnGuardar.setOnClickListener { guardarCambios() }
    }

    private fun cargarDatosDoctor() {
        val sharedPref = getSharedPreferences("UserDokterData", MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        if (email != null) {
            val key = email.replace(".", "_")
            val ref = FirebaseDatabase.getInstance().getReference("akundokter").child(key)

            ref.get().addOnSuccessListener { snapshot ->
                binding.editNama.setText(snapshot.child("name").value?.toString() ?: "")
                binding.editSpesialis.setText(snapshot.child("specialis").value?.toString() ?: "")
                binding.editAlamat.setText(snapshot.child("alamat").value?.toString() ?: "")
                binding.editNoHp.setText(snapshot.child("noHp").value?.toString() ?: "")
                binding.emailDokter.text = email
            }
        }
    }

    private fun guardarCambios() {
        val sharedPref = getSharedPreferences("UserDokterData", MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        if (email != null) {
            val key = email.replace(".", "_")
            val ref = FirebaseDatabase.getInstance().getReference("akundokter").child(key)

            val nuevosDatos = mapOf(
                "name" to binding.editNama.text.toString().trim(),
                "specialis" to binding.editSpesialis.text.toString().trim(),
                "alamat" to binding.editAlamat.text.toString().trim(),
                "noHp" to binding.editNoHp.text.toString().trim()
            )

            ref.updateChildren(nuevosDatos).addOnSuccessListener {
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al guardar cambios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
