package com.example.codoc

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

class FragmentPasienHome : Fragment() {

    private lateinit var binding: FragmentPasienHomeBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasienHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        dbHelper = DatabaseHelper(requireContext())

        // Lista de especialidades
        val listOfSpecialties = listOf("General", "Otorrinolaringología", "Dermatología", "Odontología", "Medicina Interna", "Ginecología", "Neurología")

        // Configurar el RecyclerView con la lista original de doctores
        val rvmenu: RecyclerView = view.findViewById(R.id.recyclerViewDoctor)
        rvmenu.layoutManager = LinearLayoutManager(activity)
        val adapterDokter = AdapterDokter(emptyList())
        rvmenu.adapter = adapterDokter

        // Funcionalidad de búsqueda
        val doctorDataEditText = binding.doctorData
        doctorDataEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                // Llamar al método de búsqueda con la consulta escrita
                val searchResults = dbHelper.searchDoctors(query)
                // Actualizar el adaptador del RecyclerView con los resultados
                adapterDokter.updateData(searchResults)
            }

            override fun afterTextChanged(s: Editable?) {
                // No hacer nada
            }
        })

        // Filtrar la lista de doctores por especialidad
        val chipContainer: LinearLayout = view.findViewById(R.id.chipContainer)

        // Agregar dinámicamente chips al contenedor con márgenes
        for (specialty in listOfSpecialties) {
            val chip = Chip(context)
            chip.text = specialty

            // Establecer márgenes programáticamente
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 8, 8, 8) // Ajusta los márgenes si es necesario
            chip.layoutParams = params

            chip.setOnClickListener {
                // Obtener la lista de doctores desde la base de datos filtrados por especialidad
                val doctorsListFromDB = dbHelper.getDoctorsBySpecialty(specialty)
                // Actualizar el RecyclerView con la lista filtrada
                adapterDokter.updateData(doctorsListFromDB)
            }
            // Puedes configurar más propiedades del chip si lo necesitas
            chipContainer.addView(chip)
        }

        // Obtener datos desde la base de datos y actualizar el RecyclerView
        val doctorsListFromDB = dbHelper.getAllDoctors()
        adapterDokter.updateData(doctorsListFromDB)

        // Mostrar "Hola $usuario"
        val textNama: TextView = view.findViewById(R.id.namapasien)
        textNama.text = name

        return view
    }

    companion object {
        // Variable para mostrar "Hola $name"
        var name = "Tes nama"
    }
}
