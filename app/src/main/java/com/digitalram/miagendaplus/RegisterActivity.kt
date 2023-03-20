package com.digitalram.miagendaplus

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    // Declaracion de componetes
    private lateinit var txtNom : TextInputEditText
    private lateinit var txtApe : TextInputEditText
    private lateinit var txtTelCel : TextInputEditText
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
        txtApe = findViewById(R.id.txtApellido)
        txtTelCel = findViewById(R.id.txtTelefonoCel)
        txtCorr = findViewById(R.id.txtCorreo)
        txtPass = findViewById(R.id.txtContrasena)
        txtPassR = findViewById(R.id.txtContrasenaRep)

        admin = AdminBD(this)
    }

    fun btnRegistrarseClick(view: View) {

        var txtnom = txtNom.text.toString()
        var txtape = txtApe.text.toString()
        var txttelcel = txtTelCel.text.toString()
        var txtcorr = txtCorr.text.toString()
        var txtpass = txtPass.text.toString()

        /*
        * usuarios:
        * id_user
        * nom_user
        * ape_user
        * numcel_user
        * correo_user
        * pass_user
        */
        var sentencia : String = "INSERT INTO usuarios(nom_user, ape_user, numcel_user, correo_user, pass_user) VALUES('$txtnom', '$txtape', '$txttelcel'," +
                "'$txtcorr', '$txtpass')"

        when{

            txtNom.text.isNullOrEmpty() ->{

                txtNom.setError("Campo de nombre vacio")
                txtNom.requestFocus()
            }
            txtApe.text.isNullOrEmpty() ->{

                txtApe.setError("Campo de apellido vacio")
                txtApe.requestFocus()
            }
            txtTelCel.text.isNullOrEmpty() ->{

                txtTelCel.setError("Campo de telefono vacio")
                txtTelCel.requestFocus()
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

                // Si ya hay usuario con ese telefono o correo manda error y no inserta usuario
                if (nCeloCorreoExisten(txttelcel, txtcorr)){

                    Toast.makeText(this, "Ya existen usuarios registrados con eso correo o numero", Toast.LENGTH_LONG).show()
                }
                // Si no existe hace la insercion
                else{

                    if (admin.Ejecuta(sentencia)){

                        limpiar()
                        startActivity(Intent(this,LoginActivity::class.java))
                        Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else{

                        Toast.makeText(this, "No se pudo registrar el usuario", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // Verifica si el numero de telefono y correo ya se encuentran registrados
    fun nCeloCorreoExisten(txttelcel : String, txtcorr : String) : Boolean{

        val sentencia = "SELECT numcel_user, correo_user FROM usuarios WHERE numcel_user = '$txttelcel' OR correo_user = '$txtcorr'"
        val result: Cursor? = admin.Consultar(sentencia)

        if (result != null && result.moveToFirst()) {

            Toast.makeText(this, "Error numero o correo ya registrados: numero celular -> " + result.getInt(0).toString() + ", correo -> " + result.getInt(1).toString(), Toast.LENGTH_SHORT).show()
            result.close()
            return true
        }
        else{

            return false
        }
    }

    // Limpia las cajas de texto
    fun limpiar(){

        txtNom.setText("")
        txtApe.setText("")
        txtTelCel.setText("")
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