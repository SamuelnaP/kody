package com.example.cvicenie5

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    var time = 0
    var paused = false
    var rest = false;
    var score = 0;
    lateinit var playground : Playground
    lateinit var mp : MediaPlayer
    lateinit var soundPool : SoundPool
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mp = MediaPlayer()
        soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)
        val shootID = soundPool.load(assets.openFd("shoot.ogg"),0)


        object : CountDownTimer(100000, 20) { // 50 Hz
            override fun onFinish() {
                for (b in playground.blocks){
                    if (!b.visible){
                        score += 1
                    }
                }

                if (score >= 25){
                    Toast.makeText(this@MainActivity, "U win", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@MainActivity, "Game over", Toast.LENGTH_LONG).show()
                }
                rest = true
                score = 0

            }
            override fun onTick(p0: Long) {
                if (!paused) {
                    runOnUiThread {
                        val playground = findViewById<Playground>(R.id.playground)
                        playground.update()
                        playground.invalidate()
                    }
                }
            }
        }.start()
        var timer = object : CountDownTimer(1000000, 1000) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Time is up", Toast.LENGTH_LONG).show()
            } // 1 Hz

            override fun onTick(p0: Long) {
                    runOnUiThread {

                        var playground = findViewById<Playground>(R.id.playground)



                        if (time == 1) {

                            playground.addBall()
                            playground.addPad()

                            for (x in 0 until 100) {
                                playground.addBlock(Random.nextInt(9)*100+100, Random.nextInt(11)*50)

                            }


                        }
                        if (playground.prehra){
                            Toast.makeText(this@MainActivity, "Stratil si 5 bodov", Toast.LENGTH_LONG).show()
                            playground.prehra = false
                        }

                        time++



                      //  val scoreTV = findViewById<TextView>(R.id.scoretv)
                       // scoreTV.setText("${Playground.score}")
                        playground.invalidate()
                    }
            }

        }.start()
    }



    fun Rest(view: View) {
        if (score == 0 && rest){
            object : CountDownTimer(100000, 20) { // 50 Hz
                override fun onFinish() {
                    for (b in playground.blocks){
                        if (!b.visible){
                            score += 1
                        }
                    }

                    if (score >= 25){
                        Toast.makeText(this@MainActivity, "U win", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@MainActivity, "Game over", Toast.LENGTH_LONG).show()
                    }
                    rest = true
                    score = 0

                }
                override fun onTick(p0: Long) {
                    if (!paused) {
                        runOnUiThread {
                            val playground = findViewById<Playground>(R.id.playground)
                            playground.update()
                            playground.invalidate()
                        }
                    }
                }
            }.start()
            var timer = object : CountDownTimer(1000000, 1000) {
                override fun onFinish() {
                    if (score >= 25){
                        Toast.makeText(this@MainActivity, "U win", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@MainActivity, "Game over", Toast.LENGTH_LONG).show()
                    }
                    rest = true
                    score = 0
                } // 1 Hz

                override fun onTick(p0: Long) {
                    runOnUiThread {

                        var playground = findViewById<Playground>(R.id.playground)



                        if (time == 1) {

                            playground.addBall()
                            playground.addPad()

                            for (x in 0 until 100) {
                                playground.addBlock(Random.nextInt(10)*100+100, Random.nextInt(10)*50)

                            }


                        }
                        if (playground.prehra){
                            Toast.makeText(this@MainActivity, "Stratil si 5 bodov", Toast.LENGTH_LONG).show()
                            playground.prehra = false
                        }

                        time++



                        //  val scoreTV = findViewById<TextView>(R.id.scoretv)
                        // scoreTV.setText("${Playground.score}")
                        playground.invalidate()
                    }
                }

            }.start()

        }else {


            playground = findViewById<Playground>(R.id.playground)
            playground.pads = emptyList<Pad>().toMutableList()
            playground.bricks = emptyList<Brick>().toMutableList()
            playground.balls = emptyList<Ball>().toMutableList()
            playground.blocks = emptyList<Block>().toMutableList()
            playground.addBall()
            playground.addPad()

            for (x in 0 until 100) {
                playground.addBlock(Random.nextInt(10) * 100 + 100, Random.nextInt(10) * 50)

            }
            score = 0
        }
    }
    fun Quit(view: View) {
        this@MainActivity.finish()
        exitProcess(0)

    }
    fun PridajLoptu(view:View){
        playground = findViewById<Playground>(R.id.playground)
        if (playground.balls.isEmpty()){
            playground.addBall()
        }else{
            Toast.makeText(this@MainActivity, "Uz mas loptu", Toast.LENGTH_LONG).show()
        }

    }



    override fun onPause() {
        super.onPause()
        //mp.release()
        soundPool.release()
    }



    override fun onRestart() {
        super.onRestart()

    }
}