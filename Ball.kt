package com.example.cvicenie5

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import kotlin.math.roundToInt
import kotlin.random.Random

class Ball(context : Context, val width : Int, val height : Int) {
        var x = width/2
        var y = height/2
        var size = 50
        var speed = 9
        var dx = 0
        var dy = 0
        var prehra = false
        var positive = Random.nextBoolean()
        var xaa = 0



    companion object { // staaticky blok

        var ballCounter = 0
        lateinit var bitmap : Bitmap
    }
    init {
        do {
            dx = speed
            dy = speed
        } while (dx == 0 && dy == 0)
        ballCounter++
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ball)
    }


    fun update(blocks : MutableList<Block>, pads :MutableList<Pad>, bricks : MutableList<Brick>) {
        var xaa = Random.nextInt(2)

        for (b in pads) if (collideL(b)) {
            when (xaa) {
                0 -> dy = -dy
                1 -> { dy = -dy
                    dx = -dx}
            }

        }

        for (b in blocks) if (collideB(b) && b.visible) {
            dx = -dx +1
            dy = -dy +1
            b.visible = false

        }

            if (x > width - size) dx = -dx
            else if (x < 0) dx = -dx
            else if (y > height - size) prehra = true
            else if (y < 0) dy = -dy




        x += dx
        y += dy


    }
    // prienik dvoch oblznikov
    fun collideB(block : Block) : Boolean =
//        block.isIn(x, y) || block.isIn(x, y+size) || block.isIn(x+size, y) || block.isIn(x+size, y+size)
          x < block.x + block.Xsize &&
          x + size > block.x &&
          y < block.y + block.Ysize &&
          y + size > block.y

    fun collideBr(brick : Brick) : Boolean =
//        block.isIn(x, y) || block.isIn(x, y+size) || block.isIn(x+size, y) || block.isIn(x+size, y+size)
            x < brick.x + brick.Xsize &&
                x + size > brick.x &&
                y < brick.y + brick.Ysize &&
                y + size > brick.y

    fun collideL(pad : Pad) : Boolean =
        pad.isInL(x, y) || pad.isInL(x, y+size) || pad.isInL(x+size/2, y) || pad.isInL(x+size/2, y+size)

   // fun collideP(pad : Pad) : Boolean =
   //     pad.isInP(x+size/2, y) || pad.isInP(x+size/2, y+size) || pad.isInP(x+size, y) || pad.isInP(x+size, y+size)



    fun isIn(eventx : Int, eventy : Int) : Boolean = // toto je stvorec, asi by to chcelo rovnicu kruznice
        x <= eventx && eventx <= x+size
                &&
        y <= eventy && eventy <= y+size

   fun draw(canvas: Canvas) {
       val mPaint = Paint()
       mPaint.color = Color.parseColor("#906090")
       //canvas.drawCircle((x+size/2).toFloat(), (y+size/2).toFloat(), (size/2).toFloat(), mPaint)
       canvas.drawBitmap(bitmap, null, Rect(x,y,x+size, y+size), mPaint)
   }
    fun Prehraj(): Boolean {
        return prehra
    }
}