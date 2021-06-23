package com.nand.myary.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nand.myary.data.db.entity.Diary

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diary WHERE date BETWEEN :from AND :to")
    fun findDiaryBetweenDates(from: String, to: String): List<Diary>

    @Query("SELECT * FROM diary WHERE date BETWEEN :from AND :to")
    fun findDiaryLivedatas(from: String, to: String): LiveData<List<Diary>>

    @Query("SELECT * FROM diary")
    fun getAll(): LiveData<List<Diary>>

    @Query("SELECT * FROM diary")
    fun getAlls(): List<Diary>

    @Query("SELECT * FROM diary WHERE date=:date")
    fun getDiary(date: String): Diary

    @Insert
    fun insertDiary(diary: Diary)

    @Delete
    fun delete(diary: Diary)

    @Query("DELETE FROM diary WHERE date=:date")
    fun deleteDiary(date: String)
}