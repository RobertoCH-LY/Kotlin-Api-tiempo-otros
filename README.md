# Proyecto

Este repositorio contiene el codigo para obtener la ubicacion del dispositivo y los datos del clima dentro de la aplicacion de salud. El archivo principal es la clase GestorClima.

### Funcionamiento

El sistema realiza los siguientes pasos en segundo plano:
1. Obtiene una coordenada reciente mediante el GPS del dispositivo y apaga el sensor para ahorrar bateria.
2. Envia estas coordenadas a la API externa de Open-Meteo para consultar la temperatura y humedad actuales.
3. Guarda automaticamente estos valores en la base de datos interna de la aplicacion utilizando Room (version 2.7.2).

### Requisitos para ejecutarlo

Para probar o abrir este proyecto necesitas:
* Android Studio instalado en el equipo.
* Conexion a internet para que Gradle descargue las librerias necesarias (Retrofit, Gson, Room).
* Un telefono Android fisico o un emulador con acceso a internet.
* Aceptar los permisos de ubicacion en el dispositivo cuando la aplicacion arranque.

### Licencia

Este proyecto utiliza la licencia MIT. El código es libre y se puede usar, modificar o distribuir sin restricciones, siempre que se mantenga el aviso de copyright original.
