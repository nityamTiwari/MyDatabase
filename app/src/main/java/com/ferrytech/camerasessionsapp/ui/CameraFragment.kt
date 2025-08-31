package com.ferrytech.camerasessionsapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.ferrytech.camerasessionsapp.R
import com.ferrytech.camerasessionsapp.ui.viewmodel.SessionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    companion object {
        private const val ARG_SESSION_ID = "arg_session_id"
        fun newInstance(sessionId: Long) = CameraFragment().apply {
            arguments = Bundle().apply { putLong(ARG_SESSION_ID, sessionId) }
        }
    }

    // Correct ViewModel initialization with AndroidViewModelFactory
    private val vm: SessionViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    private var sessionId: Long = -1L
    private lateinit var previewView: PreviewView
    private lateinit var btnCapture: Button
    private lateinit var btnSwitch: Button
    private lateinit var btnDone: Button
    private var imageCapture: ImageCapture? = null
    private var cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var cameraExecutor: ExecutorService

    private val permitLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera()
            else Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionId = arguments?.getLong(ARG_SESSION_ID) ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        previewView = view.findViewById(R.id.previewView)
        btnCapture = view.findViewById(R.id.btnCapture)
        btnSwitch = view.findViewById(R.id.btnSwitch)
        btnDone = view.findViewById(R.id.btnDone)

        cameraExecutor = Executors.newSingleThreadExecutor()

        btnCapture.setOnClickListener { takePhoto() }
        btnSwitch.setOnClickListener { toggleCamera() }
        btnDone.setOnClickListener { parentFragmentManager.popBackStack() }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            permitLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun sessionFolder(): File {
        val folder = File(requireContext().filesDir, "sessions/session_$sessionId")
        if (!folder.exists()) folder.mkdirs()
        return folder
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(requireActivity(), cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("CameraFragment", "Camera binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun toggleCamera() {
        cameraSelector = if (cameraSelector == androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA)
            androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
        else
            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
        startCamera()
    }

    private fun takePhoto() {
        val ic = imageCapture ?: return
        val filename = SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(Date()) + ".jpg"
        val file = File(sessionFolder(), filename)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        ic.takePicture(outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                CoroutineScope(Dispatchers.IO).launch {
                    vm.addPhoto(sessionId, filename)
                }
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Saved: $filename", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }
}
