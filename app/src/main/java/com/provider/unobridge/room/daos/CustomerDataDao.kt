package com.provider.unobridge.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.provider.unobridge.room.entities.CustomersData

/**
 * Created by Nitin SHarma on 8/1/2020.
 */
@Dao
interface CustomerDataDao {


    @Query("SELECT * FROM CustomersData ")
    fun getCustomers(): List<CustomersData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCustomer(list: CustomersData)


}