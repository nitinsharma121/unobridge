package com.provider.unobridge.providers.resources

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import com.provider.unobridge.providers.resources.ResourcesProvider

class ResourcesProviderImpl(private val context: Context) : ResourcesProvider {
    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun getStringArray(id: Int): Array<String> {
        return context.resources.getStringArray(id)
    }

    override fun getColor(id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.getColor(id)
        else context.resources.getColor(id)
    }

    override fun getFont(path: String): Typeface {
        return Typeface.createFromAsset(context.resources.assets,"fonts/" + path)
    }

    override fun getContext(): Context {
        return context
    }

    override fun getDrawable(id: Int): Drawable {
        return AppCompatResources.getDrawable(context, id)!!
    }

    override fun getInt(id: Int): Int {
        return context.resources.getInteger(id)
    }
}