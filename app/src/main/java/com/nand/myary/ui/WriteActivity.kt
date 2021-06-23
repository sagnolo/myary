package com.nand.myary.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nand.myary.data.CalendarItem
import com.nand.myary.data.db.entity.Diary
import com.nand.myary.viewmodel.DiaryViewModel
import com.nand.myary.R
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        var intent = getIntent()
        var item = intent.getSerializableExtra("calendaritem") as CalendarItem
        var type = intent.getIntExtra("type", 0)
        if(item.year == null)
            finish()
        txt_write_date.setText("${item.year}년 ${item.month}월 ${item.day}일")


        var diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        btn_write.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                diaryViewModel.insert(Diary(edit_diary.text.toString(), String.format("%04d", item.year) + String.format("%02d", item.month) + String.format("%02d", item.day)))
                finish()
            }
        }
    }
}