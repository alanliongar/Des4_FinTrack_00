package com.example.fintrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MonyDao {
    @Query("Select * from monyentity")
    fun getAll(): List<MonyEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(monyEntity: MonyEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(monyEntity: List<MonyEntity>)
}