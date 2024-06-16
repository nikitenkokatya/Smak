package com.example.smak.perfil


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.smak.R
import com.example.smak.base.BaseFragmentDialog
import com.example.smak.data.Perfil
import com.example.smak.data.Receta
import com.example.smak.data.Receta.Companion.TAG
import com.example.smak.database.repository.PerfilRepository
import com.example.smak.databinding.FragmentProfileBinding
import com.example.smak.perfil.ui.adapter.CreadasAdapter
import com.example.smak.perfil.ui.adapter.GuardadasAdapter
import com.example.smak.perfil.ui.usecase.CreadasListState
import com.example.smak.perfil.ui.usecase.CreadasListViewModel
import com.example.smak.perfil.ui.usecase.GuardadasViewModel
import com.example.smak.perfil.ui.usecase.PerfilViewModel
import com.example.smak.ui.CreateFragment
import com.example.smak.ui.usecase.ListState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.min


class ProfileFragment : Fragment(), MenuProvider, CreadasAdapter.onClickCreadas,
    GuardadasAdapter.onClickGuardadas {
    private lateinit var mAuth: FirebaseAuth

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterCreadas: CreadasAdapter
    private lateinit var adapterGuardadas: GuardadasAdapter
    private val viewmodel: CreadasListViewModel by viewModels()
    private val viewmodelfav: GuardadasViewModel by viewModels()
    private val perfilViewModel: PerfilViewModel by viewModels()

    private lateinit var imageView: ImageView
    private lateinit var storage: FirebaseStorage
    private lateinit var imageUri: Uri
    private lateinit var base64img: String
    private lateinit var newPhoto: ImageView

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 101
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val REQUEST_PICK_IMAGE = 10001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        viewmodel.getRecetas().observe(viewLifecycleOwner, Observer { recetas ->
            adapterCreadas.submitList(recetas)
            binding.rvcreadas.visibility = VISIBLE
            binding.rvguardadas.visibility = GONE
        })

        viewmodelfav.recetasFavoritas.observe(viewLifecycleOwner, Observer { recetas ->
            adapterGuardadas.submitList(recetas.toList())
            binding.rvcreadas.visibility = GONE
            binding.rvguardadas.visibility = VISIBLE
        })

        viewmodel.getState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is CreadasListState.Success -> onSuccess()
                is CreadasListState.Error -> onNoError()
            }
        })
        setUpToolbar()

        binding.btnnavigate.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_creadas -> {
                    viewmodel.getMisRecetas(binding.txtcreadas)
                    true
                }

                R.id.nav_guardar -> {
                    viewmodelfav.cargarRecetasFavoritas(binding.txtguardadas)
                    true
                }

                else -> false
            }
        }


        initrvCreadas()
        initrvGuardadas()

        binding.rvcreadas.visibility = VISIBLE
        binding.rvguardadas.visibility = GONE

        viewmodel.getMisRecetas(binding.txtcreadas)
        viewmodelfav.cargarRecetasFavoritasSinSuccess(binding.txtguardadas)

        binding.button2.setOnClickListener {
            showEditProfileDialog()
        }

        imageView = binding.imageView
        storage = Firebase.storage

        perfilViewModel.getPerfil(user!!.email!!, {
            binding.imageView.setImageBitmap(
                applyCircularMask(
                    base64ToBitmap(PerfilRepository.userPerfil!!.foto!!)
                )
            )

            (activity as AppCompatActivity).supportActionBar?.title = PerfilRepository.userPerfil!!.nombre

        }, {
            binding.imageView.setImageResource(R.drawable.user)

            val userEmail = user.email ?: ""
            val userNameBeforeAt = userEmail.substringBefore('@')
            (activity as AppCompatActivity).supportActionBar?.title = userNameBeforeAt

        })
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }


    override fun onResume() {
        super.onResume()

        val menuItem = binding.btnnavigate.menu.findItem(R.id.nav_creadas)
        menuItem?.isChecked = true
    }

    fun initrvCreadas() {
        adapterCreadas = CreadasAdapter(this)
        binding.rvcreadas.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvcreadas.adapter = adapterCreadas
    }

    fun initrvGuardadas() {
        adapterGuardadas = GuardadasAdapter(this)
        binding.rvguardadas.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvguardadas.adapter = adapterGuardadas
    }

    private fun setUpToolbar() {
        val menuhost: MenuHost = requireActivity()
        menuhost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.settingsFragment)
                true
            }

            else -> false
        }
    }

    fun onSuccess() {
        binding.imgchef.visibility = GONE
        binding.rvcreadas.visibility = View.VISIBLE
    }

    fun onNoError() {
        binding.imgchef.visibility = VISIBLE
        binding.rvguardadas.visibility = GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClickDetails(receta: Receta) {
        var bundle = Bundle()
        bundle.putParcelable(TAG, receta)

        findNavController().navigate(R.id.action_profileFragment2_to_detailFragment, bundle)
    }


    override fun onClickDetailsC(receta: Receta) {
        var bundle = Bundle()
        bundle.putParcelable(TAG, receta)

        findNavController().navigate(R.id.action_profileFragment2_to_detailFragment, bundle)
    }


    override fun userOnLongClickC(receta: Receta): Boolean {
        val dialog = BaseFragmentDialog.newInstance("Atención", "¿Seguro que quieres borrar?")

        dialog.show((context as AppCompatActivity).supportFragmentManager, ContentValues.TAG)

        dialog.parentFragmentManager.setFragmentResultListener(
            BaseFragmentDialog.request, viewLifecycleOwner
        ) { _, build ->
            val result = build.getBoolean(BaseFragmentDialog.result)
            if (result) {
                viewmodel.borrarReceta(receta)
                viewmodel.getMisRecetas(binding.txtcreadas)
            }
        }
        return true
    }

    private fun showEditProfileDialog() {
        val dialogView = layoutInflater.inflate(R.layout.editarperfil_layout, null)

        val alertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext()).setView(dialogView).setCancelable(true)

        val dialog = alertDialogBuilder.create()
        dialog.show()

        dialogView.findViewById<ImageButton>(R.id.btngaleria).setOnClickListener {
            openGallery()
        }

        dialogView.findViewById<ImageButton>(R.id.btnCamara).setOnClickListener {
            checkPermissionAndOpenCamera()
        }

        dialogView.findViewById<Button>(R.id.btnsave).setOnClickListener {
            val userName = dialogView.findViewById<TextView>(R.id.tieUserName).text.toString()

            if (userName.isEmpty() || base64img.isEmpty()) {
                Toast.makeText(requireContext(), "Debes ingresar un nombre y elegir una foto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            perfilViewModel.guardarPerfil(
                userName, base64img
            )

            perfilViewModel.getPerfil(mAuth.currentUser!!.email!!, {
                binding.imageView.setImageBitmap(
                    applyCircularMask(
                        base64ToBitmap(PerfilRepository.userPerfil!!.foto!!)
                    )
                )
                (activity as AppCompatActivity).supportActionBar?.title = PerfilRepository.userPerfil!!.nombre
            }, {
                binding.imageView.setImageResource(R.drawable.user)
                val userNameBeforeAt = mAuth.currentUser!!.email!!.substringBefore('@')
                (activity as AppCompatActivity).supportActionBar?.title  = userNameBeforeAt
            })

            dialog.dismiss()
        }

        base64img = ""
        newPhoto = dialogView.findViewById(R.id.imgEditPerfil)
    }

    private fun applyCircularMask(bitmap: Bitmap): Bitmap {
        val size = min(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)
        return output
    }

    private fun checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE
            )
        } else {
            openCamera()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_IMAGE)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )!!

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    val uri = data?.data

                    if (uri != null) {
                        val base64 = obtenerBase64DesdeUri(requireContext(), uri)

                        base64img = base64

                        base64.let {
                            base64img = it
                            newPhoto.setImageBitmap(applyCircularMask(getBitmapFromUri(uri)!!))
                        }
                    }
                }
            }
        }
    }

    fun getBitmapFromUri(uri: Uri): Bitmap? {
        var inputStream: InputStream? = null
        try {
            inputStream = requireContext().contentResolver.openInputStream(uri)

            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }

    fun obtenerBase64DesdeUri(context: Context, uri: Uri): String {
        var base64String: String? = null
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            val buffer = ByteArrayOutputStream()
            val bufferSize = 1024
            val bufferArray = ByteArray(bufferSize)
            var len: Int
            while (inputStream!!.read(bufferArray).also { len = it } != -1) {
                buffer.write(bufferArray, 0, len)
            }
            val data = buffer.toByteArray()
            base64String = Base64.encodeToString(data, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return base64String!!
    }
}




