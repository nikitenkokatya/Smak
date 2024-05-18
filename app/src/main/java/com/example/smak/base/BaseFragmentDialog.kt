package com.example.smak.base

import android.R
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class BaseFragmentDialog() : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getString(title)
        val message = requireArguments().getString(message)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(
            R.string.ok
        ) { _, _ ->
            val bundle = Bundle()
            bundle.putBoolean(result, true)
            requireActivity().supportFragmentManager.setFragmentResult(request, bundle)
        }

        builder.setNegativeButton(
            R.string.cancel
        )
        { _, _ -> dismiss() }
        return builder.create()
    }

    companion object {
        const val title = "title"
        const val message = "message"
        const val request = "request"
        const val result = "result"
        fun newInstance(title: String, message: String): BaseFragmentDialog {
            val fragment = BaseFragmentDialog()
            val args = Bundle()
            args.putString(Companion.title, title)
            args.putString(Companion.message, message)
            fragment.arguments = args
            return fragment
        }
    }
}