package com.example.fintrack

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MonyDao {
    @Query("Select * from monyentity")
    fun getAll(): List<MonyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(monyEntity: MonyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(monyEntity: List<MonyEntity>)

    @Update
    fun update(monyEntity: MonyEntity)

    @Delete
    fun delete(monyEntity: MonyEntity)

    @Query("Select * from monyentity where cat is :categoryName")
    fun getAllbyCatName(categoryName: String): List<MonyEntity>

    @Delete
    fun deleteAll(monyEntity: List<MonyEntity>)

    @Query("SELECT SUM(val) FROM MonyEntity WHERE (:category IS NULL OR cat = :category)")
    fun getTotalValue(category: String?): Double
}