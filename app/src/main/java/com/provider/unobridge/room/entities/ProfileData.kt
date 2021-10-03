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
class ProfileData(

    @SerializedName("Id")
    @PrimaryKey(autoGenerate = true) val Id: Int?=null,

    @SerializedName("name")
    @ColumnInfo(name = "name") var name: String?=null,

    @SerializedName("mobileNo")
    @ColumnInfo(name = "mobileNo") val mobileNo: String?=null,

    @SerializedName("aadharNo")
    @ColumnInfo(name = "aadharNo") var aadharNo: String?=null,

    @SerializedName("panNo")
    @ColumnInfo(name = "panNo") var panNo: String?=null,

    @SerializedName("companyName")
    @ColumnInfo(name = "companyName") var companyName: String?=null,

    ) : Parcelable