package com.example.marco.agendario

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_lista_tareas.*
import java.util.*






// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Lista_tareas.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Lista_tareas.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Lista_tareas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var tareas: ArrayList<Tarea> = ArrayList()
    private var adaptador : TareasAdapter? = null
    private var orden : String = "anio,mes,dia"
    private var vista : Int = 1
    private var filtro: String? = null
    private var todayDia : Int = 1
    private var todayMes : Int = 1
    private var todayAnio : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_tareas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addTareas()
        recyclerTareas.layoutManager = LinearLayoutManager(context)
        recyclerTareas.layoutManager = GridLayoutManager(context,vista)
        adaptador= TareasAdapter(tareas, context!!)
        recyclerTareas.adapter = adaptador
        initSwipe()

        btnOrdenar.setOnClickListener {
            cambiarOrden()
        }
        btnVista.setOnClickListener {
            cambiarVista()
        }
        btnFiltrar.setOnClickListener {
            cambiarFiltro()
        }

        val currentTime = Calendar.getInstance()
        todayDia = currentTime.get(Calendar.DAY_OF_MONTH)
        todayMes = currentTime.get(Calendar.MONTH)+1
        todayAnio = currentTime.get(Calendar.YEAR)
        (activity as MainActivity).lanzarNotificacion(todayDia,todayMes,todayAnio)
    }

    //Esto que me ha costado la vida es para actualizar el recycler al pasar del tab contiguo
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            refrescarRecycler()
        }
    }

    fun refrescarRecycler() {
        if(recyclerTareas != null && recyclerTareas.adapter != null){
            addTareas()
            recyclerTareas.adapter!!.notifyDataSetChanged()
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    private fun addTareas(){
        tareas.clear()
        val tar = (activity as MainActivity).getListaTareasBD(orden,filtro)
        if (tar.size > 0) {
            var t : Tarea
            for (item in tar.iterator()) {
                t = Tarea(item.id, item.titulo, item.dia,item.mes,item.anio)
                tareas.add(t)
            }
        }
    }

    private fun cambiarOrden(){
        if(orden == "titulo") {
            orden = "anio,mes,dia"
            tvOrden.text = getString(R.string.orden_fecha)
        }
        else {
            orden = "titulo"
            tvOrden.text = getString(R.string.orden_titulo)
        }
        refrescarRecycler()
    }

    private fun cambiarVista(){
        vista++
        if(vista > 4)
            vista = 1
        recyclerTareas.layoutManager = GridLayoutManager(context,vista)
    }

    private fun cambiarFiltro(){
        if(filtro == null) {
            filtro = "dia = $todayDia and mes = $todayMes and anio = $todayAnio"
            tvFiltro.text = getString(R.string.filtro_dia)
        }
        else if(filtro == "dia = $todayDia and mes = $todayMes and anio = $todayAnio"){
            filtro = "mes = $todayMes and anio = $todayAnio"
            tvFiltro.text = getString(R.string.filtro_mes)
        }
        else{
            filtro = null
            tvFiltro.text = getString(R.string.filtro_nada)
        }
        refrescarRecycler()
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
         * @return A new instance of fragment Lista_tareas.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Lista_tareas().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initSwipe() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val id = adaptador!!.getTareaId(position)
                adaptador!!.removeItem(position)
                (activity as MainActivity).borrarTareaBD(id)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerTareas)
    }
}
