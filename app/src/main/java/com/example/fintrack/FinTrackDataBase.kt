package com.example.fintrack

import androidx.room.Database

@Database([CatEntity::class], version = 1)
abstract class FinTrackDataBase {

    abstract fun getCatDao(): CatDao
}