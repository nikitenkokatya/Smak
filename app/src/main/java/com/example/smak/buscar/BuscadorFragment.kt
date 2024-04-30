package com.example.smak.buscar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smak.api.SpoonacularApiClient
import com.example.smak.buscar.adapter.BusquedaAdapter
import com.example.smak.data.RecetaAPI
import com.example.smak.databinding.FragmentBuscadorBinding
import org.json.JSONObject
import java.util.Locale


class BuscadorFragment : Fragment(), BusquedaAdapter.onClick {

    val apiKey = "cbac8ca59ade4d04b3bd2ab780365021"
    val client = SpoonacularApiClient()


    private var _binding: FragmentBuscadorBinding? = null
    private val binding get() = _binding!!
    lateinit var recetaAdapter: BusquedaAdapter
    private var recipeList: List<RecetaAPI> = emptyList()
    private lateinit var speechRecognizer: SpeechRecognizer


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

        binding.srvbuscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchRecipes(it)
                }

                return false
            }
        })

        searchRecipes("")

        binding.btnAudio.setOnClickListener {
            Log.d("YourFragment", "Botón de audio clickeado")
            startVoiceInput()
        }
    }

    fun searchRecipes(query: String) {
        client.searchRecipes(query, 50, apiKey) { response ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                recipeList = parseJson(response!!)
                recetaAdapter.submitList(recipeList)
            }
        }
    }

    fun initRV() {
        recetaAdapter = BusquedaAdapter(recipeList, this)
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
                        val voiceResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val searchText = voiceResults?.get(0) ?: ""
                        binding.srvbuscador.setQuery(searchText, true)
                    }                })

                val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                speechRecognizer.startListening(speechRecognizerIntent)
        } else {
            Toast.makeText(requireContext(), "El reconocimiento de voz no está disponible en este dispositivo", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun parseJson(jsonString: String): List<RecetaAPI> {
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("results")
        val recipeList = mutableListOf<RecetaAPI>()

        for (i in 0 until jsonArray.length()) {
            val itemObject = jsonArray.getJSONObject(i)
            val id = itemObject.getInt("id")
            val title = itemObject.getString("title")
            val image = itemObject.getString("image")

            val recipe = RecetaAPI(id, title, image)
            recipeList.add(recipe)
        }

        return recipeList
    }

    override fun onClickDetails(receta: RecetaAPI) {
       val url = "https://api.spoonacular.com/recipes/" + receta.id + "/information?apiKey=" + apiKey

        Log.d("a", url)
       /* val baseUrl = "https://api.spoonacular.com/recipes/" + receta.id + "/information?"
        val translatedBaseUrl = translateString(baseUrl, "en", "es")
        val url = "$translatedBaseUrl&apiKey=$apiKey"

        Log.d("a", url)*/
    }

   /* fun translateJson(jsonString: String): String {
        val translatedText = translateString(jsonString, "en", "es")
        return translatedText ?: jsonString
    }

    fun translateString(textToTranslate: String?, sourceLang: String, targetLang: String): String? {
        return TraductorUtil().translateString(textToTranslate, sourceLang, targetLang)
    }*/
    override fun userOnLongClick(receta: RecetaAPI): Boolean {
        return true
    }

}