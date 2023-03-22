package com.digitalram.miagendaplus

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    // Declaracion de componetes
    private lateinit var txtNom : TextInputEditText
    private lateinit var txtCorr : TextInputEditText
    private lateinit var txtPass : TextInputEditText
    private lateinit var txtPassR : TextInputEditText

    private lateinit var admin: AdminBD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Pantalla competa
        val fullScreenHelper = FullScreenHelper(this)
        fullScreenHelper.enterFullscreen()

        txtNom = findViewById(R.id.txtNombre)
        txtCorr = findViewById(R.id.txtCorreo)
        txtPass = findViewById(R.id.txtContrasena)
        txtPassR = findViewById(R.id.txtContrasenaRep)

        admin = AdminBD(this)
    }

    fun btnRegistrarseClick(view: View) {

        var txtcorr = txtCorr.text.toString()
        var txtpass = txtPass.text.toString()
        var txtnom = txtNom.text.toString()

        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        /*
        * usuarios:
        * correo_user TEXT PRIMARY KEY UNIQUE
        * nom_user TEXT
        * pass_user TEXT
        * alta_user DATE
        */
        var sentencia : String = "INSERT INTO usuarios(correo_user, nom_user, pass_user, alta_user) VALUES('$txtcorr', '$txtnom', '$txtpass', datetime('$fecha'))"

        when{

            txtNom.text.isNullOrEmpty() ->{

                txtNom.setError("Campo de nombre vacio")
                txtNom.requestFocus()
            }
            txtCorr.text.isNullOrEmpty() ->{

                txtCorr.setError("Campo de correo vacio")
                txtCorr.requestFocus()
            }
            txtPass.text.isNullOrEmpty() ->{

                txtPass.setError("Campo de contraseña vacio")
                txtPass.requestFocus()
            }
            txtPass.text.toString() != txtPassR.text.toString() ->{

                txtPass.setError("Las contraseñas no coinciden (no olvides llenar ambos campos)")
                txtPass.requestFocus()

                txtPass.setText("")
                txtPassR.setText("")
            }
            else ->{

                if (admin.Ejecuta(sentencia)){

                    limpiar()
                    startActivity(Intent(this,MainActivity::class.java))
                    Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_LONG).show()
                    finish()
                }
                else {

                    Toast.makeText(this, "No se pudo registrar el usuario", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    // Limpia las cajas de texto
    fun limpiar(){

        txtNom.setText("")
        txtCorr.setText("")
        txtPass.setText("")
        txtPassR.setText("")
    }

    // Cambiar el comportamiento del botón de volver
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}