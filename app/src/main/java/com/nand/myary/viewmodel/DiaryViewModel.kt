package com.nand.myary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.nand.myary.data.db.entity.Diary
import com.nand.myary.repository.DiaryRepository

class DiaryViewModel(application: Application): AndroidViewModel(application) {

    private val repository: DiaryRepository by lazy {
        DiaryRepository(application)
    }

    private val diarys: LiveData<List<Diary>> by lazy {
        repository.getAll()
    }

    fun getAll() = diarys

    fun getAlls(): List<Diary>{
        return repository.getAlls()
    }

    fun getDiary(date: String): Diary {
        return repository.getDiaryByDate(date)
    }

    fun findDiaryBetweenDates(from: String, to: String): List<Diary>{
        return repository.findDiaryBetweenDates(from, to)
    }

    fun findDiaryLiveDatas(from: String, to: String): LiveData<List<Diary>>{
        return repository.findDiaryLivedatas(from, to)
    }

    fun insert(diary: Diary){
        repository.insertDiary(diary)
    }

    fun delete(date: String){
        repository.deleteDiary(date)
    }

}