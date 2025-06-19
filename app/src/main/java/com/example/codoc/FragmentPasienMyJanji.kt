package com.example.codoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codoc.adapter.AdapterJanji
import com.example.codoc.databinding.FragmentPasienMyjanjiBinding
import com.example.codoc.model.MyJanjiModel
import com.google.firebase.firestore.FirebaseFirestore

class FragmentPasienMyJanji : Fragment() {

    private lateinit var binding: FragmentPasienMyjanjiBinding
    private lateinit var adapter: AdapterJanji
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasienMyjanjiBinding.inflate(inflater, container, false)

        setupRecyclerView()
        obtenerCitasDesdeFirestore()

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerViewDoctor
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterJanji(mutableListOf(), requireContext())
        recyclerView.adapter = adapter
    }

    private fun obtenerCitasDesdeFirestore() {
        val emailPaciente = com.example.codoc.activity.pasien.ProfilePasienActivity.email

        firestore.collection("janji")
            .whereEqualTo("emailPasien", emailPaciente)
            .get()
            .addOnSuccessListener { result ->
                val listaCitas = result.mapNotNull { doc ->
                    doc.toObject(MyJanjiModel::class.java).copy(id_janji = doc.id)
                }
                adapter.updateData(listaCitas)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar citas", Toast.LENGTH_SHORT).show()
            }
    }
}
