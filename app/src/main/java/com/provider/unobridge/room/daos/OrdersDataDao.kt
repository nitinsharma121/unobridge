package com.provider.unobridge.room.daos

import androidx.room.*
import com.provider.unobridge.room.entities.OrdersData

/**
 * Created by Nitin SHarma on 8/1/2020.
 */
@Dao
interface OrdersDataDao {


    @Query("SELECT * FROM OrdersData ")
    fun getOrders(): List<OrdersData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrder(list: OrdersData)



}