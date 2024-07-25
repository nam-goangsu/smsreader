package com.example.smsexecel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SMSAdapter (private val smsList: MutableMap<String,MutableList<SMS>>, private val selectedSMS: MutableList<selectSMS>) :
    RecyclerView.Adapter<SMSAdapter.SMSViewHolder>() {


    inner class SMSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headline = itemView.findViewById<LinearLayout>(R.id.HEADLINE) //main
        val headtext : TextView = itemView.findViewById(R.id.phonenumber)
        val expendbutton : ImageButton = itemView.findViewById(R.id.expand_button)
        val checkBox: CheckBox = itemView.findViewById(R.id.head_check)

        val subrecycler : RecyclerView = itemView.findViewById(R.id.subrecyclverview)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sms, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.phone_number, parent, false)
        //expendList.addAll(listOf(false))

        return SMSViewHolder(view)
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int ) {

        //전체 리스트가 옴 full
        val test = smsList.keys.toList()

        /// key string 값이 로드  폰번호 가져옴
        val testkey = test[position]



        // valuew값들이 가져옴 하지만 안쪽에 넣어야함 안쪽list 정보를 적용 size 적용
        val testvalues = smsList[testkey]?:  mutableListOf()

        val testvaluedate = testvalues[0].tyepo

        holder.headtext.text = testkey


        //todo 클릭하면 해당 내용 전부 저장
        /*        val sms = smsList[position]
                holder.checkBox.isChecked = selectedSMS.contains(sms)*/
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                testvalues.forEachIndexed { index, sms ->
                    selectedSMS.add(selectSMS(testvalues[index].address.toString(),testvalues[index].date.toString(),testvalues[index].body.toString()))
                }
            } else {
                testvalues.forEachIndexed { index, sms ->
                    selectedSMS.remove(selectSMS(testvalues[index].address.toString(),testvalues[index].date.toString(),testvalues[index].body.toString()))
                }
            }
        }

        holder.subrecycler.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.subrecycler.adapter = SMSMotherAdapter(testvalues,selectedSMS)

        when(testvaluedate){
            RowType.All_Select->{
                holder.subrecycler.visibility = View.GONE
                holder.headline.visibility = View.VISIBLE
                holder.headtext.text = testvalues[testvalues.size-1].body
                holder.expendbutton.visibility = View.GONE
            }
            RowType.Head_text->{
                holder.subrecycler.visibility = View.VISIBLE
                holder.headline.visibility = View.VISIBLE
                holder.expendbutton.visibility = View.VISIBLE
            }
            RowType.Body_text->{
                holder.subrecycler.visibility = View.VISIBLE
                holder.headline.visibility = View.VISIBLE
                holder.expendbutton.visibility = View.VISIBLE
            }
            RowType.ProductRow->{}

        }


        // todo 보여주는 폰번호 만큼 list 크기를 생성 초기값 false로 주고 이후 참값이면 변경되게 변경
/*
        val isExpanded = expendList.contains(expendList[position])
        holder.expendbutton.setImageResource(
            if (isExpanded) R.drawable.ic_arrow_drop_up
            else R.drawable.ic_arrow_drop_down
        )

*/



    //    holder.subrecycler.isVisible = isExpanded
        //val test1 = smsList.values


        /*val sms = smsList[position]
        when(sms.tyepo){
            RowType.All_Select->{
                holder.bodyline.visibility = View.GONE
                holder.headline.visibility = View.VISIBLE
                holder.headtext.text = sms.body
                holder.expendbutton.visibility = View.INVISIBLE
            }
            RowType.Head_text->{
               holder.bodyline.visibility = View.GONE
                holder.headline.visibility = View.VISIBLE
                holder.headtext.text = sms.address
                holder.expendbutton.visibility = View.VISIBLE
            }
            RowType.Body_text->{
                holder.bodyline.visibility = View.VISIBLE
                holder.headline.visibility = View.GONE
                holder.subitmetext.text = sms.date
                holder.subbodytext.text = sms.body
            }
            RowType.ProductRow->{}

        }*/

        /*holder.type.text = sms.tyepo.toString()

        holder.date.text = sms.date
        holder.body.text = sms.body
        holder.address.text = sms.address*/

 /*       holder.checkBox.isChecked = selectedSMS.contains(sms)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedSMS.add(sms)
            } else {
                selectedSMS.remove(sms)
            }
        }*/
    }

    override fun getItemCount() = smsList.size
}