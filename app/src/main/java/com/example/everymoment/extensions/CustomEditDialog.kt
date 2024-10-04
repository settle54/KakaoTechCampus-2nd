package com.example.everymoment.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.everymoment.databinding.DialogCustomEditBinding

class CustomEditDialog(
    private val message: String,
    private val editHint: String = "",
    private val instruction: String = "",
    private val negText: String,
    private val posText: String,
    private val onNegativeClick: (() -> Unit)? = null,
    private val onPositiveClick: ((String) -> Unit)? = null
): DialogFragment() {

    private lateinit var binding: DialogCustomEditBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.setFlags(
            WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
            WindowManager.LayoutParams.FLAG_BLUR_BEHIND
        )

        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCustomEditBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.dialogMessage.text = message
        binding.editText.hint = editHint
        binding.instruction.text = instruction
        binding.negButton.text = negText
        binding.posButton.text = posText

        binding.negButton.setOnClickListener {
            onNegativeClick?.invoke()
            dismiss()
        }

        binding.posButton.setOnClickListener {
            onPositiveClick?.invoke(binding.editText.text.toString())
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        binding.editText.setText("")
    }

}