package com.websarve.wings.android.androidmapapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat

/**
 * GPS
 */
class GPSActivity : AppCompatActivity() {

    /**
     * 　_latitude
     * 　_longitude
     */
    private var _latitude = 0.0
    private var _longitude = 0.0

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("GPSActivity AppCompatActivity", "start!!")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)


        showPosition()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Log.i("GPSActivity AppCompatActivity", "end!!")
    }

    /**
     * showPosition
     */
    private fun showPosition(){
        Log.i("GPSActivity showPosition", "start!!")
        val tvLatitude = findViewById<TextView>(R.id.tvLatitude)
        val tvLongitude = findViewById<TextView>(R.id.tvLongitude)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener =  GPSLocationListener()

        if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED ){
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this@GPSActivity, permissions, 1000)
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 ,0f, locationListener)

        tvLatitude.text = "35.018060"
        tvLongitude.text = "138.933563"
        Log.i("GPSActivity showPosition", "end!!")
    }

    /**
     * onOptionsItemSelected
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("GPSActivity onOptionsItemSelected", "start!!")
        if(item.itemId == android.R.id.home){
            Log.i("GPSActivity onOptionsItemSelected", "finish !!")
            finish()
        }
        Log.i("GPSActivity onOptionsItemSelected", "end!!")
        return super.onOptionsItemSelected(item)
    }

    /**
     * onGPSMapButtonClick
     */
    fun onGPSMapButtonClick(view: View) {
        Log.i("GPSActivity onGPSMapButtonClick", "start!!")
        val uriStr = "geo:${_latitude}, ${_longitude}"
        Log.i("GPSActivity _latitude", "${_latitude}")
        Log.i("GPSActivity _longitude", "${_longitude}")
        val uri = Uri.parse(uriStr)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        Log.i("GPSActivity onGPSMapButtonClick", "startActivity!!")
        startActivity(intent)


        Log.i("GPSActivity onGPSMapButtonClick", "end!!")
    }

    /**
     * GPSLocationListener
     */
    private inner class GPSLocationListener : LocationListener{
        override fun onLocationChanged(location: Location) {
            Log.i("GPSActivity onLocationChanged", "start!!")
            val tvLatitude = findViewById<TextView>(R.id.tvLatitude)
            val tvLongitude = findViewById<TextView>(R.id.tvLongitude)
            tvLatitude.text = location.latitude.toString()
            tvLongitude.text = location.longitude.toString()
            _latitude =  location.latitude
            _longitude = location.longitude
            Log.i("GPSActivity onLocationChanged", "end!!")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle){}
        override fun onProviderEnabled(provider: String){}
        override fun onProviderDisabled(provider: String){}
    }

    /**
     * onRequestPermissionsResult
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray){
        Log.i("GPSActivity onRequestPermissionsResult", "start!!")
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val locationListener = GPSLocationListener()
            if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
                return
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            Log.i("GPSActivity onRequestPermissionsResult", "end!!")
        }
    }
}