package com.example.fintrack

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([CatEntity::class, MonyEntity::class], version = 1)
abstract class FinTrackDataBase : RoomDatabase() {
    abstract fun getCatDao(): CatDao
    abstract fun getMonyDao(): MonyDao
}