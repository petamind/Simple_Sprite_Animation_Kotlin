package com.tungnd.android.simplespriteanimation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameView: SurfaceView, Runnable, SurfaceHolder.Callback, GameLoop{
    private var sprites = ArrayList<Sprite>()
    private var mCanvas: Canvas? = null
    private var mHolder: SurfaceHolder? = null
    private  var mThread: Thread
    private var myGestureDetector:GestureDetector

    constructor(ctx:Context): super(ctx)
    constructor(context: Context?, attributeSet: AttributeSet): super(context, attributeSet)

    init {
        mHolder = holder
        myGestureDetector = GestureDetector(context, MyGestureListener())
        mHolder?.addCallback(this)
        mThread = Thread(this)
        mThread.start()
    }

    override fun draw(){
        mCanvas?.drawColor(Color.WHITE)
        for(sprite in sprites)
            sprite.draw(mCanvas!!)
    }

    override fun update() {
        for(sprite in sprites)
            sprite.move()
    }

    override fun run() {
        while(true){
            if(mHolder?.surface?.isValid!!) {
                mCanvas = mHolder?.lockCanvas()
                synchronized(sprites)
                {
                    update()
                    draw()
                }
                mHolder?.unlockCanvasAndPost(mCanvas)
            }
            Thread.sleep(20)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        myGestureDetector.onTouchEvent(event)
        return true
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        for(sprite in sprites)
            sprite.setDst(Point(width/2, height/2))
    }

    inner class MyGestureListener: GestureDetector.SimpleOnGestureListener {
        constructor(): super()

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            for(sprite in sprites)
                sprite.movingDirection = sprite?.movingDirection?.next()!!
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            synchronized(sprites)
            {
                val sprite = Sprite(context, R.drawable.xmasgirl3)
                sprite.setDst(Point(e!!.x.toInt(), e!!.y.toInt()))
                sprites.add(sprite)

            }
            return true
        }

    }


}