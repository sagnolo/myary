package com.nand.myary.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.nand.myary.R
import com.nand.myary.data.CalendarItem
import com.nand.myary.data.db.entity.Diary
import com.nand.myary.viewmodel.DiaryViewModel
import kotlinx.android.synthetic.main.activity_diarys.*
import kotlinx.android.synthetic.main.item_diary_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiarysActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diarys)

        MobileAds.initialize(this, object: OnInitializationCompleteListener {
            override fun onInitializationComplete(p0: InitializationStatus) {
            }
        })
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        rv_diarys.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        rv_diarys.adapter = DiarysAdapter().apply {
            setOnItemClickListener { adapter, i ->
                val item = (adapter as DiarysAdapter).items[i]
                var calendarItem = CalendarItem(
                    item.year,
                    item.month,
                    item.day,
                    0,
                    item.content,
                    item.date
                )
                val intent = Intent()
                intent.putExtra("calendaritem", calendarItem)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }


        CoroutineScope(Dispatchers.IO).launch {
            val diaryViewModel = ViewModelProvider(this@DiarysActivity).get(DiaryViewModel::class.java)
            val list = diaryViewModel.getAlls()
            CoroutineScope(Dispatchers.Main).launch {
                (rv_diarys.adapter as DiarysAdapter).items.addAll(list)
                (rv_diarys.adapter as DiarysAdapter).notifyDataSetChanged()
            }
        }
    }

    class DiarysAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var items = ArrayList<Diary>()
        private var onItemClickListener: OnItemClickListener? = null

        fun setOnItemClickListener(listener: (RecyclerView.Adapter<*>, Int) -> Unit) {
            onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(adapter: RecyclerView.Adapter<*>, position: Int) {
                    listener(adapter, position)
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return DiarysViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_diary_list, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is DiarysViewHolder){
                holder.bind(items[position])
                holder.itemView.setOnClickListener {
                    onItemClickListener?.onItemClick(this@DiarysAdapter, position)
                }
            }
        }

        class DiarysViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind(item: Diary){
                itemView.txt_diary_content.setText(item.content)
                itemView.txt_diary_date.setText("[${item.year}년 ${item.month}월 ${item.day}일]")
            }
        }
    }
}