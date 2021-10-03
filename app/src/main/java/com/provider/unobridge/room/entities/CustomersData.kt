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
class CustomersData(

    @SerializedName("Id")
    @PrimaryKey(autoGenerate = true) val Id: Int?=null,

    @SerializedName("name")
    @ColumnInfo(name = "name") var name: String?=null,

    @SerializedName("mobileNo")
    @ColumnInfo(name = "mobileNo") var mobileNo :String?=null,

    @SerializedName("isSelected")
    @ColumnInfo(name = "isSelected") var isSelected :Boolean=false,

    ) : Parcelable