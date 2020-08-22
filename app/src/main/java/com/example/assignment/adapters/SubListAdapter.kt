package com.example.assignment.adapters

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.datamodels.SubCategory

class SubListAdapter(val context: Context, val arrayList: ArrayList<SubCategory>, val pos: Int) :
    RecyclerView.Adapter<SubListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_sub, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.check.text = arrayList[position].name
        holder.check.setOnCheckedChangeListener { _, b ->
            if (b) {
                val intent = Intent("custom-intent").apply {
                    putExtra("check", b)
                    putExtra("position", pos)
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val c = p1?.getBooleanExtra("checksub", true)
                if (!c!!) {
                    holder.check.isChecked = false
                }
            }
        }
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(receiver, IntentFilter("check-intent"))

    }

    override fun getItemCount(): Int = arrayList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val check = itemView.findViewById<CheckBox>(R.id.submaterialCheckBox)
    }

}