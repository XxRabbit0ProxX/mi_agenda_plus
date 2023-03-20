package com.digitalram.miagendaplus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//-----------------------------------------------------------
//    Adaptador del RecyclerView
//    Es la clase que hace de puente entre la vista
//    (el recyclerview) y los datos
//
//-----------------------------------------------------------
class ContactoAdapter(private var mLista:List<Contacto>,
                      private val mContext: Context, private val clickListener: (Contacto) -> Unit)
    : RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>() {

    /**
     * El layout manager invoca este método para renderizar cada elemento
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return ContactoViewHolder(layoutInflater.inflate(R.layout.celda_prototipo, parent, false))
    }

    /**
     * Este método asigna valores para cada elemento de la lista
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        holder.bind(mLista[position], mContext, clickListener)
    }

    /**
     * El método getItemCount() Cantidad de elementos del RecyclerView
     */
    override fun getItemCount(): Int = mLista.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de Productos
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(lista: List<Contacto>){
        mLista = lista
        notifyDataSetChanged()
    }

    fun getTasks(): List<Contacto> = mLista

    /*
     * Clase interna para asignar los valores a los textView definidos en la celda_prototipo
     */
    class ContactoViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val subTitulo: TextView = itemView.findViewById(R.id.tvSubtitulo)

        fun bind (cont:Contacto, context: Context, clickListener: (Contacto) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            titulo.text = cont.nomcto.toString()
            subTitulo.text = cont.numcelcto.toString()

            itemView.setOnClickListener{ clickListener(cont)}
        }
    }
}