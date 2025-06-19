package com.example.codoc.firebase

import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {

    // 🔹 Registro de paciente
    fun registrarPaciente(
        email: String,
        name: String,
        dateOfBirth: String,
        noHp: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        val db = FirebaseDatabase.getInstance().getReference("akunpasien")
        val key = email.replace(".", "_") // Firebase no permite puntos como keys

        val paciente = mapOf(
            "name" to name,
            "dateOfBirth" to dateOfBirth,
            "noHp" to noHp,
            "password" to password
        )

        db.child(key).setValue(paciente)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // 🔹 Login de paciente
    fun loginPaciente(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        val db = FirebaseDatabase.getInstance().getReference("akunpasien")
        val key = email.replace(".", "_")

        db.child(key).get().addOnSuccessListener { snapshot ->
            val data = snapshot.value as? Map<*, *>
            if (data != null && data["password"] == password) {
                onResult(true)
            } else {
                onResult(false)
            }
        }.addOnFailureListener {
            onResult(false)
        }
    }
}
