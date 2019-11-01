package com.tungnd.android.simplespriteanimation

enum class Direction {
    DOWN,LEFT, RIGHT, UP;

    fun next(): Direction{
        when(ordinal){
            0 -> return LEFT
            1 -> return UP
            2 -> return DOWN
            else  -> return RIGHT
        }
    }
}