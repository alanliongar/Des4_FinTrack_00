package com.example.fintrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MonyDao {
    @Query("Select * from monyentity")
    fun getAll(): List<MonyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(monyEntity: MonyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(monyEntity: List<MonyEntity>)
}