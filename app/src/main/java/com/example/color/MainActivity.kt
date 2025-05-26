package com.example.color

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.color.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentRed = 255
    private var currentGreen = 255
    private var currentBlue = 255

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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_color_picker, null)

        val dialogColorPreview = dialogView.findViewById<View>(R.id.dialogColorPreview)
        val etRed = dialogView.findViewById<EditText>(R.id.etRed)
        val etGreen = dialogView.findViewById<EditText>(R.id.etGreen)
        val etBlue = dialogView.findViewById<EditText>(R.id.etBlue)
        val sbRed = dialogView.findViewById<SeekBar>(R.id.sbRed)
        val sbGreen = dialogView.findViewById<SeekBar>(R.id.sbGreen)
        val sbBlue = dialogView.findViewById<SeekBar>(R.id.sbBlue)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val tvHexDialog = dialogView.findViewById<TextView>(R.id.tvHexDialog)
        val tvRgbDialog = dialogView.findViewById<TextView>(R.id.tvRgbDialog)

        // Установка начальных значений
        sbRed.max = 255
        sbGreen.max = 255
        sbBlue.max = 255

        sbRed.progress = currentRed
        sbGreen.progress = currentGreen
        sbBlue.progress = currentBlue

        etRed.setText(currentRed.toString())
        etGreen.setText(currentGreen.toString())
        etBlue.setText(currentBlue.toString())

        updateDialogViews(dialogColorPreview, tvHexDialog, tvRgbDialog, currentRed, currentGreen, currentBlue)

        // Обработчик SeekBar
        val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val red = sbRed.progress
                    val green = sbGreen.progress
                    val blue = sbBlue.progress

                    etRed.setText(red.toString())
                    etGreen.setText(green.toString())
                    etBlue.setText(blue.toString())

                    updateDialogViews(dialogColorPreview, tvHexDialog, tvRgbDialog, red, green, blue)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

        sbRed.setOnSeekBarChangeListener(seekBarListener)
        sbGreen.setOnSeekBarChangeListener(seekBarListener)
        sbBlue.setOnSeekBarChangeListener(seekBarListener)

        // Обработчик EditText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                try {
                    val red = etRed.text.toString().takeIf { it.isNotEmpty() }?.toInt()?.coerceIn(0, 255) ?: 0
                    val green = etGreen.text.toString().takeIf { it.isNotEmpty() }?.toInt()?.coerceIn(0, 255) ?: 0
                    val blue = etBlue.text.toString().takeIf { it.isNotEmpty() }?.toInt()?.coerceIn(0, 255) ?: 0

                    sbRed.progress = red
                    sbGreen.progress = green
                    sbBlue.progress = blue

                    updateDialogViews(dialogColorPreview, tvHexDialog, tvRgbDialog, red, green, blue)
                } catch (e: NumberFormatException) {
                    // Игнорируем неверный ввод
                }
            }
        }

        etRed.addTextChangedListener(textWatcher)
        etGreen.addTextChangedListener(textWatcher)
        etBlue.addTextChangedListener(textWatcher)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Выберите цвет")
            .create()

        btnConfirm.setOnClickListener {
            currentRed = sbRed.progress
            currentGreen = sbGreen.progress
            currentBlue = sbBlue.progress
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