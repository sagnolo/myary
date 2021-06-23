package com.nand.myary.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Diary(
    @ColumnInfo(name = "content") var content: String? = null,
    @ColumnInfo(name = "date") var date: String? = null
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}