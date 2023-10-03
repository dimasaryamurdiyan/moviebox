package com.singaludra.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.singaludra.data.local.entity.MovieEntity
import com.singaludra.data.local.room.dao.MovieDao

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}