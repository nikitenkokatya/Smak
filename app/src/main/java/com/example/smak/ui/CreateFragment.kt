package com.example.smak.ui

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smak.MainActivity
import com.example.smak.data.Receta
import com.example.smak.databinding.FragmentCreateBinding
import com.example.smak.ui.adapter.PhotoAdapter
import com.example.smak.ui.usecase.CreateState
import com.example.smak.ui.usecase.CreateViewModel
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.smak.perfil.ProfileFragment
import com.example.smak.utils.createNotificationChannel
import com.example.smak.utils.sendNotification


class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: CreateViewModel by viewModels()
    lateinit var txtWatcher: LogInTetxWatcher

    private lateinit var photoAdapter: PhotoAdapter
    private val selectedPhotos = mutableListOf<String>()

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

        initrv()


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
        }*/


        binding.btnfotos.setOnClickListener {
            abrirGaleriaParaVariasFotos()
        }


        val tipos = arrayOf("Desayuno", "Almuerzo", "Cena", "Postre" )
        val adapterTipos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tipos)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sptipo.adapter = adapterTipos

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
                CreateState.NombreEmptyError -> onNombreError()
                CreateState.IngredientesEmptyError -> onFormatError()
                CreateState.PasosError -> onPasosError()
                CreateState.TiempoError -> onTiempoError()
                CreateState.ImagenesEmptyError -> onImageError()
                is CreateState.Error -> onError(it.ex)
                is CreateState.Success<*> -> onSuccess(it.data as Receta)
                else -> {}
            }
        })
    }

    fun initrv(){
        photoAdapter = PhotoAdapter(selectedPhotos)
        binding.rvImagenes.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvImagenes.adapter = photoAdapter
    }

    fun onNombreError() {
        binding.tilnombre.error = "Introduce el nombre"
        binding.tilnombre.requestFocus()
    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavVisible()
    }

    fun onFormatError() {
        binding.tilingredientes.error = "Formato incorrecto"
        binding.tilingredientes.requestFocus()
    }

    fun onPasosError() {
        binding.tilpasos.error = "Introduce los pasos"
        binding.tilpasos.requestFocus()
    }

    fun onTiempoError() {
        binding.tiltiempo.error = "Introduce el tiempo"
        binding.tiltiempo.requestFocus()
    }
    fun  onImageError(){
        Toast.makeText(requireContext(), "No hay imagenes", Toast.LENGTH_SHORT).show()
    }
    fun onError(exception: Exception) {
        Toast.makeText(requireContext(), "Error al crear", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun onSuccess(receta: Receta) {
        Toast.makeText(requireContext(), "Receta guardada correctamente", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()

        val intent = Intent("com.example.receta")
        intent.putExtra("nombre_receta", receta.nombre)
        intent.putExtra("autor", receta.autor)
        activity?.sendBroadcast(intent)
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
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_SELECCION_UNA_IMAGEN)
    }

    private fun abrirGaleriaParaVariasFotos() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_CODE_SELECCION_VARIAS_IMAGENES)
    }

    private fun abrirGaleria() {
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
        startActivityForResult(intent, REQUEST_CODE_SELECCION_VARIAS_IMAGENES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SELECCION_UNA_IMAGEN -> {
                    val uri = data?.data

                    if (uri != null) {
                        val base64 = obtenerBase64DesdeUri(requireContext(), uri)
                        selectedPhotos.add(base64)

                        base64.let {
                            viewmodel.addImage(it)
                        }

                        photoAdapter.notifyDataSetChanged()
                    }
                }

                REQUEST_CODE_SELECCION_VARIAS_IMAGENES -> {
                    data?.clipData?.let { clipData ->
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            val base64 = obtenerBase64DesdeUri(requireContext(), uri)

                            selectedPhotos.add(base64)

                            base64.let {
                                viewmodel.addImage(it)
                            }
                        }
                        photoAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun obtenerBase64DesdeUri(context: Context, uri: Uri): String {
        var base64String: String? = null
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)

            // Decodificar la imagen en un Bitmap
            val options = BitmapFactory.Options()
            options.inSampleSize = calcularSampleSize(context, uri)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)

            // Convertir el Bitmap en un array de bytes
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream) // Ajusta la calidad según sea necesario
            val byteArray = byteArrayOutputStream.toByteArray()

            // Convertir el array de bytes en una cadena base64
            base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return base64String ?: ""
    }

    fun calcularSampleSize(context: Context, uri: Uri): Int {
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)

            // Solo necesitamos la dimensión de la imagen para calcular el Sample Size
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            val width = options.outWidth
            val height = options.outHeight
            var inSampleSize = 1

            // Calcular el Sample Size
            if (width > 1024 || height > 1024) { // Ajusta estos valores según tus necesidades
                val halfWidth = width / 2
                val halfHeight = height / 2
                while ((halfWidth / inSampleSize) >= 1024 && (halfHeight / inSampleSize) >= 1024) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return 1
    }

    companion object {
        private const val REQUEST_CODE_SELECCION_IMAGENES = 123
        private const val REQUEST_CODE_SELECCION_UNA_IMAGEN = 124
        private const val REQUEST_CODE_SELECCION_VARIAS_IMAGENES = 125
    }
}
