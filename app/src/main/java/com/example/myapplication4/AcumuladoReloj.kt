package com.example.myapplication2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "acumulados_reloj")
data class AcumuladoReloj(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,             // Fecha del acumulado (yyyy-MM-dd)
    val pasosTotales: Int,         // Total de pasos del día
    val distanciaTotal: Double,    // Total de distancia recorrida (km)
    val caloriasTotales: Double,   // Calorías consumidas
    val ritmoPromedio: Double      // Ritmo cardíaco promedio
)