package com.tungnd.android.simplespriteanimation

import android.content.Context
import android.graphics.*

/**
 * @author Tung Nguyen
 * http://untamed.wild-refuge.net/rmxpresources.php?characters
 */
class Sprite : Runnable {
    private val walkingInterval = 1000L/4
    var movingDirection = Direction.DOWN
    private var frameToDraw = 0
    private var frameSize: Point
    private var sprite: Bitmap
    private var dst: Rect
    private var src: Rect
    private val pace = 1

    constructor(ctx: Context, id: Int, n_horizontal: Int = 4, n_vertical: Int = 4) {
        sprite = BitmapFactory.decodeResource(ctx.resources, id)
        frameSize = Point(sprite.width / n_horizontal, sprite.height / n_vertical)
        dst = Rect(0, 0, frameSize.x, frameSize.y)
        src = Rect(0, 0, frameSize.x, frameSize.y)
        Thread(this).start()
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(sprite, src, dst, null)
    }

    fun setDst(p: Point){
        this.dst.set(p.x - dst.width()/2, p.y - dst.height()/2, p.x + dst.width()/2, p.y + dst.height()/2)
    }

    override fun run() {
        while (true) {
            frameToDraw = ++frameToDraw % 4
            when (movingDirection) {
                Direction.RIGHT -> {
                    src.top = Direction.RIGHT.ordinal * frameSize.y
                    src.bottom = (Direction.RIGHT.ordinal + 1) * frameSize.y
                }
                Direction.LEFT -> {
                    src.top = Direction.LEFT.ordinal * frameSize.y
                    src.bottom = (Direction.LEFT.ordinal + 1) * frameSize.y
                    dst.right -= pace
                    dst.left -= pace
                }
                Direction.UP -> {
                    src.top = Direction.UP.ordinal * frameSize.y
                    src.bottom = (Direction.UP.ordinal + 1) * frameSize.y
                }
                Direction.DOWN -> {
                    src.top = Direction.DOWN.ordinal * frameSize.y
                    src.bottom = (Direction.DOWN.ordinal + 1) * frameSize.y
                }
            }
            src.left = frameToDraw * frameSize.x
            src.right = (frameToDraw + 1) * frameSize.x
            Thread.sleep(walkingInterval)
        }
    }

    fun move(){
        when (movingDirection) {
            Direction.RIGHT -> {
                dst.right += pace
                dst.left += pace
            }
            Direction.LEFT -> {
                dst.right -= pace
                dst.left -= pace
            }
            Direction.UP -> {
                dst.top -= pace
                dst.bottom -= pace
            }
            Direction.DOWN -> {
                dst.top += pace
                dst.bottom += pace
            }
        }
    }
}