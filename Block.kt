package com.example.cvicenie5

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import kotlin.random.Random

class Block(context : Context, val width : Int, val height : Int, val xx:Int,val yy:Int) {
        var x = xx
        var y = yy
        var Xsize = 100
        var Ysize = 50
        var speed = 2 + Random.nextInt(8)
        var visible = true

    companion object { // staaticky blok
        var ballCounter = 0
        lateinit var bitmapb : Bitmap
    }
    init {

        bitmapb = BitmapFactory.decodeResource(context.resources, R.drawable.brik)
    }
    fun update() {
    }
    fun isIn(eventx : Int, eventy : Int) : Boolean = // toto je stvorec, asi by to chcelo rovnicu kruznice
        x <= eventx && eventx <= x+Xsize
                &&
        y <= eventy && eventy <= y+Ysize
   fun draw(canvas: Canvas) {
       val mPaint = Paint()
       mPaint.color = Color.parseColor("#906090")
       //canvas.drawCircle((x+size/2).toFloat(), (y+size/2).toFloat(), (size/2).toFloat(), mPaint)
      // canvas.drawRect(x.toFloat(),y.toFloat(), (x+Xsize).toFloat(), (y+Ysize).toFloat(), mPaint)
       canvas.drawBitmap(bitmapb, null, Rect(x,y,(x+Xsize), (y+Ysize)), mPaint)


   }
}