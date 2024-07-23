package com.example.smsexecel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SMSMotherAdapter (private val smsList: List<SMS>, private val selectedSMS: MutableList<SMS>) :
    RecyclerView.Adapter<SMSMotherAdapter.SMSViewHolder>() {

    inner class SMSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val phonenumber :TextView =itemView.findViewById(R.id.phonenumber)
        val checkBox: CheckBox = itemView.findViewById(R.id.head_check)
        val expendbutton : ImageButton = itemView.findViewById(R.id.expand_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.phone_number, parent, false)
        return SMSViewHolder(view)
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        val sms = smsList[position]

        holder.phonenumber.text = sms.address
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