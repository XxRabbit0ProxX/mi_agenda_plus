package com.digitalram.miagendaplus

import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class ContactoActivity : AppCompatActivity() {

    // Declaracion de la clase AdminBD
    private lateinit var admin: AdminBD

    // Declaracion de componentes
    private lateinit var txtNomCon : TextInputEditText
    private lateinit var txtDomCon : TextInputEditText
    private lateinit var txtNumCelCon : TextInputEditText
    private lateinit var txtNumTelCon : TextInputEditText
    private lateinit var txtCorrCon : TextInputEditText
    private lateinit var txtCumpleCon : TextInputEditText
    private lateinit var fabEdit : FloatingActionButton
    private lateinit var fabGuardar : FloatingActionButton

    private lateinit var accion : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_contacto)

        admin = AdminBD(this)

        var act = intent

        // Inicialización de los componentes
        txtNomCon = findViewById(R.id.txtNombreContacto)
        txtDomCon = findViewById(R.id.txtDomicilioContacto)
        txtNumCelCon = findViewById(R.id.txtNumeroCelContacto)
        txtNumTelCon = findViewById(R.id.txtNumeroTelContacto)
        txtCorrCon = findViewById(R.id.txtCorreoContacto)
        txtCumpleCon = findViewById(R.id.txtFechaCumpleanos)
        fabEdit = findViewById(R.id.fabEditar)
        fabGuardar = findViewById(R.id.fabGuardarContacto)

        // Variable con la accion que se realiza al presionar el boton de guardado
        accion = act.getStringExtra("accion").toString()

        // Abre el calendario para seleccionar la fecha en el edit text
        txtCumpleCon.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val monthString = (month + 1).toString().padStart(2, '0')
                    val dayString = dayOfMonth.toString().padStart(2, '0')
                    txtCumpleCon.setText("$year-$monthString-$dayString")
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Para evitar que el usuario ingrese manualmente la fecha en el EditText
        txtCumpleCon.keyListener = null

        // En caso de iniciarse la actividad por un RecyclerView Clicado
        if (accion == "descripcion") {

            // Vaciar intents a los campos de texto
            txtNomCon.setText(act.getStringExtra("nomcto"))
            txtDomCon.setText(act.getStringExtra("domcto"))
            txtNumCelCon.setText(act.getStringExtra("numcelcto"))
            txtNumTelCon.setText(act.getStringExtra("numtelcto"))
            txtCorrCon.setText(act.getStringExtra("correocto"))
            txtCumpleCon.setText(act.getStringExtra("cumplecto"))

            // Deshabilitar campos de texto
            txtNomCon.isEnabled = false
            txtDomCon.isEnabled = false
            txtNumCelCon.isEnabled = false
            txtNumTelCon.isEnabled = false
            txtCorrCon.isEnabled = false
            txtCumpleCon.isEnabled = false

            // Habilitar boton de edición
            fabEdit.isEnabled = true
            fabEdit.isVisible = true

            // Deshabilitar boton de guardado
            fabGuardar.isEnabled = false
            fabGuardar.isVisible = false
        }
    }

    // Compotamiento del evento click en el fabGuardarContacto
    fun btnGuardarContactoClick(view: View) {

        var actividad = intent

        Toast.makeText(this, "Accion: " + accion, Toast.LENGTH_SHORT).show()

        // Vaciado de las cajas de texto
        var txtnomcon = txtNomCon.text.toString()
        var txtdomcon = txtDomCon.text.toString()
        var txtnumcelcon = txtNumCelCon.text.toString()
        var txtnumtelcon = txtNumTelCon.text.toString()
        var txtcorrcon = txtCorrCon.text.toString()
        var txtcumplecon = txtCumpleCon.text.toString()

        // Vacia la fecha actual del dispositivo en una variable
        var fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Vacia el correo del usuario en una variable usando un intent
        var correoUser = actividad.getStringExtra("correoUser").toString()

        /*
        * contactos(
        * id_cto INTEGER PRIMARY KEY AUTOINCREMENT
        * nom_cto TEXT
        * dom_cto TEXT
        * correo_cto TEXT
        * tel_cto TEXT
        * cel_cto TEXT
        * cumple_cto DATE
        * alta_cto DATE
        * correo_user TEXT
        * FOREIGN KEY (correo_user) REFERENCES usuarios(correo_user))")
        */

        if (accion == "agregar") {

            var sentencia = "INSERT INTO contactos(nom_cto, dom_cto, cel_cto, tel_cto, correo_cto, cumple_cto, alta_cto, correo_user)" +
                    "VALUES('$txtnomcon', '$txtdomcon', '$txtnumcelcon', '$txtnumtelcon', '$txtcorrcon', '$txtcumplecon', datetime('$fechaActual'), '$correoUser')"

            when{

                txtNomCon.text.isNullOrEmpty() ->{

                    txtNomCon.setText("No puedes dejar el campo nombre vacio")
                    txtNomCon.requestFocus()
                }
                txtDomCon.text.isNullOrEmpty() ->{

                    txtDomCon.setError("No puedes dejar este campo vacio")
                    txtDomCon.requestFocus()
                }
                txtCorrCon.text.isNullOrEmpty() ->{

                    txtCorrCon.setError("No puedes dejar este campo vacio")
                    txtCorrCon.requestFocus()
                }
                txtNumTelCon.text.isNullOrEmpty() ->{

                    txtNumTelCon.setError("No puedes dejar este campo vacio")
                    txtNumTelCon.requestFocus()
                }
                txtNumCelCon.text.isNullOrEmpty() ->{

                    txtNumCelCon.setError("No puedes dejar este campo vacio")
                    txtNumCelCon.requestFocus()
                }
                txtCumpleCon.text.isNullOrEmpty() ->{

                    txtCumpleCon.setError("No puedes dejar este campo vacio")
                    txtCumpleCon.requestFocus()
                }
                else ->{

                    if (admin.Ejecuta(sentencia)){

                        limpiar()
                        Toast.makeText(this, "Contacto registrado exitosamente", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }
        else if (accion == "editar") {

            var sentencia = "UPDATE contactos SET nom_cto = '$txtnomcon', dom_cto = '$txtdomcon', cel_cto = '$txtnumcelcon', tel_cto = '$txtnumtelcon', " +
                    "correo_cto = '$txtcorrcon', cumple_cto = '$txtcumplecon' " +
                    "WHERE correo_user = '$correoUser'"

            when{

                txtNomCon.text.isNullOrEmpty() ->{

                    txtNomCon.setText("No puedes dejar el campo nombre vacio")
                    txtNomCon.requestFocus()
                }
                txtDomCon.text.isNullOrEmpty() ->{

                    txtDomCon.setError("No puedes dejar este campo vacio")
                    txtDomCon.requestFocus()
                }
                txtCorrCon.text.isNullOrEmpty() ->{

                    txtCorrCon.setError("No puedes dejar este campo vacio")
                    txtCorrCon.requestFocus()
                }
                txtNumTelCon.text.isNullOrEmpty() ->{

                    txtNumTelCon.setError("No puedes dejar este campo vacio")
                    txtNumTelCon.requestFocus()
                }
                txtNumCelCon.text.isNullOrEmpty() ->{

                    txtNumCelCon.setError("No puedes dejar este campo vacio")
                    txtNumCelCon.requestFocus()
                }
                txtCumpleCon.text.isNullOrEmpty() ->{

                    txtCumpleCon.setError("No puedes dejar este campo vacio")
                    txtCumpleCon.requestFocus()
                }
                else ->{

                    if (admin.Ejecuta(sentencia)){

                        limpiar()
                        Toast.makeText(this, "Contacto actualizado exitosamente", Toast.LENGTH_LONG).show()
                        finish()
                    }
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
        txtDomCon.setText("")
        txtNumCelCon.setText("")
        txtNumTelCon.setText("")
        txtCorrCon.setText("")
        txtCumpleCon.setText("")
    }

    fun btnEditarClick(view: View) {

        if (!txtNomCon.isEnabled && !txtDomCon.isEnabled && !txtNumCelCon.isEnabled && !txtNumTelCon.isEnabled && !txtCorrCon.isEnabled && !txtCumpleCon.isEnabled) {

            // Habilitar campos de texto
            txtNomCon.isEnabled = true
            txtDomCon.isEnabled = true
            txtNumCelCon.isEnabled = true
            txtNumTelCon.isEnabled = true
            txtCorrCon.isEnabled = true
            txtCumpleCon.isEnabled = true

            // Habilitar boton de guardado
            fabGuardar.isEnabled = true
            fabGuardar.isVisible = true

            accion = "editar"
        }else{

            // Deshabilitar campos de texto
            txtNomCon.isEnabled = false
            txtDomCon.isEnabled = false
            txtNumCelCon.isEnabled = false
            txtNumTelCon.isEnabled = false
            txtCorrCon.isEnabled = false
            txtCumpleCon.isEnabled = false

            // Deshabilitar boton de guardado
            fabGuardar.isEnabled = false
            fabGuardar.isVisible = false

            accion = "descripcion"
        }
    }
}