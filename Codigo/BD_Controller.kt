package com.example.marco.agendario


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import java.util.ArrayList

class BD_Controller(context: Context) : SQLiteOpenHelper(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS) {

    companion object {
        private var tarea: Tarea? = null
        private val VERSION_BASEDATOS = 1
        private val NOMBRE_BASEDATOS = "BD_Agendario.db"
        private val NOMBRE_TABLA = "Tareas"
        private val ins = "CREATE TABLE Tareas (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo VARCHAR(30), dia INT, mes INT, anio INT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ins)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA")
        onCreate(db)
    }

    fun insertarTarea(t: Tarea): Long {
        var nreg_afectados: Long = -1
        val db = writableDatabase
        if (db != null) {
            val valores = ContentValues()

            valores.put("titulo", t.titulo)
            valores.put("dia", t.dia)
            valores.put("mes", t.mes)
            valores.put("anio", t.anio)

            try {
                nreg_afectados = db.insert("$NOMBRE_TABLA", null, valores)
            }catch(E: Exception){

            }
        }
        db!!.close()
        return nreg_afectados
    }

    fun borrarTarea(id:Int): Long {
        var nreg_afectados: Long = -1
        val db = writableDatabase
        if (db != null) {
            nreg_afectados = db.delete("$NOMBRE_TABLA", "id = $id", null).toLong()
        }
        db!!.close()
        return nreg_afectados
    }

    fun hayTareaHoy(dia:Int, mes:Int, anio:Int): Boolean {
        var hayTarea = false
        val db = readableDatabase
        if (db != null) {
            val campos = arrayOf("id","titulo", "dia", "mes", "anio")
            val c = db.query("$NOMBRE_TABLA", campos, "dia=$dia and mes=$mes and anio=$anio", null, null, null, null, null)
            if (c.moveToFirst())
                hayTarea = true
            c.close()
        }
        db.close()
        return hayTarea
    }

    fun listaTareas(orden: String, filtro : String?): ArrayList<Tarea> {
        val db = readableDatabase
        var lista_tareas = ArrayList<Tarea>()

        if (db != null) {
            val campos = arrayOf("id","titulo", "dia", "mes", "anio")
            val c = db.query(
                "$NOMBRE_TABLA", campos, filtro, null, null, null,
                orden, null
            )
            if (c.moveToFirst()) {
                do {
                    tarea = Tarea(c.getInt(0),c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4))
                    lista_tareas.add(tarea!!)
                } while (c.moveToNext())
            }
            c.close()
        }
        db!!.close()
        return lista_tareas
    }

}
