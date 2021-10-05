package com.provider.unobridge.providers.resources

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable

interface ResourcesProvider {
    fun getString(id: Int): String
    fun getStringArray(id: Int): Array<String>
    fun getColor(id: Int): Int
    fun getInt(id: Int): Int
    fun getFont(path:String):Typeface
    fun getContext(): Context
    fun getDrawable(id:Int):Drawable
}