package com.example.smsexecel

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.Telephony
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var smsAdapter: SMSAdapter
    private val smsList = mutableListOf<SMS>()
    private val selectedSMS = mutableListOf<SMS>()
    private val smsGroups = mutableListOf<Pair<String, List<SMS>>>()

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val readSmsGranted = permissions[android.Manifest.permission.READ_SMS] ?: false
        val writeStorageGranted = permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false

        if (readSmsGranted && writeStorageGranted) {
            loadSms()
        } else {
            Toast.makeText(this, "Permissions are required to proceed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSMS)
        recyclerView.layoutManager = LinearLayoutManager(this)
        smsAdapter = SMSAdapter(smsList, selectedSMS)
        recyclerView.adapter = smsAdapter

        val buttonSaveSMS = findViewById<Button>(R.id.buttonSaveSMS)


        buttonSaveSMS.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED /*||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED*/) {
                requestPermissionsLauncher.launch(arrayOf(android.Manifest.permission.READ_SMS/*, android.Manifest.permission.WRITE_EXTERNAL_STORAGE*/))
            } else {
                saveSelectedSmsToExcel()
            }
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsLauncher.launch(arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        } else {
            loadSms()
        }
    }

    private fun loadSms() {
        val cursor: Cursor? = contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
        /// gruop 부분 st
        val smsMap = mutableMapOf<String, MutableList<SMS>>()
        /// gruop 부분 ed
        cursor?.use {

            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(Telephony.Sms._ID))
                val address = it.getString(it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val date = it.getString(it.getColumnIndexOrThrow(Telephony.Sms.DATE))
                val currentDateTime =
                    Instant.ofEpochMilli(date.toLong()).atZone(ZoneId.systemDefault()).toLocalDateTime()
                var a =DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초").format(currentDateTime)
                val body = it.getString(it.getColumnIndexOrThrow(Telephony.Sms.BODY))
                smsList.add(SMS(id, address, a.toString(), body))

                /// gruop 부분 st
                val sms = SMS(id, address, a.toString(), body)
                if (smsMap.containsKey(address)) {
                    smsMap[address]?.add(sms)
                } else {
                    smsMap[address] = mutableListOf(sms)
                }
/// gruop 부분 ed


            }
        }
/// gruop 부분 st
        smsGroups.clear()
        smsGroups.addAll(smsMap.map { it.key to it.value })

        /// gruop 부분 ed
        smsAdapter.notifyDataSetChanged()
    }

    private fun saveSelectedSmsToExcel() {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Selected SMS")

        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("ID")
        header.createCell(1).setCellValue("Address")
        header.createCell(2).setCellValue("Date")
        header.createCell(3).setCellValue("Body")

        selectedSMS.forEachIndexed { index, sms ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(sms.id)
            row.createCell(1).setCellValue(sms.address)
            row.createCell(2).setCellValue(sms.date)
            row.createCell(3).setCellValue(sms.body)
        }


        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "sms_$timestamp.xlsx"

        val smsDir = File(getExternalFilesDir(null), "sms")
        if (!smsDir.exists()) {
            smsDir.mkdirs()
        }

        val filePath = File(smsDir, fileName)
        FileOutputStream(filePath).use { fileOut ->
            workbook.write(fileOut)
        }

/*
        val filePath = File(getExternalFilesDir(null), "selected_sms.xlsx")
        FileOutputStream(filePath).use { fileOut ->
            workbook.write(fileOut)
        }*/

        workbook.close()
        Toast.makeText(this, "SMS saved to Excel file", Toast.LENGTH_SHORT).show()
    }
}
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}