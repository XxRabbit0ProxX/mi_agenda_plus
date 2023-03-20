package com.digitalram.miagendaplus

        /*
        * contactos:
        * nom_cto
        * ape_cto
        * numcel_cto
        * correo_cto
        * tiene_whatsapp
        * id_user FOREIGN KEY (id_user) REFERENCES usuarios(id_user))
        */

class Contacto(id_cto: Int, nom_cto: String, ape_cto : String, numcel_cto : String, correo_cto : String, tiene_whatsapp : String, id_user : Int) {

    var idcto : Int = 0
    var nomcto : String = ""
    var apecto : String = ""
    var numcelcto : String = ""
    var correocto : String = ""
    var tienewhats : String = ""
    var iduser : Int = 0

    init {

        this.idcto = id_cto
        this.nomcto = nom_cto
        this.apecto = ape_cto
        this.numcelcto = numcel_cto
        this.correocto = correo_cto
        this.tienewhats = tiene_whatsapp
        this.iduser = id_user
    }
}