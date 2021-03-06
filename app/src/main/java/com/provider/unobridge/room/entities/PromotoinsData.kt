package com.provider.unobridge.room.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Nitin SHarma on 8/1/2020.
 */
@Parcelize
@Entity
class PromotoinsData(

    @SerializedName("Id")
    @PrimaryKey(autoGenerate = true) val Id: Int? = null,

    @SerializedName("discountValue")
    @ColumnInfo(name = "discountValue") var discountValue: String? = null,
    @SerializedName("type")
    @ColumnInfo(name = "type") var type: String? = null,

    @SerializedName("expireDate")
    @ColumnInfo(name = "expireDate") var expireDate: String? = null,
    @SerializedName("title")
    @ColumnInfo(name = "title") var title: String?=null,

    @SerializedName("description")
    @ColumnInfo(name = "description") var description: String?=null,


    ) : Parcelable