package com.nand.myary.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
data class Diary(
    @ColumnInfo(name = "content") var content: String? = null,
    @ColumnInfo(name = "date") var date: String? = null,
    @ColumnInfo(name = "year") var year: Int,
    @ColumnInfo(name = "month") var month: Int,
    @ColumnInfo(name = "day") var day: Int
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}