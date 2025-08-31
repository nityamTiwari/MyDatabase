package com.ferrytech.camerasessionsapp.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ferrytech.camerasessionsapp.R
import com.ferrytech.camerasessionsapp.ui.viewmodel.SessionViewModel
class SessionCreateFragment : Fragment() {
    private val vm: SessionViewModel by viewModels()
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var btnStart: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_session_create, container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        etName = view.findViewById(R.id.etSessionName)
        etAge = view.findViewById(R.id.etSessionAge)
        btnStart = view.findViewById(R.id.btnStartSession)
        btnStart.setOnClickListener {
            val name = etName.text.toString().trim()
            val age = etAge.text.toString().toIntOrNull()
            if (name.isEmpty() || age == null) {
                Toast.makeText(requireContext(), "Enter valid name and age",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.createSession(name, age) { sessionId ->
// open camera for this sessionId
                requireActivity().runOnUiThread {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container,
                            CameraFragment.newInstance(sessionId))
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }
}
