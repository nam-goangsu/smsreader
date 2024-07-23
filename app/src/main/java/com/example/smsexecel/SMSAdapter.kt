package com.example.smsexecel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SMSAdapter (private val smsList: List<SMS>, private val selectedSMS: MutableList<SMS>) :
    RecyclerView.Adapter<SMSAdapter.SMSViewHolder>() {

    inner class SMSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val address: TextView = itemView.findViewById(R.id.smsAddress)
        val date: TextView = itemView.findViewById(R.id.smsDate)
        val body: TextView = itemView.findViewById(R.id.smsBody)
        val checkBox: CheckBox = itemView.findViewById(R.id.smsCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sms, parent, false)
        return SMSViewHolder(view)
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        val sms = smsList[position]
        holder.address.text = sms.address
        holder.date.text = sms.date
        holder.body.text = sms.body
        holder.checkBox.isChecked = selectedSMS.contains(sms)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedSMS.add(sms)
            } else {
                selectedSMS.remove(sms)
            }
        }
    }

    override fun getItemCount() = smsList.size
}