package com.example.myapplication2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AcumuladoRelojDao {

    @Insert
    suspend fun insertarAcumulado(acumulado: AcumuladoReloj)

    @Query("SELECT * FROM acumulados_reloj ORDER BY fecha DESC")
    suspend fun obtenerAcumulados(): List<AcumuladoReloj>

    @Query("SELECT * FROM acumulados_reloj WHERE fecha = :fecha LIMIT 1")
    suspend fun obtenerAcumuladoPorFecha(fecha: String): AcumuladoReloj?
}