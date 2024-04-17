package com.example.smak.compras

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smak.R
import com.example.smak.base.BaseFragmentDialog
import com.example.smak.compras.adapter.ComprasAdapter
import com.example.smak.compras.data.Compras
import com.example.smak.compras.ui.ComprasCreateState
import com.example.smak.compras.ui.ComprasCreateViewModel
import com.example.smak.compras.ui.ComprasListViewModel
import com.example.smak.databinding.FragmentComprasBinding
import com.example.smak.ui.usecase.ListState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Exception


class ComprasFragment : Fragment(), ComprasAdapter.onClickListener {
    private var _binding: FragmentComprasBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingredientesAdapter: ComprasAdapter

    private val viewmodel: ComprasCreateViewModel by viewModels()
    private val listviewmodel: ComprasListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComprasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initrv()

        binding.btnguardarCompra.setOnClickListener {
            mostrarDialogoAgregarIngrediente()
        }

        listviewmodel.getComprasList().observe(viewLifecycleOwner, Observer { listaIngredientes ->
            ingredientesAdapter.submitList(listaIngredientes)
        })

        listviewmodel.obtenerIngredientes()

        listviewmodel.getState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ListState.Success -> onSuccessList()
                is ListState.Error -> onNoErrorList()
            }
        })

        viewmodel.getState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                ComprasCreateState.NombreEmptyError -> mostrarMensajeError("Nombre de ingrediente vacío")
                is ComprasCreateState.Success<*> -> {
                    val ingredienteAgregado = state.data as String
                }
                is ComprasCreateState.Error -> mostrarMensajeError("Error al agregar ingrediente: ${state.ex.message}")
            }
        })
    }
    private fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }
    fun initrv(){
        ingredientesAdapter = ComprasAdapter(this)
        binding.rvcompras.layoutManager = LinearLayoutManager(requireContext())
        binding.rvcompras.adapter = ingredientesAdapter
    }

    fun onNombreEmpty(){

    }
    fun onSuccessList(){
        binding.lotiesNoCompras.visibility = GONE
        binding.rvcompras.visibility = VISIBLE
    }
    fun onNoErrorList(){
        binding.lotiesNoCompras.visibility = VISIBLE
        binding.rvcompras.visibility = GONE
    }

    fun onError(exception: Exception){

    }
    fun onSuccess(compras: Compras){

    }

    @SuppressLint("MissingInflatedId")
    private fun mostrarDialogoAgregarIngrediente() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_agregar_ingrediente, null)
        val editTextIngrediente = dialogView.findViewById<EditText>(R.id.etIngrediente)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("\n\nAgregar Ingrediente")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val nuevoIngrediente = editTextIngrediente.text.toString().trim()
                if (nuevoIngrediente.isNotEmpty()) {
                    viewmodel.agregarIngrediente(nuevoIngrediente)
                    listviewmodel.obtenerIngredientes()
                } else {
                    Toast.makeText(requireContext(), "Ingrediente vacío", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickDelete(compras: Compras):Boolean {
        val dialog = BaseFragmentDialog.newInstance("Atencion", "Seguro que quieres borrar?")

        dialog.show((context as AppCompatActivity).supportFragmentManager, ContentValues.TAG)

        dialog.parentFragmentManager.setFragmentResultListener(BaseFragmentDialog.request,viewLifecycleOwner){
                _, build->
            val result = build.getBoolean(BaseFragmentDialog.result)
            if(result){
                listviewmodel.borrarIngrediente(compras)
            }
        }
        return true
    }
}