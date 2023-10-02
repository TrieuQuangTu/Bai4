package com.example.bai4.Screen

import android.app.DatePickerDialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bai4.ContentProvider.MyContentProvider
import com.example.bai4.SQLiteHelper.WorkSQLOpenHelper
import com.example.bai4.databinding.ActivityAddBinding
import java.util.Calendar

class AddActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddBinding
    private lateinit var db:WorkSQLOpenHelper
    private lateinit var myCalendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myCalendar=Calendar.getInstance()
        db = WorkSQLOpenHelper(this)


        val datepicker  = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            binding.tvDatepicker.setText("$dayOfMonth - $month - $year")
        }

        binding.datePickerActions.setOnClickListener {
            DatePickerDialog(this,datepicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
        }
        binding.btnSave.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val namework = binding.editWork.text.toString()
        val time=binding.tvDatepicker.text.toString()


        val values = ContentValues()
        values.put(WorkSQLOpenHelper.COLUMN_NAME,namework)
        values.put(WorkSQLOpenHelper.COLUMN_TIME,time)

        val uri = contentResolver.insert(MyContentProvider.CONTENT_URI, values)
        if (uri != null) {
            setResult(RESULT_OK)
            finish()
        } else {

        }


    }
}