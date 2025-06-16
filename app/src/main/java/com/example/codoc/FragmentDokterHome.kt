package com.example.codoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codoc.activity.dokter.ProfileDokterActivity
import com.example.codoc.adapter.AdapterJanjiOnDokter
import com.example.codoc.databinding.FragmentDokterHomeBinding

class FragmentDokterHome : Fragment() {

    private lateinit var binding: FragmentDokterHomeBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDokterHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        dbHelper = DatabaseHelper(requireContext())

        // Mostrar texto de bienvenida al doctor
        val textNama: TextView = view.findViewById(R.id.namadokter)
        textNama.text = name

        // Configurar RecyclerView con los datos de las citas
        setupRecyclerView(view)

        return view
    }

    // Configura el RecyclerView con los datos de citas del doctor
    private fun setupRecyclerView(view: View) {
        val rvMenu: RecyclerView = view.findViewById(R.id.recyclerViewDoctor)
        rvMenu.layoutManager = LinearLayoutManager(activity)

        // Obtener citas desde la base de datos por correo del doctor
        val myJanjiListFromDB = dbHelper.getDataJanjiOnDokter(ProfileDokterActivity.email)
        rvMenu.adapter = AdapterJanjiOnDokter(myJanjiListFromDB)
    }

    companion object {
        var name = "Tes nama" // Valor por defecto del nombre del doctor
    }
}
