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
class EmployeesData(

    @SerializedName("Id")
    @PrimaryKey(autoGenerate = true) val Id: Int?=null,

    @SerializedName("customer")
    @ColumnInfo(name = "customer") var customer: String?=null,

    @SerializedName("name")
    @ColumnInfo(name = "name") var name: String?=null,


    @SerializedName("contactNo")
    @ColumnInfo(name = "contactNo") var contactNo: String?=null,


    @SerializedName("address")
    @ColumnInfo(name = "address") var address: String?=null,


    @SerializedName("aadharNo")
    @ColumnInfo(name = "aadharNo") var aadharNo: String?=null,



    ) : Parcelable