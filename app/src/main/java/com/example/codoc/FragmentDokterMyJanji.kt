package com.example.codoc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// Constantes para argumentos del fragmento
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Un fragmento simple.
 * Usa [FragmentDokterMyJanji.newInstance] para crear una instancia de este fragmento.
 */
class FragmentDokterMyJanji : Fragment() {
    // Parámetros del fragmento
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño para este fragmento
        return inflater.inflate(R.layout.fragment_dokter_my_janji, container, false)
    }

    companion object {
        /**
         * Usa este método de fábrica para crear una nueva instancia
         * de este fragmento usando los parámetros proporcionados.
         *
         * @param param1 Parámetro 1.
         * @param param2 Parámetro 2.
         * @return Una nueva instancia del fragmento FragmentDokterMyJanji.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentDokterMyJanji().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
