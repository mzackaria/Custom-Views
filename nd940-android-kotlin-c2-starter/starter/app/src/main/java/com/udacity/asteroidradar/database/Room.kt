package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface AsteroidDao {
    @Query("select * from databaseasteroid ORDER BY DATE(closeApproachDate) ASC ")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("delete from databaseasteroid where DATE(closeApproachDate) < DATE(:todayDate)")
    fun deleteOldAsteroid(todayDate: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids : DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao : AsteroidDao
}

private lateinit var INSTANCE : AsteroidDatabase

fun getDatabase(context: Context) : AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
            "asteroids").build()
        }
    }
    return INSTANCE
}