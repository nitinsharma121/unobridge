package com.provider.unobridge.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.provider.unobridge.room.daos.CustomerDataDao
import com.provider.unobridge.room.daos.OrdersDataDao
import com.provider.unobridge.room.daos.ProfileDataDao
import com.provider.unobridge.room.daos.PromotoinsDataDao
import com.provider.unobridge.room.entities.CustomersData
import com.provider.unobridge.room.entities.OrdersData
import com.provider.unobridge.room.entities.ProfileData
import com.provider.unobridge.room.entities.PromotoinsData

/**
 * Created by Nitin SHarma on 8/1/2020.
 */
@Database(
    entities = [CustomersData::class, OrdersData::class, ProfileData::class, PromotoinsData::class],
    version = 1,
    exportSchema = false
)
public abstract class CalculatorDatabase : RoomDatabase() {

    abstract fun getCustomersDao(): CustomerDataDao
    abstract fun getOrdersDao(): OrdersDataDao
    abstract fun getProfileDao(): ProfileDataDao
    abstract fun getPromotoinsDao(): PromotoinsDataDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CalculatorDatabase? = null

        fun getDatabase(context: Context): CalculatorDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalculatorDatabase::class.java,
                    "unobridge_db"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}