package com.nand.myary.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nand.myary.data.CalendarItem
import com.nand.myary.data.db.entity.Diary
import com.nand.myary.viewmodel.DiaryViewModel
import com.nand.myary.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_calendar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var diaryViewModel: DiaryViewModel
    private var calendarAdapter = CalendarAdapter()
    private var diaryMap = HashMap<String, Diary>()
    private var nowDate = ""
    private var nowCalendarItem: CalendarItem? = null
    val startActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>(){
            if(it.resultCode == Activity.RESULT_OK){
                val result = it.data
                val calendarItem = result?.getSerializableExtra("calendaritem") as CalendarItem
                nowCalendarItem = calendarItem
                txt_diary.setText(calendarItem.content)
                txt_main_date.setText("${calendarItem.year}년 ${calendarItem.month}월 ${calendarItem.day}일")
                btn_start.visibility = View.GONE
                layout_diary.visibility = View.VISIBLE
                nowDate = calendarItem.date!!
                day = calendarItem.day
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val layoutManager = object : GridLayoutManager(applicationContext, 7) {
            override fun canScrollVertically(): Boolean { return false }
            override fun canScrollHorizontally(): Boolean { return false }
        }

        rv_calendar.layoutManager = layoutManager
        rv_calendar.adapter = calendarAdapter.apply {
            setOnItemClickListener { adapter, i ->
                var items = (adapter as CalendarAdapter).items
                day = items[i].day
                if(items[i].content == null) {
                    var intent = Intent(applicationContext, WriteActivity::class.java)
                    intent.putExtra("type", 0)
                    intent.putExtra("calendaritem", items[i])
                    startActivityResult.launch(intent)
                } else {
                    txt_diary.setText(items[i].content)
                    txt_main_date.setText("${items[i].year}년 ${items[i].month}월 ${items[i].day}일")
                    nowDate = items[i].date!!
                    nowCalendarItem = items[i]
                    btn_start.visibility = View.GONE
                    layout_diary.visibility = View.VISIBLE
                }
                drawerLayout.closeDrawers()
            }
        }

        img_prev.setOnClickListener {
            val c = Calendar.getInstance()
            c.set(year, month, 1)
            c.add(Calendar.MONTH, -1)
            loadDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH))
        }

        img_next.setOnClickListener {
            val c = Calendar.getInstance()
            c.set(year, month, 1)
            c.add(Calendar.MONTH, +1)
            loadDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH))
        }

        btn_modify.setOnClickListener {
            var intent = Intent(applicationContext, WriteActivity::class.java)
            intent.putExtra("type", 1)
            intent.putExtra("calendaritem", nowCalendarItem)
            startActivityResult.launch(intent)
        }

        btn_delete.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("삭제하시겠습니까?")
                .setPositiveButton("삭제",
                    DialogInterface.OnClickListener { dialog, id ->
                        CoroutineScope(Dispatchers.IO).launch {
                            diaryViewModel.delete(nowDate)
                            CoroutineScope(Dispatchers.Main).launch {
                                btn_start.visibility = View.VISIBLE
                                layout_diary.visibility = View.GONE
                            }
                        }
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    }).create().show()
        }

        btn_start.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        this.year = year
        this.month = month
        this.day = day
        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)
        diaryViewModel.getAll().observe(this, Observer {
            diaryMap.clear()
            for (x in 0..it.size - 1) {
                diaryMap.put(it[x].date!!, it[x])
            }
            loadDate(this.year, this.month)
        })

        btn_view.setOnClickListener {
            var intent = Intent(applicationContext, DiarysActivity::class.java)
            startActivity(intent)
        }
    }

    fun loadDate(yearValue: Int, monthValue: Int){
        year = yearValue
        month = monthValue

        val c = Calendar.getInstance()
        c.set(year, month, 1)
        val firstWeek = c.get(Calendar.DAY_OF_WEEK) - 1

        val lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH)

        CoroutineScope(Dispatchers.IO).launch {
            val items = arrayListOf<CalendarItem>()
            for (i in 0 until firstWeek) {
                items.add(CalendarItem(0, 0, 0, 0, null, null))
            }
            for (day in 1..lastDay) {
                c.set(Calendar.DATE, day)
                val week = c.get(Calendar.DAY_OF_WEEK)
                var value = String.format("%04d", year) + String.format("%02d", month + 1) + String.format("%02d", day)
                var item = diaryMap.get(value)
                if (item != null)
                    items.add(CalendarItem(year, month + 1, day, week, item.content, item.date))
                else
                    items.add(CalendarItem(year, month + 1, day, week, null, null))
            }

            CoroutineScope(Dispatchers.Main).launch {
                txt_date.setText("${year}년 ${month+1}월")
                calendarAdapter.items.clear()
                calendarAdapter.items.addAll(items)
                calendarAdapter.notifyDataSetChanged()
            }
        }
    }

    class CalendarAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        val items = ArrayList<CalendarItem>()
        private var onItemClickListener: OnItemClickListener? = null

        fun setOnItemClickListener(listener: (RecyclerView.Adapter<*>, Int) -> Unit) {
            onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(adapter: RecyclerView.Adapter<*>, position: Int) {
                    listener(adapter, position)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ItemViewHolder).bind(items[position])
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemClick(this@CalendarAdapter, position)
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
            fun bind(item: CalendarItem){
                if(item.content == null)
                    itemView.day_text.background = null
                else
                    itemView.day_text.setBackgroundResource(R.drawable.date_round)
                if(item.day != 0)
                    itemView.day_text.setText(item.day.toString())
                else
                    itemView.visibility = View.GONE
            }
        }

        interface OnItemClickListener {
            fun onItemClick(adapter: RecyclerView.Adapter<*>, position: Int)
        }
    }
}