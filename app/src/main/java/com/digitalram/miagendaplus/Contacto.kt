package com.digitalram.miagendaplus

        /*
        * contactos:
        * nom_cto
        * ape_cto
        * cel_cto
        * correo_cto
        * tiene_whatsapp
        * id_user FOREIGN KEY (id_user) REFERENCES usuarios(id_user))
        */

class Contacto(id_cto: Int, nom_cto: String, dom_cto : String, cel_cto : String, tel_cto : String, correo_cto : String, cumple_cto : String, alta_cto : String, correo_user : String) {

    var idcto : Int = 0
    var nomcto : String = ""
    var domcto : String = ""
    var numcelcto : String = ""
    var numtelcto : String = ""
    var correocto : String = ""
    var cumplecto : String = ""

    var altacto : String = ""

    var correouser : String = ""

    init {

        this.idcto = id_cto
        this.nomcto = nom_cto
        this.domcto = dom_cto
        this.numcelcto = cel_cto
        this.numtelcto = tel_cto
        this.correocto = correo_cto
        this.cumplecto = cumple_cto

        this.altacto = alta_cto

        this.correouser = correo_user
    }
}