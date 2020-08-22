package com.example.assignment.adapters

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.datamodels.Item
import com.example.assignment.R

class ListAdapter(val context: Context, val arrayList: ArrayList<Item>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.check.text = arrayList[position].name
        val subListAdapter =
            arrayList[position].subCategory?.let { SubListAdapter(context, it, position) }
        holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(context)
        holder.recyclerView.adapter = subListAdapter
        val messageReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val check = p1?.getBooleanExtra("check", false)
                val pos = p1?.getIntExtra("position", 0)
                if (check!! && pos == position) {
                    holder.check.isChecked = true
                }
            }
        }
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(messageReceiver, IntentFilter("custom-intent"))
        holder.check.setOnCheckedChangeListener { _, b ->
            if (!b) {
                val intent = Intent("check-intent").apply {
                    putExtra("checksub", b)
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }
    }

    override fun getItemCount(): Int = arrayList.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val check = itemView.findViewById<CheckBox>(R.id.materialCheckBox)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.subRecyclerView)
    }


}