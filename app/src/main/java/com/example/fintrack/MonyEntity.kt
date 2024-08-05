package com.example.fintrack

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["name", "cat","val"], unique = true)]
)
//nessa parte aqui de cima do código, depois eu posso remover, por que não vou precisar de "registros padrão".
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
