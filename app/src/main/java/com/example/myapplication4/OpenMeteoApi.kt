package com.example.myapplication4


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. Así es como recibimos los datos de la API
data class ClimaResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val temperature_2m: Double,
    val relative_humidity_2m: Int
)

// 2. Aquí definimos la llamada exacta a Open-Meteo
interface OpenMeteoService {
    @GET("v1/forecast")
    suspend fun obtenerClima(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        @Query("current") currentParams: String = "temperature_2m,relative_humidity_2m"
    ): ClimaResponse
}

// 3. El motor de conexión (Retrofit)
object RetrofitClient {
    val service: OpenMeteoService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoService::class.java)
    }
}