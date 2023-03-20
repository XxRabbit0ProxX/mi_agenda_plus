package com.digitalram.miagendaplus

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    // Declaracion de componentes
    private lateinit var txtCorr : TextInputEditText
    private lateinit var txtPass : TextInputEditText

    private lateinit var admin: AdminBD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Pantalla completa:Modo imersivo
        val fullScreenHelper = FullScreenHelper(this)
        fullScreenHelper.enterFullscreen()

        txtCorr = findViewById(R.id.txtCorreo)
        txtPass = findViewById(R.id.txtContrasena)

        admin = AdminBD(this)
    }

    fun btnIniciarSesionClick(view: View) {

        var txtcorr = txtCorr.text.toString()
        var txtpass = txtPass.text.toString()
        var actividad = Intent(this,MainActivity::class.java)

        if (usuarioExiste(txtcorr, txtpass)){

            val sentencia = "SELECT id_user FROM usuarios WHERE correo_user = '$txtcorr' AND pass_user = '$txtpass'"
            val result: Cursor? = admin.Consultar(sentencia)

            if (result != null && result.moveToFirst()){

                actividad.putExtra("idUser", result.getString(0).toString())
                Toast.makeText(this, "id del usuario: " + result.getString(0).toString(), Toast.LENGTH_SHORT).show()
            }

            actividad.putExtra("usuariolog", true)
            startActivity(actividad)
            //finish()
        }
        else{

            Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
        }
    }

    fun usuarioExiste(txtcorr : String, txtpass : String) : Boolean {

        val sentencia = "SELECT correo_user, pass_user FROM usuarios WHERE correo_user = '$txtcorr' AND pass_user = '$txtpass'"
        val result: Cursor? = admin.Consultar(sentencia)

        if (result != null && result.moveToFirst()) {

            Toast.makeText(this, "Bienvenido: " + result.getString(0).toString(), Toast.LENGTH_LONG).show()
            result.close()
            return true
        }
        else{

            return false
        }
    }

    fun btnNuevoUsuarioClick(view: View) {

        startActivity(Intent(this,RegisterActivity::class.java))
    }
}