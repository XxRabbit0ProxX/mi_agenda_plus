package com.digitalram.miagendaplus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class ContactoActivity : AppCompatActivity() {

    // Declaracion de la clase AdminBD
    private lateinit var admin: AdminBD

    // Declaracion de componentes
    private lateinit var txtNomCon : TextInputEditText
    private lateinit var txtApeCon : TextInputEditText
    private lateinit var txtNumCon : TextInputEditText
    private lateinit var txtCorrCon : TextInputEditText
    private lateinit var rbS : RadioButton
    private lateinit var rbN : RadioButton
    private lateinit var rgTieneWhats : RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacto)

        admin = AdminBD(this)

        // Inicialización de los componentes
        txtNomCon = findViewById(R.id.txtNombreContacto)
        txtApeCon = findViewById(R.id.txtApellidoContacto)
        txtNumCon = findViewById(R.id.txtNumeroContacto)
        txtCorrCon = findViewById(R.id.txtCorreoContacto)
        rbS = findViewById(R.id.rbSi)
        rbN = findViewById(R.id.rbNo)
        rgTieneWhats = findViewById(R.id.rgTieneWhatsapp)
    }

    // Compotamiento del evento click en el fabGuardarContacto
    fun btnGuardarContactoClick(view: View) {

        var actividad = intent

        //Vaciado de la accion de la pantalla
        var accion = actividad.getStringExtra("accion")

        // Vaciado de las cajas de texto
        var txtnomcon = txtNomCon.text.toString()
        var txtapecon = txtApeCon.text.toString()
        var txtnumcon = txtNumCon.text.toString()
        var txtcorrcon = txtCorrCon.text.toString()

        // Vaciado de los valores de los check
        var rbs = rbS.isChecked
        var rbn = rbN.isChecked
        var tieneWhats = ""

        when{
            accion == "descripcion" ->{

                // Vaciar valores de la actividad anterior en variables nuevas
                var idcon = actividad.getStringExtra("idcto")
                var nomcon = actividad.getStringExtra("nomcto")
                var apecon = actividad.getStringExtra("apecto")
                var numcelcon = actividad.getStringExtra("numcelcto")
                var correocon = actividad.getStringExtra("correocto")
                var tienewhats = actividad.getStringExtra("tienewhats")
                var idusercon = actividad.getStringExtra("iduser")

                // Deshabilitar los campos en caso de que el usuario solo quiera ver los datos de su contacto
                txtNumCon.isEnabled = false
                txtApeCon.isEnabled = false
                txtNumCon.isEnabled = false
                txtCorrCon.isEnabled = false
                rgTieneWhats.isEnabled = false

                txtNomCon.setText(nomcon)
                txtNomCon.setText(nomcon)
                txtNomCon.setText(nomcon)
            }
        }
        // Almacena el id del usuario
        var idUser = actividad.getStringExtra("idUser").toString()

        // Si un check esta marcado y el otro no almacena si tiene o no whatsapp
        if (rbs == true && rbn == false){

            tieneWhats = "si"
        }else if(rbs == false && rbn == true){

            tieneWhats = "no"
        }

        /*
        * contactos:
        * nom_cto
        * ape_cto
        * numcel_cto
        * correo_cto
        * tiene_whatsapp
        * id_user FOREIGN KEY (id_user) REFERENCES usuarios(id_user))
        */

        var sentencia = "INSERT INTO contactos(nom_cto, ape_cto, numcel_cto, correo_cto, tiene_whatsapp, id_user)" +
                "VALUES('$txtnomcon', '$txtapecon', '$txtnumcon', '$txtcorrcon', '$tieneWhats', $idUser)"

        when{

            txtNomCon.text.isNullOrEmpty() ->{

                txtNomCon.setText("No puedes dejar el campo nombre vacio")
                txtNomCon.requestFocus()
            }
            txtNumCon.text.isNullOrEmpty() ->{

                txtNumCon.setError("No puedes dejar este campo vacio")
                txtNumCon.requestFocus()
            }
            rbS.isChecked == false && rbN.isChecked == false ->{

                Toast.makeText(this, "Debes seleccionar una opción", Toast.LENGTH_SHORT).show()
                rgTieneWhats.requestFocus()
            }
            else ->{

                if (admin.Ejecuta(sentencia)){

                    limpiar()
                    //startActivity(Intent(this,LoginActivity::class.java))
                    Toast.makeText(this, "Contacto registrado exitosamente", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
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

    // Limpia las cajas de texto
    fun limpiar(){

        txtNomCon.setText("")
        txtApeCon.setText("")
        txtNumCon.setText("")
        txtCorrCon.setText("")
        rbS.isChecked = false
        rbN.isChecked = false
    }
}