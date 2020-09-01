package com.example.desafioeventos.view


import android.app.ProgressDialog
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.desafioeventos.R
import com.example.desafioeventos.adapter.EventAdapter
import com.example.desafioeventos.api.EventAPI
import com.example.desafioeventos.models.Event
import com.example.desafioeventos.util.Utilidades
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

/**
 * Created by Marcelo López on 01/09/2020
 */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Callback<List<Event>> {


    @BindView(R.id.toolbar)
    lateinit  var toolbar: Toolbar

    @BindView(R.id.drawer_layout)
    lateinit var drawer: DrawerLayout

    @BindView(R.id.nav_view)
    lateinit var navigationView: NavigationView

    @BindView(R.id.content_main_rv_events)
    lateinit var rvEvents: RecyclerView

    @BindView(R.id.content_main_tv_no_item)
    lateinit var tvNoItens: TextView

    lateinit var dialog: ProgressDialog
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var eventAdapter: EventAdapter
    var eventsList: List<Event> = ArrayList()
    var listCall : Call<List<Event>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        ButterKnife.setDebug(true)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        navigationView!!.setNavigationItemSelectedListener(this)

        rvEvents!!.setHasFixedSize(true)

        // use a linear layout manager
        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
        rvEvents!!.layoutManager = layoutManager
        rvEvents!!.itemAnimator = DefaultItemAnimator()
        // specify an adapter (see also next example)
        eventAdapter = EventAdapter(this@MainActivity, applicationContext, eventsList)
        rvEvents!!.adapter = eventAdapter

        LoadEvents()

    }

    @OnClick(R.id.fab)
    internal fun load(view: View) {
        LoadEvents()
    }


    private fun LoadEvents() {
        dialog = ProgressDialog.show(this@MainActivity, "", applicationContext.resources.getString(R.string.loading), true)
        dialog!!.show()

        val retrofit = Retrofit.Builder()
                .baseUrl(EventAPI.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        listCall = retrofit.create(EventAPI::class.java).getEvents()
        listCall!!.enqueue(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = menuItem.itemId

        if (id == R.id.nav_exit) {
            exit()
        } else if (id == R.id.nav_load) {
            LoadEvents()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun exit() {
        val dialogExit = ProgressDialog.show(this@MainActivity, "",
                applicationContext.resources.getString(R.string.exiting), true)

        dialogExit.show()
        val handler = Handler()
        handler.postDelayed({
            dialogExit.dismiss()
            finish()
        }, 3000)
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onFailure(call: Call<List<Event>>, t: Throwable) {
        Utilidades.showToast(resources.getString(R.string.failed_posts),this)
        eventsList
        eventsList = emptyList()
        dialog.dismiss()
        tvNoItens.visibility = View.VISIBLE
        rvEvents.visibility = View.GONE
    }

    override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
        dialog.dismiss()
        if (response.isSuccessful()) {
            eventsList = response.body()!!
            eventAdapter.setList(eventsList)
            eventAdapter.notifyDataSetChanged()
            tvNoItens.visibility = View.GONE
            rvEvents.visibility = View.VISIBLE

        }else{
            tvNoItens.visibility = View.VISIBLE
            rvEvents.visibility = View.GONE
        }
    }

}
