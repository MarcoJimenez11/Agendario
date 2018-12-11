package com.example.marco.agendario

import android.app.Notification
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.NotificationCompat
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Toast


class MainActivity : AppCompatActivity(), Lista_tareas.OnFragmentInteractionListener, Calendario.OnFragmentInteractionListener, Opciones.OnFragmentInteractionListener{

    private var bd : BD_Controller = BD_Controller(this)

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        bd = BD_Controller(this)
    }

    fun lanzarNotificacion(d:Int,m:Int,a:Int){
        if(bd.hayTareaHoy(d,m,a)){
            //1.Get reference to Notification Manager
            val mNotificationManager : NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            //2.Build Notification with NotificationCompat.Builder
            val notification : Notification = NotificationCompat.Builder(this)
                .setContentTitle("Agendario")                              //Titulo de notificación
                .setContentText("¡Tienes tarea pendiente para hoy!")       //Texto de la notificación
                .setSmallIcon(android.R.drawable.ic_menu_agenda)           //Icono
                .build()

            //Send the notification.
            mNotificationManager.notify(1, notification)
        }
    }

    fun getListaTareasBD(orden:String, filtro:String?) : ArrayList<Tarea>{
        var tareas = ArrayList<Tarea>()
        try {
            tareas = bd.listaTareas(orden,filtro)
        }catch (e: Exception){
            //Toast.makeText(this,  e.message, Toast.LENGTH_SHORT).show()
        }
        return tareas
    }

    fun insertTareaBD(t : Tarea){
        try {
            bd.insertarTarea(t)
        }catch (e: Exception){
            //Toast.makeText(context,  "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun borrarTareaBD(id:Int){
        try {
            bd.borrarTarea(id)
        }catch (e: Exception){
            //Toast.makeText(context,  "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun cambiarColor(n:Int){
        when (n) {
            0 -> container.setBackgroundColor(Color.WHITE)
            1 -> container.setBackgroundColor(Color.BLUE)
            2 -> container.setBackgroundColor(Color.GREEN)
            3 -> container.setBackgroundColor(Color.YELLOW)
            4 -> container.setBackgroundColor(Color.RED)
        }
    }

    override fun onBackPressed() {
        bd.close()
        super.onBackPressed()
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> Lista_tareas.newInstance("a","b")
                1 -> Calendario.newInstance("a","b")
                else -> Opciones.newInstance("s","s")
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }

    }

}

class NoSwipeViewPager(context: Context, attrs: AttributeSet): ViewPager(context, attrs) {
    private var swipeEnabled = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (swipeEnabled) {
            true -> super.onTouchEvent(event)
            false -> false
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return when (swipeEnabled) {
            true -> super.onInterceptTouchEvent(event)
            false -> false
        }
    }
    /* Por si quieres hacerlo swipeable en algun momento
    fun setSwipePagingEnabled(swipeEnabled: Boolean) {
        this.swipeEnabled = swipeEnabled
    }
    */
}