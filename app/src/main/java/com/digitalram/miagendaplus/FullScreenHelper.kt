package com.digitalram.miagendaplus

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity

class FullScreenHelper(private val activity: AppCompatActivity) {
    fun enterFullscreen() {
        // Si el dispositivo tiene Android 11 o superior...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Establecer que el decorView no está adaptado para las ventanas del sistema
            activity.window.setDecorFitsSystemWindows(false)
            // Obtener el WindowInsetsController y ocultar las barras de estado y de navegación
            activity.window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                // Establecer el comportamiento del WindowInsetsController para que muestre las barras de manera transitoria
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Si el dispositivo tiene una versión anterior a Android 11...
            // Establecer las propiedades del decorView para que oculte la barra de estado y la de navegación, y utilice un modo inmersivo
            activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }

    fun exitFullscreen() {
        // Si el dispositivo tiene Android 11 o superior...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Establecer que el decorView está adaptado para las ventanas del sistema
            activity.window.setDecorFitsSystemWindows(true)
            // Obtener el WindowInsetsController y mostrar las barras de estado y de navegación
            activity.window.insetsController?.let {
                it.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                // Establecer el comportamiento del WindowInsetsController para que muestre las barras cuando el usuario toque la pantalla
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
            }
        } else {
            // Si el dispositivo tiene una versión anterior a Android 11...
            // Establecer las propiedades del decorView para que muestre la barra de estado y la de navegación
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}