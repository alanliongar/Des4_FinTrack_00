package com.example.fintrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
//comit
@Dao
interface CatDao {
    @Query("Select * From CatEntity")
    fun getAll(): List<CatEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(catEntity: CatEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(catEntity: List<CatEntity>)
}