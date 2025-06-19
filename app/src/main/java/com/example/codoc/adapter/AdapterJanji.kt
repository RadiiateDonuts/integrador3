package com.example.codoc.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.codoc.R
import com.example.codoc.activity.pasien.EditMyJanjiActivity
import com.example.codoc.model.MyJanjiModel
import com.google.firebase.firestore.FirebaseFirestore

class AdapterJanji(
    private var listDokter: MutableList<MyJanjiModel>,
    private val context: Context
) : RecyclerView.Adapter<AdapterJanji.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nama: TextView = itemView.findViewById(R.id.textNama)
        private val spesialis: TextView = itemView.findViewById(R.id.Spesialis)
        private val jam: TextView = itemView.findViewById(R.id.jamtext)
        private val tanggal: TextView = itemView.findViewById(R.id.tanggaltext)
        private val idJanji: TextView = itemView.findViewById(R.id.idJanji)
        private val editButton: ImageButton = itemView.findViewById(R.id.buttonEdit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(model: MyJanjiModel) {
            nama.text = model.namaDokter
            spesialis.text = model.spesialis
            jam.text = model.jamJanji
            tanggal.text = model.tanggalJanji
            idJanji.text = model.id_janji

            editButton.setOnClickListener {
                val intent = Intent(context, EditMyJanjiActivity::class.java).apply {
                    putExtra("ID_JANJI", model.id_janji)
                    putExtra("TANGGAL_JANJI", model.tanggalJanji)
                    putExtra("JAM_JANJI", model.jamJanji)
                    // Si usas más datos en EditMyJanjiActivity, agrégalos también aquí:
                    putExtra("EMAIL_PASIEN", model.emailPasien)
                    putExtra("EMAIL_DOKTER", model.emailDokter)
                    putExtra("NAMA_DOKTER", model.namaDokter)
                    putExtra("SPESIALIS", model.spesialis)
                    putExtra("KELUHAN", model.keluhan)
                }
                context.startActivity(intent)
            }


            deleteButton.setOnClickListener {
                mostrarDialogoEliminar(model, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.janji_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listDokter[position])
    }

    override fun getItemCount(): Int = listDokter.size

    fun updateData(newList: List<MyJanjiModel>) {
        listDokter = newList.toMutableList()
        notifyDataSetChanged()
    }

    private fun mostrarDialogoEliminar(model: MyJanjiModel, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Eliminar cita")
            .setMessage("¿Deseas eliminar la cita con ${model.namaDokter}?")
            .setPositiveButton("Sí") { _, _ ->
                db.collection("janji")
                    .whereEqualTo("id_janji", model.id_janji)
                    .get()
                    .addOnSuccessListener { result ->
                        for (doc in result) {
                            db.collection("janji").document(doc.id).delete()
                                .addOnSuccessListener {
                                    listDokter.removeAt(position)
                                    notifyItemRemoved(position)
                                    Toast.makeText(context, "Cita eliminada", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error al buscar la cita", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
