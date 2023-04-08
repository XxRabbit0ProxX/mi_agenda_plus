package com.digitalram.miagendaplus

import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    // Declaracion de componetes
    private lateinit var txtNom : TextInputEditText
    private lateinit var txtCorr : TextInputEditText
    private lateinit var txtPass : TextInputEditText
    private lateinit var txtPassR : TextInputEditText

    private lateinit var admin: AdminBD

    var IP = "http://192.168.1.77"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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

        var act = Intent(this, MainActivity::class.java)

        var fechaAct = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()).toString();

        /*
        * usuarios:
        * correo_user TEXT PRIMARY KEY UNIQUE
        * nom_user TEXT
        * pass_user TEXT
        * alta_user DATE
        */

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


                var jsonEntrada : JSONObject = JSONObject()
                jsonEntrada.put("correoUser", txtCorr.text.toString())
                jsonEntrada.put("nomUser", txtNom.text.toString())
                jsonEntrada.put("passUser", txtPass.text.toString())
                jsonEntrada.put("altaUser", fechaAct)
                sendRequest(IP + "/miagendaplus/regUsuario.php", jsonEntrada)
            }
        }
    }

    // Metodo que envia la peticion al Web Service
    private fun sendRequest(wsUrl : String, jsonEnt : JSONObject) {

        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.POST, wsUrl, jsonEnt,
            Response.Listener { response ->

                val succ = response["status"]
                val msg = response["message"]

                Toast.makeText(this, "WS Say -> STATUS: $succ | MESSAGE: $msg", Toast.LENGTH_SHORT).show()

                // Dependiendo del estado que arroje el WS va realizar acciones en el dispositivo
                if (succ == 404 || succ == 201) { // 404: El usuario existe y no tiene ctos | 201: El usuario no existe pero se inserta uno nuevo

                    val user = response["user"]

                    var txtcorr = txtCorr.text.toString()
                    var txtpass = txtPass.text.toString()
                    var txtnom = txtNom.text.toString()

                    val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    var sentencia : String = "INSERT INTO usuarios(correo_user, nom_user, pass_user, alta_user) VALUES('$txtcorr', '$txtnom', '$txtpass', datetime('$fechaActual'))"

                    if (admin.Ejecuta(sentencia)) {

                        Toast.makeText(this, "Dispositivo: Usuario Guardado | Usuario: $user", Toast.LENGTH_LONG).show()
                        limpiar()
                    }

                    // Se inicializa el mainActivity y se finaliza el actual
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else if(succ == 200){ // El usuario existe y devuelve contactos

                    val user = response["user"]
                    val arrCtos = response.getJSONArray("contactos")

                    var txtcorr = txtCorr.text.toString()
                    var txtpass = txtPass.text.toString()
                    var txtnom = txtNom.text.toString()

                    val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    var sentencia : String = "INSERT INTO usuarios(correo_user, nom_user, pass_user, alta_user) VALUES('$txtcorr', '$txtnom', '$txtpass', datetime('$fechaActual'))"

                    if (admin.Ejecuta(sentencia)) {

                        Toast.makeText(this, "Dispositivo: Usuario Guardado | Usuario: $user", Toast.LENGTH_LONG).show()
                        limpiar()
                    }

                    // Se basean los datos json que arroja el WS para insertarlos en el dispositivo
                    for (i in 0 until arrCtos.length()){

                        val idCto = arrCtos.getJSONObject(i).getInt("idcto")
                        val nom = arrCtos.getJSONObject(i).getString("nomcto")
                        val dom = arrCtos.getJSONObject(i).getString("domcto")
                        val correo = arrCtos.getJSONObject(i).getString("correocto")
                        val tel = arrCtos.getJSONObject(i).getString("telcto")
                        val cel = arrCtos.getJSONObject(i).getString("celcto")
                        val cumple = arrCtos.getJSONObject(i).getString("cumplecto")
                        val alta = arrCtos.getJSONObject(i).getString("altacto")
                        val correoUser = arrCtos.getJSONObject(i).getString("correouser")

                        val sentencia = "INSERT INTO contactos(id_cto, nom_cto, dom_cto, correo_cto, tel_cto, cel_cto, cumple_cto, alta_cto, correo_user) " +
                                "VALUES($idCto, '$nom', '$dom', '$correo', '$tel', '$cel', '$cumple', '$alta', '$correoUser')"
                        if (admin.Ejecuta(sentencia)){
                            Toast.makeText(this, "Contacto "+ (i+1) +" cargado", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Se inicia el mainActivity y se finaliza el actual
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            },

            Response.ErrorListener{ error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
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