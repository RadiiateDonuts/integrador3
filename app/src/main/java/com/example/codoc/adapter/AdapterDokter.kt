package com.example.codoc.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codoc.R
import com.example.codoc.activity.pasien.TambahJanjiActivity
import com.example.codoc.model.ProfileDokterModel

class AdapterDokter(var listDokter: List<ProfileDokterModel>) : RecyclerView.Adapter<AdapterDokter.ViewHolder>() {

    fun updateData(newList: List<ProfileDokterModel>) {
        listDokter = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val email: TextView = view.findViewById(R.id.emailDokter)
        val nama: TextView = view.findViewById(R.id.textNama)
        val spesialis: TextView = view.findViewById(R.id.Spesialis)
        val rumahSakit: TextView = view.findViewById(R.id.rumahSakit)
        val button: Button = view.findViewById(R.id.book_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doctor_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dokter = listDokter[position]
        holder.email.text = dokter.email
        holder.nama.text = dokter.nama
        holder.spesialis.text = dokter.spesialis
        holder.rumahSakit.text = dokter.alamat

        holder.button.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, TambahJanjiActivity::class.java).apply {
                putExtra("namaDokter", dokter.nama)
                putExtra("emailDokter", dokter.email)
                putExtra("spesialis", dokter.spesialis)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listDokter.size
}
