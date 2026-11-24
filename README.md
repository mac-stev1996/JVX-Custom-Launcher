# JVX Custom Launcher

Proyecto inicial de launcher Android en Kotlin + Jetpack Compose, pensado para una pantalla horizontal 1280x720.

## Incluye
- 10 temas visuales seleccionables.
- Preferencia de tema guardada localmente.
- Accesos a Maps, YouTube, Spotify y Netflix (si están instalados).
- Acceso de retorno al selector HOME de Android.
- Workflow GitHub Actions para compilar un APK debug.

## Requisitos de compilación
- JDK 17
- Android Gradle Plugin 8.7.3
- Android SDK Platform 35
- Gradle 8.9 o compatible

## Compilar en GitHub
1. Sube todos los archivos y carpetas de este proyecto a la raíz del repositorio.
2. Abre la pestaña Actions.
3. Ejecuta `Build JVX Custom Launcher` con `Run workflow` o realiza un push a `main`.
4. Al finalizar, abre la ejecución y descarga el artifact `JVX-Custom-Launcher-debug`.

## Importante
Este es un prototipo inicial. No controla todavía radio FM, CAN Bus, climatización ni mandos del vehículo: esas funciones dependen de interfaces y SDK específicos del fabricante. El botón de retorno abre el selector HOME de Android; el acceso al launcher original depende de cómo el fabricante haya registrado su aplicación HOME.

El APK debug sirve para pruebas. Antes de usarlo como launcher predeterminado, instala y prueba la aplicación manualmente y conserva el acceso al launcher original. El proyecto usa compileSdk 35 y targetSdk 32 como base conservadora; no se afirma compatibilidad certificada con Android 17 ni con el firmware JVX hasta probarlo físicamente.
