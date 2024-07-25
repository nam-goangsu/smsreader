package com.example.smsexecel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SMSMotherAdapter (private val smsList: List<SMS>, private val  selectedSMS: MutableList<selectSMS>) :
    RecyclerView.Adapter<SMSMotherAdapter.SMSViewHolder>() {

    inner class SMSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val timetext :TextView =itemView.findViewById(R.id.time_text)
        val bodytext :TextView =itemView.findViewById(R.id.smsbody_text)
        val checkBox: CheckBox = itemView.findViewById(R.id.child_checkbox)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_sms, parent, false)
        return SMSViewHolder(view)
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        val smstext = smsList[position]

        holder.timetext.text = smstext.date
        holder.bodytext.text = smstext.body


        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                    selectedSMS.add(selectSMS(smstext.address.toString(),smstext.date.toString(),smstext.body.toString(),true))

            } else {

                    selectedSMS.remove(selectSMS(smstext.address.toString(),smstext.date.toString(),smstext.body.toString(),true))

            }
        }

    }

    override fun getItemCount() = smsList.size


    /*inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderTextView: TextView = itemView.findViewById(R.id.senderTextView)
        private val smsRecyclerView: RecyclerView = itemView.findViewById(R.id.smsRecyclerView)

        fun bind(group: SmsGroup) {
            senderTextView.text = group.sender
            smsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            smsRecyclerView.adapter = SmsAdapter(group.smsList)
        }
    }*/
}