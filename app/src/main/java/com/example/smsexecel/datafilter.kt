package com.example.smsexecel

class datafilter {

    fun groupSmsByAddress(smsList: List<SMS>): Map<String, List<SMS>> {
        return smsList
            .sortedByDescending { it.date } // 날짜를 기준으로 최신순으로 정렬
            .groupBy { it.address } // address로 그룹화
    }

}