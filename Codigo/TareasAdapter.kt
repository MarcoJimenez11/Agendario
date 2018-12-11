package com.example.marco.agendario

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fila_tarea.view.*

class TareasAdapter(val items : ArrayList<Tarea>, val context: Context) : RecyclerView.Adapter<ViewHolder>(){

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.fila_tarea, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.titulo?.text = items[position].titulo
        holder?.fecha?.text = "" + items[position].dia + " - " + mesEnTxt(items[position].mes) + " - " + items[position].anio
        //holder?.itemView?.setOnClickListener { Toast.makeText(context,  items.get(position).dato1, Toast.LENGTH_SHORT).show() }
    }

    fun mesEnTxt(m:Int):String{
        var mes = "Enero"
        when (m) {
            1 -> mes = "Enero"
            2 -> mes = "Febrero"
            3 -> mes = "Marzo"
            4 -> mes = "Abril"
            5 -> mes = "Mayo"
            6 -> mes = "Junio"
            7 -> mes = "Julio"
            8 -> mes = "Agosto"
            9 -> mes = "Septiembre"
            10 -> mes = "Octubre"
            11 -> mes = "Noviembre"
            12 -> mes = "Diciembre"
        }
        return mes
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    fun getTareaId(position: Int):Int{
        return items[position].id
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val titulo = view.tvTitulo
    val fecha = view.tvFecha
}