package com.example.myapplication4

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GestorClima(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun obtenerClimaActual(): CurrentWeather? {
        return try {
            // 1. Llamamos a nuestra nueva función que obliga al GPS a despertar
            val ubicacion = obtenerUbicacionFresca()

            if (ubicacion != null) {
                val lat = ubicacion.latitude
                val lon = ubicacion.longitude

                Log.d("PRUEBA_ROBERTO", "NUEVAS Coordenadas detectadas -> Latitud: $lat, Longitud: $lon")

                // 2. Pasamos las coordenadas a la API
                val respuestaApi = RetrofitClient.service.obtenerClima(lat, lon)
                respuestaApi.current
            } else {
                Log.d("PRUEBA_ROBERTO", "Fallo: El GPS no pudo obtener una ubicación nueva.")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Esta función enciende la antena GPS, espera a recibir exactamente 1 coordenada nueva,
     * apaga la antena para ahorrar batería y devuelve el resultado.
     */
    @SuppressLint("MissingPermission")
    private suspend fun obtenerUbicacionFresca(): android.location.Location? = suspendCancellableCoroutine { continuation ->

        // Configuramos el GPS para máxima precisión y que solo nos devuelva 1 actualización
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setMaxUpdates(1)
            .build()

        // Preparamos el "receptor" que estará atento a cuando llegue la coordenada
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation

                // En cuanto llega, apagamos el receptor y enviamos la coordenada
                fusedLocationClient.removeLocationUpdates(this)
                if (continuation.isActive) {
                    continuation.resume(location)
                }
            }
        }

        // Encendemos el GPS con las reglas que hemos definido arriba
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener {
            if (continuation.isActive) {
                continuation.resume(null)
            }
        }

        // Si por algún motivo se cancela el proceso, nos aseguramos de apagar el GPS
        continuation.invokeOnCancellation {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}