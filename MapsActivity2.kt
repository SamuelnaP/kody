package com.example.cvicenie9

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.FusedLocationApi
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Math.sqrt

class MapsActivity : AppCompatActivity(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener, // pozor, nie iny LocationListener
    ResultCallback<LocationSettingsResult>,
    OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    //private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111
    private val REQUEST_CHECK_SETTINGS = 222
    private val CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000
    var prvalat = 0.00
    var prvalon = 0.00
    var druhalat = 0.00
    var druhalon = 0.00
    var aktualna = 0.00
    var zac = true
    var rychlost = 0.00
    var nadmorska = 0.00
    var maxspeed = 0.00
    var pocetZmienLokacie = 0.00
    var priemernaRychlost = 0.00
    var vsetkyRychlosti = 0.00
    var vyska = 0.00
    var minVyska = 1000000.00
    var maxVyska = 0.00


    lateinit var mGoogleApiClient: GoogleApiClient
    val TAG = "CV9"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        // -- permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                            this@MapsActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                        this, // Activity
                        arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION
                )
            }
        }
        //----
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener { latlng ->
            Log.d("MapV2", "#LONG" + latlng.latitude + ":" + latlng.longitude)
        }
        setUpMapIfNeeded()
        // Check if has GPS
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private fun checkPlayServices(): Boolean {
        val apiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode: Int = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
            } else {
                finish()
            }
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (!checkPlayServices()) {
            Log.d(TAG, "You need to install Google Play Services to use the App properly")
        }
    }

    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
            mGoogleApiClient.disconnect()
        }
    }

    private fun setUpMapIfNeeded() {
        val sf = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        sf.getMapAsync(this)
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    override fun onResult(locationSettingsResult: LocationSettingsResult) {
        Log.d(TAG, "onResult")
        val status = locationSettingsResult.status
        when (status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS -> {
            }
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                try {
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (e: IntentSender.SendIntentException) {
                }
            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed")
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST)
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.errorCode)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(bundle: Bundle?) {
        Log.d(TAG, "onConnected")
        val interval = (10 * 1000).toLong()   // 10 seconds, in milliseconds
        val fastestInterval = (1 * 1000).toLong()  // 1 second, in milliseconds
        val minDisplacement = 0f

        val mLocationRequest1 = LocationRequest.create()
                //.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setSmallestDisplacement(minDisplacement)

        val mLocationRequest2 = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                //           LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                //           LocationRequest.PRIORITY_HIGH_ACCURACY;
                .setInterval((5 * 1000).toLong()  /* 5 secs */)
                .setFastestInterval((2 * 1000).toLong() /* 2 sec */)

        FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, mLocationRequest1, this@MapsActivity)
    }

    private fun handleNewLocation(location: Location) {
        Log.d(TAG, location.toString())
        val latLng = LatLng(location.latitude, location.longitude)

        val options = MarkerOptions()
                .position(latLng)
                .title("position")
        mMap.clear()
        mMap.addMarker(options)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))


    }

    override fun onLocationChanged(location: Location) {

        pocetZmienLokacie++

        if (zac){
            prvalat = location.latitude
            prvalon = location.longitude
        }
        zac = false

        Log.d(TAG, location.toString())
        handleNewLocation(location)
        druhalat = location.latitude
        druhalon = location.longitude



        rychlost = Speed(prvalat,prvalon,druhalat,druhalon)


        vsetkyRychlosti += rychlost

        if (rychlost > maxspeed){
            maxspeed = rychlost
        }

        vyska = location.altitude

        if (vyska >= maxVyska){
            maxVyska = vyska
        }
        if (vyska <= minVyska){
            minVyska = vyska
        }

        vyska = maxVyska - minVyska //* -1.00                                       //////////////////////////////////////////////////////////



        priemernaRychlost = vsetkyRychlosti / pocetZmienLokacie

        var tv6: TextView = findViewById<View>(R.id.textView6) as TextView

        tv6.setText("Priemerna: " + priemernaRychlost + " m/s")

        var tv3: TextView = findViewById<View>(R.id.textView3) as TextView

        tv3.setText("Maximalna: " + maxspeed + " m/s")


        var tv5: TextView = findViewById<View>(R.id.textView5) as TextView

        tv5.setText("Aktualna: " + rychlost + " m/s")

        var tv4: TextView = findViewById<View>(R.id.textView4) as TextView

        tv4.setText("Prejdena vzdialenost: " + aktualna + " km")

        //vypis
        var tv7: TextView = findViewById<View>(R.id.textView7) as TextView

        tv7.setText("Azimut: " + location.bearing + " %")

        var tv8: TextView = findViewById<View>(R.id.textView8) as TextView

        tv8.setText("Latitude: " + location.latitude + "lat")

        var tv9: TextView = findViewById<View>(R.id.textView9) as TextView

        tv9.setText("Longitude: " + location.longitude + "long")

        var tv10: TextView = findViewById<View>(R.id.textView10) as TextView

        tv10.setText("Altitude: " + location.altitude)

        var tv11: TextView = findViewById<View>(R.id.textView11) as TextView

        tv11.setText("Prevysenie: " + vyska)




        aktualna += distance(prvalat,prvalon,druhalat,druhalon)
  //      setContentView(tv1)
        prvalat = druhalat
        prvalon = druhalon



    }

    override fun onConnectionSuspended(i: Int) {
    }

    private fun Speed(one1: Double,one2: Double, two1: Double,two2: Double): Double {
        val R = 6371000
        val dLat = toRad(two1 - one1)
        val dLon = toRad(two2 - one2)
        val lat1 = toRad(one1)
        val lat2 = toRad(two1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2))
        val c = 2 * Math.atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    private fun toRad(d: Double): Double {
        return d * Math.PI / 180
    }

    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double =
            if (lat1 === lat2 && lon1 === lon2)
                0.0
            else
                Math.toDegrees(
                        Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                                Math.cos(Math.toRadians(lon1 - lon2)))) * 60 * 1.1515 * 1.609344

}