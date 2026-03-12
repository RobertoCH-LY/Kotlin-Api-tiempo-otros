package com.example.myapplication2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DatosClimaDao {

    @Insert
    suspend fun insertarDatosClima(datos: DatosClima)

    @Query("SELECT * FROM datos_clima ORDER BY timestamp DESC")
    suspend fun obtenerDatosClima(): List<DatosClima>

    @Query("SELECT * FROM datos_clima WHERE latitud = :lat AND longitud = :lon ORDER BY timestamp DESC LIMIT 1")
    suspend fun obtenerUltimoPorUbicacion(lat: Double, lon: Double): DatosClima?
}