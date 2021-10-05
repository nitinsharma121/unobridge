package com.provider.unobridge.data.model

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName

data class DrawerMenuItem(
    @field:SerializedName("icon") val icon: Drawable,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("navigationId") val navigationId: Int
)
