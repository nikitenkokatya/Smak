package com.example.smak.buscar

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smak.R
import com.example.smak.data.Receta
import com.example.smak.databinding.FragmentBuscadorBinding
import com.example.smak.ui.adapter.RecetaAdapter
import com.example.smak.ui.usecase.ListState
import com.example.smak.ui.usecase.ListViewModel
import java.util.Locale


class BuscadorFragment : Fragment(), RecetaAdapter.onClick {
    private var _binding: FragmentBuscadorBinding? = null
    private val binding get() = _binding!!
    lateinit var recetaAdapter: RecetaAdapter
    private lateinit var speechRecognizer: SpeechRecognizer
    private val viewmodel: ListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuscadorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRV()

        val tipo = arguments?.getString("tipo") ?: ""

        viewmodel.recetas.observe(viewLifecycleOwner, Observer { recetas ->
            recetaAdapter.submitList(recetas)
            if (recetas.isEmpty()) {
                showImageBusqueda()
            } else {
                hideImageBusqueda()
            }
        })

        viewmodel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ListState.Success -> onSuccess()
                is ListState.Error -> onNoError()
                is ListState.Loading -> showProgressBar(state.value)
            }
        }

        binding.srvbuscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewmodel.getAllRecetasTipo(tipo, newText ?: "")
                return false
            }
        })

        viewmodel.getAllRecetasTipo(tipo, "")

        binding.btnAudio.setOnClickListener {
            Log.d("YourFragment", "Botón de audio clickeado")
            startVoiceInput()
        }
    }
    private fun showProgressBar(value: Boolean) {
        binding.imgbusqueda.visibility = if (value) VISIBLE else GONE
    }

    private fun onSuccess() {
        hideImageBusqueda()
    }

    private fun onNoError() {
        showImageBusqueda()
    }

    private fun showImageBusqueda() {
        binding.imgbusqueda.visibility = VISIBLE
        binding.rvbusqueda.visibility = GONE
    }

    private fun hideImageBusqueda() {
        binding.imgbusqueda.visibility = GONE
        binding.rvbusqueda.visibility = VISIBLE
    }
    fun initRV() {
        recetaAdapter = RecetaAdapter( this)
        binding.rvbusqueda.layoutManager = LinearLayoutManager(requireContext())
        binding.rvbusqueda.adapter = recetaAdapter
    }

    private fun startVoiceInput() {
        if (SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}

                override fun onResults(results: Bundle?) {
                    val voiceResults =
                        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val searchText = voiceResults?.get(0) ?: ""
                    binding.srvbuscador.setQuery(searchText, true)
                }
            })

            val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            speechRecognizer.startListening(speechRecognizerIntent)
        } else {
            Toast.makeText(
                requireContext(),
                "El reconocimiento de voz no está disponible en este dispositivo",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClickDetails(receta: Receta) {
        var bundle = Bundle()
        bundle.putParcelable(Receta.TAG, receta)

        findNavController().navigate(R.id.action_buscadorFragment2_to_detailFragment, bundle)
    }

    override fun userOnLongClick(receta: Receta): Boolean {
        return true
    }

    override fun onCommentButtonClick(receta: Receta) {
        var bundle = Bundle()
        bundle.putParcelable(Receta.TAG, receta)

        findNavController().navigate(R.id.action_buscadorFragment2_to_comentariosFragment, bundle)
    }
}