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

    private lateinit var correoUser : String

    // Metodo de creacion de la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        //Inicializacion de componentes
        toolbar = findViewById(R.id.toolbar)
        btnAgregarContacto = findViewById(R.id.fabAgregarContacto)
        btnMenu = findViewById(R.id.fabMenu)

        //Inflar toolbar
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.menu)

        admin = AdminBD(this)

        if (!correoExiste()){

            startActivity(Intent(this,RegisterActivity::class.java))
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

    // Metodo de creacion del menu de opciones
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    fun btnAddContactoClick(view: View) {

        var actividad = Intent(this,ContactoActivity::class.java)

        actividad.putExtra("correoUser", correoUser)
        Toast.makeText(this, "correo de usuario: " + correoUser, Toast.LENGTH_SHORT).show()
        actividad.putExtra("accion", "agregar")
        startActivity(actividad)
    }

    private fun onItemClickListener(cto: Contacto) {

        var actividad = Intent(this, ContactoActivity::class.java)

        actividad.putExtra("idcto", cto.idcto.toString())

        actividad.putExtra("nomcto", cto.nomcto)
        actividad.putExtra("domcto", cto.domcto)
        actividad.putExtra("numcelcto", cto.numcelcto)
        actividad.putExtra("numtelcto", cto.numtelcto)
        actividad.putExtra("correocto", cto.correocto)
        actividad.putExtra("cumplecto", cto.cumplecto)

        actividad.putExtra("altacto", cto.altacto)

        actividad.putExtra("correoUser", cto.correouser)

        actividad.putExtra("accion", "descripcion")

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

        var listCto : MutableList<Contacto> = ArrayList()

        //                                  0           1           2       3          4           5           6               7              8
        val sentencia : String = "SELECT A.id_cto, A.nom_cto, A.dom_cto, A.cel_cto, A.tel_cto, A.correo_cto, A.cumple_cto, A.alta_cto, A.correo_user " +
                                    "FROM contactos AS A " +
                                    "INNER JOIN usuarios AS B ON A.correo_user = B.correo_user " +
                                    "WHERE B.correo_user = '$correoUser' ORDER BY A.nom_cto"

        var result : Cursor? = admin.Consultar(sentencia)

        if(result!=null) {
            while (result!!.moveToNext()) {

                val idc = result.getInt(0).toInt()

                val nomc = result.getString(1).toString()
                val domc = result.getString(2).toString()
                val numcelc = result.getString(3).toString()
                val numtelc = result.getString(4).toString()
                val correoc = result.getString(5).toString()
                val cumplec = result.getString(6).toString()
                val altac = result.getString(7).toString()

                val correou = result.getString(8).toString()

                listCto.add(Contacto(idc, nomc, domc, numcelc, numtelc, correoc, cumplec, altac, correou))
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

    // Verifica si existe un correo en la BD de SQLite
    fun correoExiste() : Boolean{

        val sentencia = "SELECT correo_user, alta_user FROM usuarios"
        val result: Cursor? = admin.Consultar(sentencia)

        if (result != null && result.moveToFirst()) {

            correoUser = result.getString(0).toString()

            Toast.makeText(this, "Bienvenido -> " + correoUser + " alta: " + result.getString(1), Toast.LENGTH_SHORT).show()
            result.close()
            return true
        }
        else{

            return false
        }
    }
}