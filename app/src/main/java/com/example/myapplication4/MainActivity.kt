package com.example.myapplication4

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // 1. Herramienta para pedir permisos al usuario
    private val pedirPermisosGps = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val gpsPreciso = permisos[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val gpsAproximado = permisos[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (gpsPreciso || gpsAproximado) {
            Log.d("PRUEBA_ROBERTO", "Permiso de GPS concedido por el usuario.")
        } else {
            Log.e("PRUEBA_ROBERTO", "Permiso denegado.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pedimos los permisos nada más abrir la app
        pedirPermisosGps.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        // Iniciamos tu GestorClima
        val miGestor = GestorClima(this)

        // 2. Dibujamos la pantalla con Jetpack Compose
        setContent {
            // Variables que guardan el texto que se ve en pantalla
            var textoPantalla by remember { mutableStateOf("Pulsa el botón para buscar tu clima") }
            val coroutineScope = rememberCoroutineScope()

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Texto que irá cambiando
                Text(
                    text = textoPantalla,
                    modifier = Modifier.padding(16.dp)
                )

                // El botón de refrescar
                Button(onClick = {
                    // Al pulsar, primero cambiamos el mensaje
                    textoPantalla = "Buscando GPS y Clima..."

                    // Lanzamos tu código en segundo plano
                    coroutineScope.launch {
                        try {
                            val clima = miGestor.obtenerClimaActual()

                            if (clima != null) {
                                // Si hay éxito, actualizamos la pantalla
                                textoPantalla = "Temperatura: ${clima.temperature_2m}ºC\nHumedad: ${clima.relative_humidity_2m}%"
                            } else {
                                textoPantalla = "Fallo: Comprueba que el GPS está encendido."
                            }
                        } catch (e: Exception) {
                            textoPantalla = "Error de conexión: ${e.message}"
                        }
                    }
                }) {
                    Text("Actualizar Clima")
                }
            }
        }
    }
}