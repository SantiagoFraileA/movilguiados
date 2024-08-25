package com.example.movilguia2

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var textView: TextView
    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null
    private var isSensorAvailable = false
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (temperatureSensor != null) {
            isSensorAvailable = true
        } else {
            textView.text = "Sensor de temperatura no disponible"
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val temperature = it.values[0]
            textView.text = "${temperature.toInt()} °C"
            changeBackgroundColor(temperature)
        }
    }

    private fun changeBackgroundColor(temperature: Float) {
        val color = when {
            temperature <= 1 -> Color.rgb(0, 191, 255)  // Azul hielo
            temperature in 1.1..10.0 -> Color.rgb(15, 96, 239)  // Azul
            temperature in 10.1..30.0 -> Color.rgb(21, 190, 34)  // Verde
            temperature in 30.1..45.0 -> Color.rgb(255, 164, 41)  // Naranja
            temperature in 45.1..60.0 -> Color.rgb(255, 0, 0)  // Rojo
            else -> Color.rgb(200, 0, 40)  // Rojo fuerte
        }
        imageView.setBackgroundColor(color)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        if (isSensorAvailable) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSensorAvailable) {
            sensorManager.unregisterListener(this)
        }
    }
}
