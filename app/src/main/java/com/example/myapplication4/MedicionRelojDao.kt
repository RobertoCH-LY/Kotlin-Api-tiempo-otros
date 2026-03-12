package com.example.myapplication2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MedicionRelojDao {

    @Insert
    suspend fun insertarMedicion(medicion: MedicionReloj)

    @Query("SELECT * FROM mediciones_reloj ORDER BY timestamp DESC")
    suspend fun obtenerTodasMediciones(): List<MedicionReloj>
}