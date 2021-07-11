package com.example.cvicenie5

import android.content.Context
import android.graphics.*


class Pad(context : Context, val width : Int, val height : Int) {
    var Xsize = 200
    var Ysize = 30
    var y = height-Ysize
    var x = width/2 -Xsize/2


    companion object { // staaticky blok

        lateinit var bitmapp : Bitmap
    }
    init {
        bitmapp = BitmapFactory.decodeResource(context.resources, R.drawable.padd)
    }


    fun updateRight() {
        if (x < width-Xsize-width/10) {
            x += width / 10
        }

    }
    fun updateLeft() {
        if (x > 0+Xsize/2) {
            x -= width / 10
        }
    }
    fun isInL(eventx : Int, eventy : Int) : Boolean =
        x <= eventx && eventx <= x+Xsize && y <= eventy && eventy <= y + Ysize
  //  fun isInP(eventx : Int, eventy : Int) : Boolean =
   //     x + Xsize/2 <= eventx && eventx <= x+Xsize && y <= eventy && eventy <= y+Ysize


    fun draw(canvas: Canvas) {
        val mPaint = Paint()
        mPaint.color = Color.parseColor("#906090")
        //canvas.drawCircle((x+size/2).toFloat(), (y+size/2).toFloat(), (size/2).toFloat(), mPaint)
        canvas.drawBitmap(Pad.bitmapp, null, Rect(x,y,(x+Xsize), (y+Ysize)), mPaint)


    }
}