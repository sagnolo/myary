package com.nand.myary.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("UPDATE diary SET content=:content WHERE date=:date")
    fun updateDiary(content: String, date: String)

    @Delete
    fun delete(diary: Diary)

    @Query("DELETE FROM diary WHERE date=:date")
    fun deleteDiary(date: String)
}