package com.example.cvicenie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.LogPrinter
import java.io.File
import java.util.*
import kotlin.math.abs


var zaciatok = 0.0;
var prejdene = 0.0;
var najvyssi = 0.0;
var najnizsi = 0.0;
var zac = true;
var rozdiel = 0.0;
var prevydesnie = 0.0;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sc = Scanner(File("27.txt"))
        while (sc.hasNextLine()) {


            var line = sc.nextLine()
            var nextline = sc.nextLine()


            if (line.trim().length == 0) break
            if (nextline.trim().length == 0) break

            val parts = line.split(",")
            val nextparts = nextline.split(",")

            val datum = parts[0].toDouble();
            val cas = parts[1].toDouble();
            val lat1 = parts[2].toDouble();
            val lon1 = parts[3].toDouble();
            val nadmorska2 = parts[4].toDouble()

            val nextdatum = nextparts[0].toDouble();
            val nextcas = nextparts[1].toDouble();
            val nextlat1 = nextparts[2].toDouble();
            val nextlon1 = nextparts[3].toDouble();
            val nextnadmorska2 = nextparts[4].toDouble()

            var vzdialenost = distance(lat1,lon1,nextlat1,nextlon1)

            if (zac){
                zaciatok = nadmorska2
                zac = false
            }

            zaciatok = zaciatok - nadmorska2


            if (nadmorska2 > najvyssi){
                najvyssi = nadmorska2
            }
            if (nadmorska2 < najnizsi){
                najnizsi = nadmorska2
            }

            LogPrinter(1, vzdialenost.toString())
            LogPrinter(1, najvyssi.toString())
            LogPrinter(1, najnizsi.toString())
        }


        
}
    fun distance(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double) : Double =
        if (lat1 === lat2 && lon1 === lon2)
            0.0
        else
            Math.toDegrees(
                Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.cos(Math.toRadians(lon1 - lon2)))) * 60 * 1.1515 * 1.609344
}