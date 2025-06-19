package com.example.codoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codoc.adapter.AdapterJanjiOnDokter
import com.example.codoc.databinding.FragmentDokterHomeBinding
import com.example.codoc.model.MyJanjiModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class FragmentDokterHome : Fragment() {

    private lateinit var binding: FragmentDokterHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDokterHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        mostrarNombreDoctor(view)
        setupRecyclerView(view)

        return view
    }

    private fun mostrarNombreDoctor(view: View) {
        val sharedPref = requireActivity().getSharedPreferences("UserDokterData", android.content.Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        if (email != null) {
            val key = email.replace(".", "_")
            val ref = FirebaseDatabase.getInstance().getReference("akundokter").child(key)

            ref.get().addOnSuccessListener { snapshot ->
                val nama = snapshot.child("name").value?.toString() ?: "Doctor"
                val textNama: TextView = view.findViewById(R.id.namadokter)
                textNama.text = "Hola, $nama!"
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewDoctor)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val janjiList = mutableListOf<MyJanjiModel>()
        val adapter = AdapterJanjiOnDokter(janjiList)
        recyclerView.adapter = adapter

        val sharedPref = requireActivity().getSharedPreferences("UserDokterData", android.content.Context.MODE_PRIVATE)
        val emailDokter = sharedPref.getString("email", null)

        if (emailDokter != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("janji")
                .whereEqualTo("emailDokter", emailDokter)
                .get()
                .addOnSuccessListener { documents ->
                    janjiList.clear()
                    for (doc in documents) {
                        val model = doc.toObject(MyJanjiModel::class.java)
                        janjiList.add(model)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al cargar citas", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
