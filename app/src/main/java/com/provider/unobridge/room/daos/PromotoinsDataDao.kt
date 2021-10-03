package com.provider.unobridge.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.provider.unobridge.room.entities.PromotoinsData

/**
 * Created by Nitin SHarma on 8/1/2020.
 */
@Dao
interface PromotoinsDataDao {


    @Query("SELECT * FROM PromotoinsData ")
    fun getPromotoins(): List<PromotoinsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPromotoin(list: PromotoinsData)


}