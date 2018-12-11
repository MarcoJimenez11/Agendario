package com.example.marco.agendario

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.AdapterView
import kotlinx.android.synthetic.main.fragment_opciones.*
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_lista_tareas.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Opciones.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Opciones.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Opciones : Fragment() ,AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //Nada
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
        (activity as MainActivity).cambiarColor(pos)
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference
    var eventos : ArrayList<Tarea> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opciones, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var colores = arrayOf(getString(R.string.blanco), getString(R.string.azul), getString(R.string.verde), getString(R.string.amarillo), getString(R.string.rojo))
        spinnerColores!!.onItemSelectedListener = this
        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, colores)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerColores!!.adapter = aa

        //Firebase recycler
        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("eventos")

        recyclerEventos.layoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.VISIBLE
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                val gson= Gson()
                eventos.clear()
                for (obj in dataSnapshot.children){
                    val registro = obj.value
                    try {
                        val t : Tarea = gson.fromJson(registro.toString(), Tarea::class.java)
                        eventos.add(t)
                    }
                    catch (e: com.google.gson.JsonSyntaxException) {
                        Log.d("error",e.message)
                    }
                }
            }
            override fun onCancelled(databaseError : DatabaseError) {
                Log.d("error", databaseError.message)
            }
        }
        progressBar.visibility = View.INVISIBLE
        dbReference.addValueEventListener(menuListener)
        recyclerEventos.adapter = TareasAdapter(eventos,context!!)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Opciones.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Opciones().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
