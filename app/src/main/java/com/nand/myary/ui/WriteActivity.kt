package com.nand.myary.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.nand.myary.data.CalendarItem
import com.nand.myary.data.db.entity.Diary
import com.nand.myary.viewmodel.DiaryViewModel
import com.nand.myary.R
import kotlinx.android.synthetic.main.activity_diarys.*
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.activity_write.adView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        MobileAds.initialize(this, object: OnInitializationCompleteListener {
            override fun onInitializationComplete(p0: InitializationStatus) {
            }
        })
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        var intent = getIntent()
        var item = intent.getSerializableExtra("calendaritem") as CalendarItem
        var type = intent.getIntExtra("type", 0)
        if(item.year == null) finish()
        if(type == 1) edit_diary.setText(item.content)
        txt_write_date.setText("${item.year}년 ${item.month}월 ${item.day}일")

        edit_diary.setMovementMethod(ScrollingMovementMethod())
        edit_diary.requestFocus()

        var diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        btn_write.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if(type == 0)
                    diaryViewModel.insert(
                        Diary(
                            edit_diary.text.toString(),
                            String.format("%04d", item.year) + String.format("%02d", item.month) + String.format("%02d", item.day),
                            item.year,
                            item.month,
                            item.day
                        )
                    )
                else
                    diaryViewModel.update(edit_diary.text.toString(), item.date!!)

                item.content = edit_diary.text.toString()
                item.date = "" + item.year + item.month + item.day
                val intent = Intent()
                intent.putExtra("calendaritem", item)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}