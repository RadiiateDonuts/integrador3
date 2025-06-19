package com.example.codoc

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codoc.adapter.AdapterDokter
import com.example.codoc.databinding.FragmentPasienHomeBinding
import com.google.android.material.chip.Chip
import com.google.firebase.database.FirebaseDatabase

class FragmentPasienHome : Fragment() {

    private lateinit var binding: FragmentPasienHomeBinding
    private lateinit var adapterDokter: AdapterDokter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasienHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // Especialidades de doctores
        val listOfSpecialties = listOf("General", "Otorrinolaringología", "Dermatología", "Odontología", "Medicina Interna", "Ginecología", "Neurología")

        // Inicializar RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewDoctor)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterDokter = AdapterDokter(emptyList())
        recyclerView.adapter = adapterDokter

        // Búsqueda de doctores (actualiza con datos reales si lo usas)
        binding.doctorData.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Aquí puedes buscar en Firebase si migras también los doctores
            }
        })

        // Mostrar nombre del paciente desde Firebase
        mostrarNombrePacienteDesdeFirebase(view)

        // Agregar chips para filtrar especialidades
        val chipContainer: LinearLayout = view.findViewById(R.id.chipContainer)
        for (specialty in listOfSpecialties) {
            val chip = Chip(context).apply {
                text = specialty
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 8, 8, 8)
                layoutParams = params
                setOnClickListener {
                    // Aquí también puedes implementar filtrado con Firebase si migras doctores
                }
            }
            chipContainer.addView(chip)
        }

        return view
    }

    private fun mostrarNombrePacienteDesdeFirebase(view: View) {
        val sharedPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        if (email != null) {
            val key = email.replace(".", "_")
            val ref = FirebaseDatabase.getInstance().getReference("akunpasien").child(key)

            ref.get().addOnSuccessListener { snapshot ->
                val nombre = snapshot.child("name").value?.toString() ?: "Paciente"
                val textNama: TextView = view.findViewById(R.id.namapasien)
                textNama.text = "Hola, $nombre!"
            }
        }
    }
}