package com.ferrytech.camerasessionsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferrytech.camerasessionsapp.R
import com.ferrytech.camerasessionsapp.ui.adapters.PhotoAdapter
import com.ferrytech.camerasessionsapp.ui.viewmodel.SessionViewModel
import java.io.File

class SessionDetailFragment : Fragment() {

    companion object {
        private const val ARG_ID = "arg_id"

        fun newInstance(sessionId: Long) = SessionDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_ID, sessionId)
            }
        }
    }

    private val vm: SessionViewModel by viewModels()
    private var sessionId: Long = -1L

    private lateinit var tvName: TextView
    private lateinit var tvMeta: TextView
    private lateinit var rvPhotos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionId = arguments?.getLong(ARG_ID) ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_session_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tvDetailName)
        tvMeta = view.findViewById(R.id.tvDetailMeta)
        rvPhotos = view.findViewById(R.id.rvPhotos)
        rvPhotos.layoutManager = GridLayoutManager(requireContext(), 2)

        vm.getSessionDetails(sessionId) { sessionWithPhotos ->
            requireActivity().runOnUiThread {
                if (sessionWithPhotos == null) return@runOnUiThread

                tvName.text = sessionWithPhotos.session.name
                tvMeta.text = "Age: ${sessionWithPhotos.session.age} • ID: ${sessionWithPhotos.session.sessionId}"

                val photoFiles = sessionWithPhotos.photos.map { photo ->
                    val file = File(
                        requireContext().filesDir,
                        "sessions/session_${sessionId}/${photo.fileName}" // ✅ Fixed path (removed extra space)
                    )
                    if (!file.exists()) {
                        Log.w("SessionDetailFragment", "File not found: ${file.absolutePath}")
                    }
                    file
                }

                rvPhotos.adapter = PhotoAdapter(photoFiles)
            }
        }
    }
}
