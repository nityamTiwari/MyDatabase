package com.ferrytech.camerasessionsapp.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferrytech.camerasessionsapp.R
import com.ferrytech.camerasessionsapp.ui.adapters.SessionAdapter
import com.ferrytech.camerasessionsapp.ui.viewmodel.SessionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class SessionListFragment : Fragment() {
    private val vm: SessionViewModel by viewModels()
    private lateinit var rvSessions: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var btnNewSession: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_session_list, container,
            false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvSessions = view.findViewById(R.id.rvSessions)
        etSearch = view.findViewById(R.id.etSearch)
        btnNewSession = view.findViewById(R.id.btnNewSession)
        rvSessions.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SessionAdapter { session ->
// open detail
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SessionDetailFragment.newInstance(session.sessionId))
                .addToBackStack(null)
                .commit()
        }
        rvSessions.adapter = adapter
        btnNewSession.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SessionCreateFragment())
                .addToBackStack(null)
                .commit()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            vm.sessions.collectLatest { list -> adapter.submitList(list) } }

// search on text change (simple)
        etSearch.setOnEditorActionListener { v, actionId, event ->
            vm.search(v.text.toString())
            true
        }
// show search results when available
        viewLifecycleOwner.lifecycleScope.launch {
            vm.searchResults.collectLatest { results ->
// transform SessionWithPhotos to Session (for simple list) - we display name and id
                        adapter.submitList(results.map { it.session })
            }
        }
        vm.loadAllSessions()
    }
}
