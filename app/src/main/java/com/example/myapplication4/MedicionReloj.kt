package com.example.myapplication2
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "mediciones_reloj")
data class MedicionReloj(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,                // ID autogenerado de la medición
    val timestamp: String,           // Fecha y hora de la medición
    val valor: Double,               // Valor medido (pasos, bpm, distancia, etc.)
    val tipoMedicion: String,        // Tipo de medición: "pasos", "frecuencia_cardiaca", etc.
    val unidad: String? = null       // Unidad opcional: "pasos", "bpm", "km", etc.
)