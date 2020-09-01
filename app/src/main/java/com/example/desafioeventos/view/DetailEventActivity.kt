package com.example.desafioeventos.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.desafioeventos.BuildConfig
import com.example.desafioeventos.R
import com.example.desafioeventos.adapter.AdapterGeneric
import com.example.desafioeventos.api.EventAPI
import com.example.desafioeventos.models.Cupom
import com.example.desafioeventos.models.EventDetail
import com.example.desafioeventos.models.Person
import com.example.desafioeventos.util.Utilidades
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.util.StorageUtils.getStorage
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Marcelo López on 01/09/2020
 */

class DetailEventActivity : AppCompatActivity() {

    private var osmMapView: MapView? = null
    private var controller: IMapController? = null
    private var projection: Projection? = null

    val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val df = DecimalFormat("0.##")
    lateinit var date: Date
    lateinit var animation_in: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val provider = Configuration.getInstance()
        provider.userAgentValue = BuildConfig.APPLICATION_ID
        provider.osmdroidBasePath = getStorage()
        provider.osmdroidTileCache = getStorage()
        animation_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        checkPermissions()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val intent = intent
        //val event = intent.extras!!.getSerializable("event") as Event?
        val event = intent.getParcelableExtra<EventDetail>("event")

        val detailt_iv_picture = findViewById<View>(R.id.detailt_iv_picture) as ImageView
        val share = findViewById<View>(R.id.share) as ImageView
        val checkin = findViewById<View>(R.id.checkin) as ImageView
        val people = findViewById<View>(R.id.iv_people) as ImageView
        val cupons = findViewById<View>(R.id.iv_cupons) as ImageView
        val detailt_tv_name = findViewById<View>(R.id.detail_tv_name) as TextView
        val detail_tv_description = findViewById<View>(R.id.detail_tv_description) as TextView
        val detail_tv_date = findViewById<View>(R.id.detail_tv_date) as TextView
        val detail_tv_price = findViewById<View>(R.id.detail_tv_price) as TextView
        osmMapView = findViewById<View>(R.id.osm_map_view) as MapView


        osmMapView!!.setTileSource(TileSourceFactory.MAPNIK)
        osmMapView!!.setBuiltInZoomControls(true)
        osmMapView!!.setMultiTouchControls(true)

        projection = osmMapView!!.projection

        controller = osmMapView!!.controller
        controller!!.setZoom(16)


        Picasso.with(applicationContext)
                .load(event!!.image)
                .placeholder(applicationContext.resources.getDrawable(R.drawable.event))
                .error(applicationContext.resources.getDrawable(R.drawable.event))
                .into(detailt_iv_picture)

        detailt_tv_name.text = event.title
        detail_tv_description.text = event.description
        date = Date(event.date!!)
        detail_tv_date.text = format.format(date) + "Hs"
        detail_tv_price.text = "Preço R$: " + df.format(event.price)

        val geoPoint = GeoPoint((event.latitude!! * 1E6).toInt(), (event.longitude!! * 1E6).toInt())
        controller!!.animateTo(geoPoint)
        addMarker(geoPoint)

        people.setOnClickListener {
            people.startAnimation(animation_in)
            openDialogPeople(event.people)
        }

        share.setOnClickListener {
            share.startAnimation(animation_in)
            shareEvent(event)
        }

        checkin.setOnClickListener {
            checkin.startAnimation(animation_in)
            openDialogCheckin(event)
        }

        cupons.setOnClickListener {
            cupons.startAnimation(animation_in)
            openDialogDiscounts(event.cupons)
        }

    }

    private fun openDialogPeople(people: List<Person>) {
        val mBuilder = AlertDialog.Builder(this@DetailEventActivity)
        val mView = layoutInflater.inflate(R.layout.dialog_people, null)
        val listPeople = mView.findViewById<View>(R.id.lvPeople) as ListView
        val btFechar = mView.findViewById<View>(R.id.btFechar) as Button

        val adapterPeople = AdapterGeneric(applicationContext, null, people)
        listPeople.adapter = adapterPeople
        adapterPeople.notifyDataSetChanged()


        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        dialog.setCancelable(false)
        dialog.show()

        btFechar.setOnClickListener {
            btFechar.startAnimation(animation_in)
            dialog.dismiss()
        }

    }


    private fun openDialogDiscounts(cupons: List<Cupom>) {
        val mBuilder = AlertDialog.Builder(this@DetailEventActivity)
        val mView = layoutInflater.inflate(R.layout.dialog_people, null)
        val listPeople = mView.findViewById<View>(R.id.lvPeople) as ListView
        val btFechar = mView.findViewById<View>(R.id.btFechar) as Button

        val adapterPeople = AdapterGeneric(applicationContext, cupons, null)
        listPeople.adapter = adapterPeople
        adapterPeople.notifyDataSetChanged()

        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        dialog.setCancelable(false)
        dialog.show()

        btFechar.setOnClickListener {
            btFechar.startAnimation(animation_in)
            dialog.dismiss()
        }

    }

    private fun openDialogCheckin(event: EventDetail) {
        val mBuilder = AlertDialog.Builder(this@DetailEventActivity)
        val mView = layoutInflater.inflate(R.layout.dialog_checkin, null)
        val etNome = mView.findViewById<View>(R.id.etNome) as EditText
        val etEmail = mView.findViewById<View>(R.id.etEmail) as EditText
        val btCheckin = mView.findViewById<View>(R.id.btCheckin) as Button

        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        dialog.show()
        btCheckin.setOnClickListener {
            btCheckin.startAnimation(animation_in)
            if (!etNome.text.toString().isEmpty() && !etEmail.text.toString().isEmpty()) {
                val retrofit = Retrofit.Builder()
                        .baseUrl(EventAPI.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                val eventAPI = retrofit.create<EventAPI>(EventAPI::class.java!!)

                try {
                    val call = eventAPI.sendChekin(event.id, etNome.text.toString(), etEmail.text.toString());
                    call.enqueue(callback { response, thorwable ->
                        response?.let {
                            if (response.isSuccessful) {
                                if (response.message().equals("Created")) {
                                    Utilidades.showToast(resources.getString(R.string.success_checckin), this)
                                } else {
                                    Utilidades.showToast(resources.getString(R.string.error_checkin_response) +
                                            "Resposta: " + response.message() +
                                            "Code: " + response.code(), this)
                                }
                            } else {
                                Utilidades.showToast(resources.getString(R.string.error_checkin_response) +
                                        "Resposta: " + response.message() +
                                        "Code: " + response.code(), this)
                            }
                            dialog.dismiss()
                        }
                        thorwable?.let {
                            Utilidades.showToast(resources.getString(R.string.error_checkin_response) + it.message.toString(), this)
                        }
                    })
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Utilidades.showToast(resources.getString(R.string.error_checkin), this)
            }
        }
    }

    private fun shareEvent(event: EventDetail) {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = event.description
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, event.title)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, format.format(date) + "Hs \n" + shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Compartilhar"))
    }


    fun <T> callback(callResponse: (response: Response<T>?,
                                    throwable: Throwable?) -> Unit): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                callResponse(response, null)
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                callResponse(null, t)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    fun checkPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }

    fun addMarker(center: GeoPoint) {
        val marker = Marker(osmMapView!!)
        marker.position = center
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        osmMapView!!.overlays.clear()
        osmMapView!!.overlays.add(marker)
        osmMapView!!.invalidate()
        marker.icon = resources.getDrawable(android.R.drawable.ic_menu_myplaces)
    }

}
