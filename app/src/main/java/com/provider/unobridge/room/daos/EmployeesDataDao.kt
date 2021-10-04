package com.provider.unobridge.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.provider.unobridge.room.entities.EmployeesData

/**
 * Created by Nitin SHarma on 8/1/2020.
 */
@Dao
interface EmployeesDataDao {


    @Query("SELECT * FROM EmployeesData ")
    fun getEmployees(): List<EmployeesData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEmployee(list: EmployeesData)


}