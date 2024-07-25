package com.example.smsexecel

data class SMS(
    val tyepo: RowType,
    val id: String,
    val address: String,
    val date: String,
    val body: String,
    val check: Boolean = false
)

enum class RowType(val id: Int) {
    All_Select(0),
    Head_text(1),
    Body_text(2),
    ProductRow(3);

}


data class selectSMS(val address: String, val date: String, val body: String, val check: Boolean)