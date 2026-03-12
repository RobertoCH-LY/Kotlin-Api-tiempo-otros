package com.example.myapplication4

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.myapplication2.AppDatabase
import com.example.myapplication2.DatosClima
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume

class GestorClima(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Inicialización de la base de datos local (Room)
    private val baseDatos = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "app_database"
    ).fallbackToDestructiveMigration().build()

    private val climaDao = baseDatos.datosClimaDao()

    @SuppressLint("MissingPermission")
    suspend fun obtenerClimaActual(): CurrentWeather? {
        return try {
            val ubicacion = obtenerUbicacionFresca()

            if (ubicacion != null) {
                val lat = ubicacion.latitude
                val lon = ubicacion.longitude

                Log.d("PRUEBA_ROBERTO", "Coordenadas GPS -> Lat: $lat, Lon: $lon")

                // Petición a la API de Open-Meteo
                val respuestaApi = RetrofitClient.service.obtenerClima(lat, lon)
                val climaActual = respuestaApi.current

                // Guardado en SQLite (Tabla DatosClima)
                if (climaActual != null) {
                    val formatoFecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val horaActual = formatoFecha.format(Date())

                    // Convertimos a Double (Ignora si Android Studio lo subraya en amarillo)
                    val nuevoRegistro = DatosClima(
                        timestamp = horaActual,
                        latitud = lat,
                        longitud = lon,
                        temperatura = climaActual.temperature_2m.toDouble(),
                        humedad = climaActual.relative_humidity_2m.toDouble()
                    )

                    climaDao.insertarDatosClima(nuevoRegistro)

                    // Comprobación de integridad por consola
                    Log.d("PRUEBA_ROBERTO", "Registro insertado en SQLite correctamente.")
                    Log.d("PRUEBA_ROBERTO", "Historial completo: ${climaDao.obtenerDatosClima()}")
                }

                climaActual
            } else {
                Log.d("PRUEBA_ROBERTO", "Error: Timeout o fallo al obtener coordenadas GPS.")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Fuerza el encendido del GPS con prioridad de alta precisión,
     * captura una única coordenada fresca y apaga el sensor para ahorrar batería.
     */
    @SuppressLint("MissingPermission")
    private suspend fun obtenerUbicacionFresca(): android.location.Location? = suspendCancellableCoroutine { continuation ->

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMaxUpdates(1)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                fusedLocationClient.removeLocationUpdates(this)
                if (continuation.isActive) {
                    continuation.resume(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener {
            if (continuation.isActive) {
                continuation.resume(null)
            }
        }

        continuation.invokeOnCancellation {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}