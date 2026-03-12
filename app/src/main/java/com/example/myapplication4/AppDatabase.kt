package com.example.myapplication2

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [MedicionReloj::class, AcumuladoReloj::class, DatosClima::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicionRelojDao(): MedicionRelojDao
    abstract fun acumuladoRelojDao(): AcumuladoRelojDao
    abstract fun datosClimaDao(): DatosClimaDao
}