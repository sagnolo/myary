package com.nand.myary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        rv_diarys.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        rv_diarys.adapter = DiarysAdapter()

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

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return DiarysViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_diary_list, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is DiarysViewHolder){
                holder.bind(items[position])
            }
        }

        class DiarysViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind(item: Diary){
                itemView.txt_test.setText(item.content)
            }
        }
    }
}