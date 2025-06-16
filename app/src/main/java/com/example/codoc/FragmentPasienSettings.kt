package com.example.codoc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.codoc.activity.FirstActivity
import com.example.codoc.activity.pasien.ProfilePasienActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentPasienSettings : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_pasien_settings, container, false)

        val aboutUs: LinearLayout = view.findViewById(R.id.linearLayout2)
        aboutUs.setOnClickListener {
            showAboutUsDialog()
        }

        val feedback: LinearLayout = view.findViewById(R.id.linearLayout4)
        feedback.setOnClickListener {
            sendFeedbackEmail()
        }

        val editProfile: LinearLayout = view.findViewById(R.id.linearLayout1)
        editProfile.setOnClickListener {
            val intentSettingActivity = Intent(requireContext(), ProfilePasienActivity::class.java)
            startActivity(intentSettingActivity)
        }

        val logOut: LinearLayout = view.findViewById(R.id.linearLayout5)
        logOut.setOnClickListener {
            val intentSettingActivity = Intent(requireContext(), FirstActivity::class.java)
            startActivity(intentSettingActivity)
        }

        return view
    }

    private fun sendFeedbackEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("rolh_2003@outlook.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Comentarios")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Estimado equipo,\n\nQuisiera proporcionar los siguientes comentarios:\n\n"
        )

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun showAboutUsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Acerca de nosotros")
            .setMessage(
                "Somos un equipo de desarrollo comprometido con ofrecer la mejor experiencia de usuario en el registro de citas médicas. " +
                        "Nuestra aplicación CRUD (Crear, Leer, Actualizar, Eliminar) fue creada con el objetivo de brindar una solución confiable, rápida y fácil de usar para usuarios que desean agendar citas con sus médicos."
            )
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Más información") { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/humberto.rl.47932/"))
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentPasienSettings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
