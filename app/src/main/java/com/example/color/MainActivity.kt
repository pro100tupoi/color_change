package com.example.color

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.color.databinding.ActivityMainBinding
import com.example.color.databinding.DialogColorPickerBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentRed = 100
    private var currentGreen = 125
    private var currentBlue = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateMainViews(currentRed, currentGreen, currentBlue)

        binding.btnShowDialog.setOnClickListener {
            showColorPickerDialog()
        }
    }

    private fun showColorPickerDialog() {
        val dialogBinding = DialogColorPickerBinding.inflate(LayoutInflater.from(this))

        // Установка начальных значений
        dialogBinding.sbRed.max = 255
        dialogBinding.sbGreen.max = 255
        dialogBinding.sbBlue.max = 255

        dialogBinding.sbRed.progress = currentRed
        dialogBinding.sbGreen.progress = currentGreen
        dialogBinding.sbBlue.progress = currentBlue

        dialogBinding.etRed.setText(currentRed.toString())
        dialogBinding.etGreen.setText(currentGreen.toString())
        dialogBinding.etBlue.setText(currentBlue.toString())

        updateDialogViews(dialogBinding.dialogColorPreview, dialogBinding.tvHexDialog, dialogBinding.tvRgbDialog,
            currentRed, currentGreen, currentBlue)

        // Обработчик SeekBar
        val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val red = dialogBinding.sbRed.progress
                    val green = dialogBinding.sbGreen.progress
                    val blue = dialogBinding.sbBlue.progress

                    dialogBinding.etRed.setText(red.toString())
                    dialogBinding.etGreen.setText(green.toString())
                    dialogBinding.etBlue.setText(blue.toString())

                    // Обновляем диалоговое окно
                    updateDialogViews(dialogBinding.dialogColorPreview, dialogBinding.tvHexDialog,
                        dialogBinding.tvRgbDialog, red, green, blue)//ЦИА
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

        dialogBinding.sbRed.setOnSeekBarChangeListener(seekBarListener)
        dialogBinding.sbGreen.setOnSeekBarChangeListener(seekBarListener)
        dialogBinding.sbBlue.setOnSeekBarChangeListener(seekBarListener)

        // Обработчик EditText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                try {
                    val red = dialogBinding.etRed.text.toString().takeIf { it.isNotEmpty() }?.toInt()?.coerceIn(0, 255) ?: 0
                    val green = dialogBinding.etGreen.text.toString().takeIf { it.isNotEmpty() }?.toInt()?.coerceIn(0, 255) ?: 0
                    val blue = dialogBinding.etBlue.text.toString().takeIf { it.isNotEmpty() }?.toInt()?.coerceIn(0, 255) ?: 0

                    dialogBinding.sbRed.progress = red
                    dialogBinding.sbGreen.progress = green
                    dialogBinding.sbBlue.progress = blue

                    updateDialogViews(dialogBinding.dialogColorPreview, dialogBinding.tvHexDialog,
                        dialogBinding.tvRgbDialog, red, green, blue)
                } catch (e: NumberFormatException) {
                    // Игнорируем неверный ввод
                }
            }
        }

        dialogBinding.etRed.addTextChangedListener(textWatcher)
        dialogBinding.etGreen.addTextChangedListener(textWatcher)
        dialogBinding.etBlue.addTextChangedListener(textWatcher)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle("Выберите цвет")
            .create()

        dialogBinding.btnConfirm.setOnClickListener {
            currentRed = dialogBinding.sbRed.progress
            currentGreen = dialogBinding.sbGreen.progress
            currentBlue = dialogBinding.sbBlue.progress
            updateMainViews(currentRed, currentGreen, currentBlue)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateDialogViews(
        preview: View,
        hexView: TextView,
        rgbView: TextView,
        red: Int,
        green: Int,
        blue: Int
    ) {
        preview.setBackgroundColor(Color.rgb(red, green, blue))
        rgbView.text = "RGB: $red, $green, $blue"
        hexView.text = "HEX: ${rgbToHex(red, green, blue)}"
    }

    private fun updateMainViews(red: Int, green: Int, blue: Int) {
        binding.colorPreview.setBackgroundColor(Color.rgb(red, green, blue))
        binding.tvRgb.text = "RGB: $red, $green, $blue"
        binding.tvHex.text = "HEX: ${rgbToHex(red, green, blue)}"
    }

    private fun rgbToHex(red: Int, green: Int, blue: Int): String {
        return String.format("#%02X%02X%02X", red, green, blue)
    }
}