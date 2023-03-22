package com.digitalram.miagendaplus

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AdminBD(context: Context): SQLiteOpenHelper(context, "BDMiAgendaPlus", null, 1){

    // Se sobrescribe el método onCreate para crear la base de datos
    override fun onCreate(db: SQLiteDatabase?) {

        // Se verifica que la base de datos no sea nula
        if (db != null) {

            // Se ejecutan sentencias SQL para crear tablas
            db.execSQL("CREATE TABLE usuarios(correo_user TEXT PRIMARY KEY UNIQUE," +
                    "nom_user TEXT, pass_user TEXT, alta_user DATE)")

            db.execSQL("CREATE TABLE contactos(id_cto INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nom_cto TEXT, dom_cto TEXT, correo_cto TEXT," +
                    "tel_cto TEXT, cel_cto TEXT, cumple_cto DATE, alta_cto DATE, correo_user TEXT" +
                    ", FOREIGN KEY (correo_user) REFERENCES usuarios(correo_user))")
        }
    }

    // Se sobrescribe el método onConfigure método que permite configurar ciertas opciones de la base de datos
    override fun onConfigure(db: SQLiteDatabase) {

        super.onConfigure(db)

        //Habilita las llaves foraneas en la base de datos
        db.execSQL("PRAGMA foreign_keys = ON;")
    }


    // Se define un método para ejecutar sentencias SQL en la base de datos
    fun Ejecuta(sentencia : String): Boolean{

        try {

            // Se obtiene una referencia a la base de datos en modo escritura
            val db = this.writableDatabase

            // Se ejecuta la sentencia SQL
            db.execSQL(sentencia)

            // Se cierra la base de datos
            db.close()

            // Se retorna true para indicar que la ejecución fue exitosa
            return true
        }
        // En caso de que se produzca una excepción, se captura y se maneja
        catch (ex : SQLiteException){

            // Se imprime un mensaje de error y se retorna false para indicar que la ejecución falló
            Log.d("Rabbit", ex.message.toString() + " --> " + sentencia)
            return false
        }
    }

    // Se define un método para consultar la base de datos y obtener un Cursor con los resultados
    fun Consultar(consulta : String): Cursor?{

        try {

            // Se obtiene una referencia a la base de datos en modo lectura
            val db = this.readableDatabase
            // Se ejecuta la consulta y se retorna el Cursor con los resultados
            return db.rawQuery(consulta, null)
        }

        // En caso de que se produzca una excepción, se captura y se maneja
        catch (ex : Exception){

            // Se imprime un mensaje de error y se retorna null para indicar que la consulta falló
            Log.d("Rabbit", ex.message.toString() + "-->" + consulta)
            return null
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}