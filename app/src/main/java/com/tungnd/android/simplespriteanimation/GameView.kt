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
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class GameView : SurfaceView, Runnable, SurfaceHolder.Callback, GameLoop {
    private var sprites = ArrayList<Sprite>()
    private var mCanvas: Canvas? = null
    private var mHolder: SurfaceHolder? = null
    private var mThread: Thread
    private var myGestureDetector: GestureDetector
    val FRAME_RATE = 60
    val MS_PER_SECOND = 1000L / FRAME_RATE

    constructor(ctx: Context) : super(ctx)
    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        mHolder = holder
        myGestureDetector = GestureDetector(context, MyGestureListener())
        mHolder?.addCallback(this)
        mThread = Thread(this)
        mThread.start()
    }

    override fun draw() {
        mCanvas?.drawColor(Color.WHITE)
        for (sprite in sprites)
            sprite.draw(mCanvas!!)
    }

    override fun update() {
        for (sprite in sprites)
            sprite.move()
    }

    override fun run() {
        var previous = System.currentTimeMillis()
        var lag = 0.0
        while (true) {

            val current = System.currentTimeMillis()
            val elapsed = current - previous
            previous = current.toLong()
            lag += elapsed

            if (mHolder?.surface?.isValid!!) {
                mCanvas = mHolder?.lockCanvas()
                synchronized(sprites)
                {
                    while (lag >= MS_PER_SECOND) {
                        update()
                        lag -= MS_PER_SECOND;
                    }

                    draw()
                }
                mHolder?.unlockCanvasAndPost(mCanvas)
            }
            //Thread.sleep(MS_PER_SECOND)
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
        for (sprite in sprites)
            sprite.setDst(Point(width / 2, height / 2))
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener {
        constructor() : super()

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            for (sprite in sprites)
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