package com.provider.unobridge.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.provider.unobridge.room.entities.ProfileData

/**
 * Created by Nitin SHarma on 8/1/2020.
 */
@Dao
interface ProfileDataDao {


    @Query("SELECT * FROM ProfileData WHERE id=:id")
    fun getProfileData(id: String): ProfileData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveData(list: ProfileData):Long


}