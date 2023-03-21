package com.digitalram.miagendaplus

import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digitalram.miagendaplus.ContactoAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var admin: AdminBD

    // Declaracion de componentes
    private lateinit var toolbar: Toolbar
    private lateinit var rvCtoList: RecyclerView
    private lateinit var btnAgregarContacto: FloatingActionButton
    private lateinit var btnMenu: FloatingActionButton

    // Declaracion de views
    private lateinit var viewAdapter : ContactoAdapter
    private lateinit var viewManager : RecyclerView.LayoutManager

    var contactoLista : List<Contacto> = ArrayList()

    //Metodo de creacion de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        //Inicializacion de componentes
        toolbar = findViewById(R.id.toolbar)

        //Inflar toolbar
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.menu)

        admin = AdminBD(this)

        var actividad = intent
        var usuariolog : Boolean

        usuariolog = actividad.getBooleanExtra("usuariolog", false)

        if (usuariolog == false){

            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        //---------------------------------------------------------------
        rvCtoList = findViewById<RecyclerView>(R.id.rvContactosLista)
        viewManager = LinearLayoutManager(this)
        viewAdapter = ContactoAdapter(contactoLista, this, {cto : Contacto -> onItemClickListener(cto)})
        rvCtoList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val indice = viewHolder.adapterPosition
                val cto = viewAdapter.getTasks()
                if (admin.Ejecuta("DELETE FROM contactos WHERE id_cto = " + cto[indice].idcto)) {

                    retriveContactos()
                }
            }

        }).attachToRecyclerView(rvCtoList)

        //---------------------------------------------------------------
    }

    //Metodo de creacion del menu de opciones
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    fun btnAddContactoClick(view: View) {
        var act = intent
        var actividad = Intent(this,ContactoActivity::class.java)

        actividad.putExtra("idUser", act.getStringExtra("idUser").toString())
        Toast.makeText(this, "id de usuario: " + act.getStringExtra("idUser"), Toast.LENGTH_SHORT).show()
        actividad.putExtra("accion", "agregar")
        startActivity(actividad)
    }

    private fun onItemClickListener(cto: Contacto) {

        var actividad = Intent(this, ContactoActivity::class.java)

        actividad.putExtra("idcto", cto.idcto)
        actividad.putExtra("nomcto", cto.nomcto)
        actividad.putExtra("apecto", cto.apecto)
        actividad.putExtra("numcelcto", cto.numcelcto)
        actividad.putExtra("correocto", cto.correocto)
        actividad.putExtra("tienewhats", cto.tienewhats)
        actividad.putExtra("iduser", cto.iduser)
        actividad.putExtra("accion", "descripción")

        startActivity(actividad)
    }

    override fun onResume() {
        super.onResume()
        retriveContactos()
    }

    private fun retriveContactos() {

        val ListaCto = llenaListaContactos()
        viewAdapter.setTask(ListaCto)
    }

    fun llenaListaContactos() : MutableList<Contacto>{

        var act = intent
        val iduser = act.getStringExtra("idUser").toString()

        var listCto : MutableList<Contacto> = ArrayList()

        val sentencia : String = "SELECT A.id_cto, A.nom_cto, A.ape_cto, A.numcel_cto, A.correo_cto, A.tiene_whatsapp, A.id_user" +
                                    "FROM Contactos AS A" +
                                    "INNER JOIN Usuarios AS B ON A.id_usuario = B.id_usuario" +
                                    "WHERE B.id_usuario = $iduser ORDER BY A.nom_cto"

        var result : Cursor? = admin.Consultar(sentencia)

        if(result!=null) {
            while (result!!.moveToNext()) {
                val idc = result.getInt(0).toInt()
                val nomc = result.getString(1).toString()
                val apec = result.getString(2).toString()
                val numcelc = result.getString(3).toString()
                val correoc = result.getString(4).toString()
                val tienew = result.getString(5).toString()
                val idu = result.getInt(6).toInt()

                listCto.add(Contacto(idc, nomc, apec, numcelc, correoc, tienew, idu))
            }
        }
        return listCto
    }

    fun btnMenuClick(view: View) {

        when{
            btnAgregarContacto.isEnabled == false && btnAgregarContacto.isVisible == false ->{

                btnAgregarContacto.isEnabled = true
                btnAgregarContacto.isVisible = true
                btnMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_menu_open_black, null))
            }
            else ->{

                btnAgregarContacto.isEnabled = false
                btnAgregarContacto.isVisible = false
                btnMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_menu_black, null))
            }
        }
    }
}