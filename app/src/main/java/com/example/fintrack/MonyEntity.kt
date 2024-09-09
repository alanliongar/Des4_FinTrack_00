package com.example.fintrack

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["name", "cat", "val"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = CatEntity::class,
        parentColumns = ["key"],
        childColumns = ["cat"]
    )]
)

data class MonyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("cat")
    val category: String,
    @ColumnInfo("val")
    val value: Double
)