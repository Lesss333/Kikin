package com.kikin.gastos.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "expenses")
@Parcelize
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val amount: Double
) : Parcelable
