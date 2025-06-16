package com.example.codoc.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codoc.DatabaseHelper
import com.example.codoc.R
import com.example.codoc.activity.pasien.EditMyJanjiActivity
import com.example.codoc.activity.pasien.HomePasienActivity
import com.example.codoc.model.MyJanjiModel

class AdapterJanji(private var listDokter: List<MyJanjiModel>) :
    RecyclerView.Adapter<AdapterJanji.ViewHolder>() {

    // Actualizar la lista de citas y refrescar el RecyclerView
    fun updateData(newList: List<MyJanjiModel>) {
        listDokter = newList
        notifyDataSetChanged()
    }

    // ViewHolder que representa cada tarjeta de cita (janji)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nama: TextView = itemView.findViewById(R.id.textNama)
        private val spesialis: TextView = itemView.findViewById(R.id.Spesialis)
        private val jam: TextView = itemView.findViewById(R.id.jamtext)
        private val tanggal: TextView = itemView.findViewById(R.id.tanggaltext)
        private val idJanji: TextView = itemView.findViewById(R.id.idJanji)
        private val editButton: ImageButton = itemView.findViewById(R.id.buttonEdit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.buttonDelete)

        // Asignar datos del modelo a los elementos de la vista
        fun bind(modelDokter: MyJanjiModel) {
            nama.text = modelDokter.namaDokter
            spesialis.text = modelDokter.spesialis
            jam.text = modelDokter.jamJanji
            tanggal.text = modelDokter.tanggalJanji
            idJanji.text = modelDokter.id_janji

            // Acción del botón Editar
            editButton.setOnClickListener {
                handleEditButtonClick(
                    modelDokter.id_janji,
                    modelDokter.tanggalJanji,
                    modelDokter.jamJanji,
                    itemView.context
                )
            }

            // Acción del botón Eliminar
            deleteButton.setOnClickListener {
                handleDeleteButtonClick(modelDokter.id_janji, itemView.context)
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

    override fun getItemCount(): Int {
        return listDokter.size
    }

    // Manejar la acción del botón Editar
    private fun handleEditButtonClick(id: String, tanggalJanji: String, jamJanji: String, context: Context) {
        Log.d("AdapterJanji", "Botón de edición presionado para ID: $id")
        val intent = Intent(context, EditMyJanjiActivity::class.java)
        intent.putExtra("ID_JANJI", id)
        intent.putExtra("TANGGAL_JANJI", tanggalJanji)
        intent.putExtra("JAM_JANJI", jamJanji)
        context.startActivity(intent)
    }

    // Manejar la acción del botón Eliminar
    private fun handleDeleteButtonClick(id: String, context: Context) {
        Log.d("AdapterJanji", "Botón de eliminación presionado para ID: $id")
        val intent = Intent(context, HomePasienActivity::class.java)
        context.startActivity(intent)
        val dbHelper = DatabaseHelper(context)
        dbHelper.deleteJanji(id)
        Log.d("AdapterJanji", "Eliminando cita con ID: $id")
        notifyDataSetChanged()
    }
}
