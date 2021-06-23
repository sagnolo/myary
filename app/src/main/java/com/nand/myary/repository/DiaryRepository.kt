package com.nand.myary.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.nand.myary.data.db.entity.Diary
import com.nand.myary.data.db.dao.DiaryDao
import com.nand.myary.data.db.DiaryDatabase

class DiaryRepository(application: Application) {

    private val diaryDao: DiaryDao by lazy {
        val db = DiaryDatabase.getDatabase(application)
        db.diaryDao()
    }

    private val diarys: LiveData<List<Diary>> by lazy {
        diaryDao.getAll()
    }

    fun getAlls(): List<Diary>{
        return diaryDao.getAlls()
    }

    fun findDiaryLivedatas(from: String, to: String): LiveData<List<Diary>>{
        return diaryDao.findDiaryLivedatas(from, to)
    }

    fun findDiaryBetweenDates(from: String, to: String): List<Diary>{
        return diaryDao.findDiaryBetweenDates(from, to)
    }

    fun getAll(): LiveData<List<Diary>> {
        return diarys
    }

    fun getDiaryByDate(date: String): Diary {
        return diaryDao.getDiary(date)
    }

    fun insertDiary(diary: Diary){
        diaryDao.insertDiary(diary)
    }

    fun deleteDiary(date: String){
        diaryDao.deleteDiary(date)
    }

}