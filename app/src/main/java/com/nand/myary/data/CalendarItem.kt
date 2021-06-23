package com.nand.myary.data

import java.io.Serializable

data class CalendarItem(
    val year: Int,
    val month: Int,
    val day: Int = 0,
    val week: Int = 0,
    val content: String?,
    val date: String?
    ): Serializable