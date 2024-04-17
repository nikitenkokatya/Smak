package com.example.smak.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smak.data.Receta
import com.example.smak.databinding.FragmentCreateBinding
import com.example.smak.ui.adapter.PhotoAdapter
import com.example.smak.ui.adapter.RecetaAdapter
import com.example.smak.ui.usecase.CreateState
import com.example.smak.ui.usecase.CreateViewModel
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception


class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: CreateViewModel by viewModels()
    lateinit var txtWatcher: LogInTetxWatcher


    private lateinit var photoAdapter: PhotoAdapter
    private val selectedPhotos = mutableListOf<Uri>()

    private val selectedVideos = mutableListOf<Uri>()

    private var MAX_PHOTOS = 10
    private var MAX_VIDEOS = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoAdapter = PhotoAdapter(selectedPhotos)
        binding.rvImagenes.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvImagenes.adapter = photoAdapter

        // Listener para el botón de agregar foto
       /* binding.btnAgregarImagen.setOnClickListener {
            //abrirGaleria()
            abrirGaleriaParaUnaFoto()
        }*/
        binding.btnunafoto.setOnClickListener {
                  //abrirGaleria()
                  abrirGaleriaParaUnaFoto()
              }
        /*binding.btnSeleccionarUnaFoto.setOnClickListener {
            abrirGaleriaParaUnaFoto()
        }

        // Configurar el botón para seleccionar varias fotos
        binding.btnSeleccionarVariasFotos.setOnClickListener {
            abrirGaleriaParaVariasFotos()
        }*/


        val ciudades = arrayOf("Malaga", "Cadiz", "Barcelona")
        val adapterCiudades =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ciudades)
        adapterCiudades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sptipo.adapter = adapterCiudades

        txtWatcher = LogInTetxWatcher(binding.tilnombre)
        binding.tienombre.addTextChangedListener(txtWatcher)

        txtWatcher = LogInTetxWatcher(binding.tilingredientes)
        binding.tieingredientes.addTextChangedListener(txtWatcher)

        txtWatcher = LogInTetxWatcher(binding.tilpasos)
        binding.tiepasos.addTextChangedListener(txtWatcher)

        txtWatcher = LogInTetxWatcher(binding.tiltiempo)
        binding.tietiempo.addTextChangedListener(txtWatcher)


        binding.btnGuardarReceta.setOnClickListener {
            viewmodel.tipo = binding.sptipo.selectedItem.toString()
            viewmodel.validateCredentials()
        }
        viewmodel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                CreateState.NombreEmptyError -> onCodeError()
                CreateState.IngredientesEmptyError -> onFormatError()
                CreateState.PasosError -> onFechaError()
                CreateState.TiempoError -> onTimpoError()
                is CreateState.Error -> onError(it.ex)
                is CreateState.Success<*> -> onSuccess(it.data as Receta)
            }
        })
    }

    fun onCodeError() {
        binding.tilnombre.error = "Introduce el codigo"
        binding.tilnombre.requestFocus()
    }

    fun onFormatError() {
        binding.tilingredientes.error = "Formato incorrecto"
        binding.tilingredientes.requestFocus()
    }

    fun onFechaError() {
        binding.tilpasos.error = "Formato incorrecto"
        binding.tilpasos.requestFocus()
    }

    fun onTimpoError() {
        binding.tiltiempo.error = "Formato incorrecto"
        binding.tiltiempo.requestFocus()
    }

    fun onError(exception: Exception) {
        Toast.makeText(requireContext(), "Error al crear", Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun onSuccess(receta: Receta) {

        // findNavController().popBackStack()
        Toast.makeText(requireContext(), "Receta guardada correctamente", Toast.LENGTH_SHORT).show()

    }


    open inner class LogInTetxWatcher(val tilerror: TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            tilerror.error = null
        }

    }

    private fun abrirGaleriaParaUnaFoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_SELECCION_UNA_IMAGEN)
    }

    // Método para abrir la galería y permitir seleccionar varias fotos
    private fun abrirGaleriaParaVariasFotos() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Permitir múltiples fotos
        startActivityForResult(intent, REQUEST_CODE_SELECCION_VARIAS_IMAGENES)
    }

    private fun abrirGaleria() {
        // Verificar si ya se alcanzó el límite de fotos
        if (selectedPhotos.size >= MAX_PHOTOS) {
            Toast.makeText(
                requireContext(),
                "¡Se ha alcanzado el límite de fotos!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        startActivityForResult(intent, REQUEST_CODE_SELECCION_IMAGENES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SELECCION_UNA_IMAGEN -> {
                    // Manejar la selección de una sola imagen
                    val uri = data?.data
                    if (uri != null) {
                        // Agregar la URI al arreglo de fotos seleccionadas
                        selectedPhotos.clear()
                        selectedPhotos.add(uri)
                        photoAdapter.notifyDataSetChanged()
                    }
                }

                REQUEST_CODE_SELECCION_VARIAS_IMAGENES -> {
                    // Manejar la selección de varias imágenes
                    data?.clipData?.let { clipData ->
                        // Limpiar la lista de fotos seleccionadas
                        selectedPhotos.clear()
                        // Agregar las URIs de las imágenes seleccionadas al arreglo
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            selectedPhotos.add(uri)
                        }
                        photoAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_SELECCION_IMAGENES = 123
        private const val REQUEST_CODE_SELECCION_UNA_IMAGEN = 124
        private const val REQUEST_CODE_SELECCION_VARIAS_IMAGENES = 125
    }
}
