package com.example.cvicenie5

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class Playground(internal var context: Context, attrs: AttributeSet)
    : View(context, attrs),
    View.OnKeyListener {

    var balls = emptyList<Ball>().toMutableList()
    var blocks = emptyList<Block>().toMutableList()
    var bricks = emptyList<Brick>().toMutableList()
    var pads = emptyList<Pad>().toMutableList()
    var pwidth = 0
    var pheight = 0
    var prehra = false
    var HP = 5
    var skore = 0




    companion object {  // staticke premenne
        var score = 0
    }

    fun addBall() {
        if (pwidth == 0 || pheight == 0)
            return
        balls.add(Ball(context, pwidth, pheight))
    }

    fun addPad() {
        if (pwidth == 0 || pheight == 0)
            return
        pads.add(Pad(context, pwidth, pheight))
    }



    fun addBlock(xx:Int, yy:Int) {
        if (pwidth == 0 || pheight == 0)
            return
        blocks.add(Block(context, pwidth, pheight,xx,yy))
    }

    fun addBrick(xx:Int, yy:Int) {
        if (pwidth == 0 || pheight == 0)
            return
        bricks.add(Brick(context, pwidth, pheight,xx,yy))
    }

    fun update() {




        for (b in balls){
            b.update(blocks,pads,bricks)
            b.speed +=1

        }




    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        pwidth = widthMeasureSpec
        pheight = heightMeasureSpec
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pwidth = w
        pheight = h
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (b in balls) {
            if (b.prehra){
                prehra = true
                balls.remove(b)

            }
                b.draw(canvas)
        }
        for (b in blocks) {
            if (b.visible) {
                b.draw(canvas)
            }
        }
        for (b in pads) {
            b.draw(canvas)
        }
        for (b in bricks) {
            if (b.visible)
                b.draw(canvas)
        }



    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            for (b in pads) {
                if (event.x.toInt() < width/2) {
                    b.updateLeft()
                }
                if (event.x.toInt() > width/2) {
                    b.updateRight()
                }
            }

        }
        return super.onTouchEvent(event)
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }

    fun Prehraj(): Boolean {
        return prehra
    }


}
