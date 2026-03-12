package com.example.myapplication2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "datos_clima")
data class DatosClima(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: String,           // Fecha y hora de la medición
    val latitud: Double,             // Latitud del dispositivo o usuario
    val longitud: Double,            // Longitud del dispositivo o usuario
    val temperatura: Double,         // temperature_2m de Open-Meteo
    val humedad: Double,             // relative_humidity_2m de Open-Meteo
    val pm10: Double? = null,        // PM10 (opcional)
    val pm25: Double? = null,        // PM2.5 (opcional)
    val polen: String? = null,       // Nivel de polen (opcional)
    val ciudad: String? = null       // Ciudad o localidad (opcional)
)