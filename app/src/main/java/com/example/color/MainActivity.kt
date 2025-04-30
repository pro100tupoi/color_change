package com.example.color
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import com.example.color.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим кнопку и View для цвета
        val btnShowDialog = findViewById<Button>(R.id.btnShowDialog)
        val colorPreview = findViewById<android.view.View>(R.id.colorPreview)

        // Обработка нажатия на кнопку
        btnShowDialog.setOnClickListener {
            showColorPickerDialog(colorPreview)
        }
    }

    private fun showColorPickerDialog(colorPreview: android.view.View) {
        // Загружаем разметку диалога
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_color_picker, null)

        // Находим все элементы диалога
        val dialogColorPreview = dialogView.findViewById<android.view.View>(R.id.dialogColorPreview)
        val etRed = dialogView.findViewById<EditText>(R.id.etRed)
        val etGreen = dialogView.findViewById<EditText>(R.id.etGreen)
        val etBlue = dialogView.findViewById<EditText>(R.id.etBlue)
        val sbRed = dialogView.findViewById<SeekBar>(R.id.sbRed)
        val sbGreen = dialogView.findViewById<SeekBar>(R.id.sbGreen)
        val sbBlue = dialogView.findViewById<SeekBar>(R.id.sbBlue)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)

        // Устанавливаем начальные значения (белый цвет)
        sbRed.progress = 255
        sbGreen.progress = 255
        sbBlue.progress = 255
        etRed.setText("255")
        etGreen.setText("255")
        etBlue.setText("255")
        updateColorPreview(dialogColorPreview, 255, 255, 255)

        // Обработка изменений в SeekBar
        val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val red = sbRed.progress
                val green = sbGreen.progress
                val blue = sbBlue.progress

                etRed.setText(red.toString())
                etGreen.setText(green.toString())
                etBlue.setText(blue.toString())

                updateColorPreview(dialogColorPreview, red, green, blue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

        sbRed.setOnSeekBarChangeListener(seekBarListener)
        sbGreen.setOnSeekBarChangeListener(seekBarListener)
        sbBlue.setOnSeekBarChangeListener(seekBarListener)

        // Обработка изменений в EditText
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                try {
                    val red = etRed.text.toString().toInt().coerceIn(0, 255)
                    val green = etGreen.text.toString().toInt().coerceIn(0, 255)
                    val blue = etBlue.text.toString().toInt().coerceIn(0, 255)

                    sbRed.progress = red
                    sbGreen.progress = green
                    sbBlue.progress = blue

                    updateColorPreview(dialogColorPreview, red, green, blue)
                } catch (e: Exception) {
                    // Если ввод неверный, оставляем предыдущие значения
                }
            }
        }

        etRed.addTextChangedListener(textWatcher)
        etGreen.addTextChangedListener(textWatcher)
        etBlue.addTextChangedListener(textWatcher)

        // Создаём и показываем диалог
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Выберите цвет")
            .create()

        // Обработка кнопки "Применить"
        btnConfirm.setOnClickListener {
            val red = sbRed.progress
            val green = sbGreen.progress
            val blue = sbBlue.progress

            colorPreview.setBackgroundColor(Color.rgb(red, green, blue))
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateColorPreview(view: android.view.View, red: Int, green: Int, blue: Int) {
        view.setBackgroundColor(Color.rgb(red, green, blue))
    }
}