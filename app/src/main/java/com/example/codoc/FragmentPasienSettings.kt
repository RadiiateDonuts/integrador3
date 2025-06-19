package com.example.codoc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.codoc.activity.FirstActivity
import com.example.codoc.activity.pasien.ProfilePasienActivity
import com.google.firebase.auth.FirebaseAuth

class FragmentPasienSettings : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pasien_settings, container, false)

        // Acción: Ver perfil
        view.findViewById<LinearLayout>(R.id.linearLayout1).setOnClickListener {
            val intent = Intent(requireContext(), ProfilePasienActivity::class.java)
            startActivity(intent)
        }

        // Acción: Acerca de nosotros
        view.findViewById<LinearLayout>(R.id.linearLayout2).setOnClickListener {
            showAboutUsDialog()
        }

        // Acción: Enviar comentarios
        view.findViewById<LinearLayout>(R.id.linearLayout4).setOnClickListener {
            sendFeedbackEmail()
        }

        // Acción: Cerrar sesión
        view.findViewById<LinearLayout>(R.id.linearLayout5).setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Firebase logout
            val intent = Intent(requireContext(), FirstActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }

    private fun sendFeedbackEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:rolh_2003@outlook.com")
            putExtra(Intent.EXTRA_SUBJECT, "Comentarios")
            putExtra(
                Intent.EXTRA_TEXT,
                "Estimado equipo,\n\nQuisiera proporcionar los siguientes comentarios:\n\n"
            )
        }

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun showAboutUsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Acerca de nosotros")
            .setMessage(
                "Somos un equipo comprometido con ofrecer la mejor experiencia en el registro de citas médicas.\n\n" +
                        "Codoc fue creado como una solución confiable, rápida y fácil de usar para pacientes y médicos."
            )
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("Más información") { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/humberto.rl.47932/"))
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }
            .create()
            .show()
    }
}
