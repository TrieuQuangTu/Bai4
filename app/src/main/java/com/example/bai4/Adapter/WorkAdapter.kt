package com.example.bai4.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bai4.Model.Work
import com.example.bai4.R

class WorkAdapter(private var mList: List<Work>):RecyclerView.Adapter<WorkAdapter.WorkViewHolder>() {

    inner class WorkViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tv_item_work:TextView=itemView.findViewById(R.id.tv_item_work)
        val tv_item_time:TextView=itemView.findViewById(R.id.tv_item_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item,parent,false)
        return WorkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val currentItem = mList[position]
        holder.tv_item_work.text = currentItem.namework
        holder.tv_item_time.text =currentItem.time
    }

}