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
class OrdersData(

    @SerializedName("Id")
    @PrimaryKey(autoGenerate = true) val Id: Int?=null,

    @SerializedName("orderPrice")
    @ColumnInfo(name = "orderPrice") var orderPrice: String?=null,

    @SerializedName("customerName")
    @ColumnInfo(name = "customerName") var customerName: String?=null,

    @SerializedName("endDate")
    @ColumnInfo(name = "endDate") var endDate: String?=null,

    ) : Parcelable