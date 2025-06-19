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
import com.example.codoc.activity.dokter.ProfileDokterActivity

// Constantes de argumentos (si se usan)
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragmento de configuración del doctor.
 * Usa [FragmentDokterSettings.newInstance] para crear una instancia de este fragmento.
 */
class FragmentDokterSettings : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_dokter_settings, container, false)

        // Ir a "Acerca de nosotros"
        val aboutUs: LinearLayout = view.findViewById(R.id.linearLayout2)
        aboutUs.setOnClickListener {
            showAboutUsDialog()
        }

        // Enviar feedback
        val feedback: LinearLayout = view.findViewById(R.id.linearLayout4)
        feedback.setOnClickListener {
            sendFeedbackEmail()
        }

        // Editar perfil
        val editProfile: LinearLayout = view.findViewById(R.id.linearLayout1)
        editProfile.setOnClickListener {
            val intentSettingActivity = Intent(requireContext(), ProfileDokterActivity::class.java)
            startActivity(intentSettingActivity)
        }

        // Cerrar sesión
// Cerrar sesión
        val logOut: LinearLayout = view.findViewById(R.id.linearLayout5)
        logOut.setOnClickListener {
            // Borrar SharedPreferences del doctor
            val sharedPref = requireActivity().getSharedPreferences("UserDokterData", android.content.Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            // Redirigir a FirstActivity y limpiar la pila
            val intent = Intent(requireContext(), FirstActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        return view
    }

    // Mostrar cuadro de diálogo de "Acerca de nosotros"
    private fun showAboutUsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Acerca de nosotros")
            .setMessage("Somos un equipo de desarrolladores comprometidos en proporcionar la mejor experiencia de usuario para el agendamiento de citas médicas. " +
                    "Nuestra aplicación CRUD (Crear, Leer, Actualizar, Eliminar) fue creada con el objetivo de ofrecer una solución rápida, confiable y fácil de usar para los usuarios que desean agendar una cita con su médico.")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Más información") { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/humberto.rl.47932/"))

                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                } else {
                    // Aquí podrías mostrar un Toast o AlertDialog si no hay navegador
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    // Enviar correo de retroalimentación
    private fun sendFeedbackEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("rolh_2003@outlook.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Retroalimentación")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Estimado equipo,\n\nMe gustaría proporcionar el siguiente comentario:\n\n"
        )

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            // Aquí podrías mostrar un Toast o AlertDialog si no hay app de correo
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentDokterSettings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
