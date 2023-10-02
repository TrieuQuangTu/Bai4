package com.example.bai4.Screen

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bai4.Adapter.WorkAdapter
import com.example.bai4.ContentProvider.MyContentProvider
import com.example.bai4.SQLiteHelper.WorkSQLOpenHelper
import com.example.bai4.Model.Work
import com.example.bai4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mList:MutableList<Work> = mutableListOf()
    private lateinit var mAdapter: WorkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //cấu hình Recyclerview
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = WorkAdapter(mList)
        binding.recyclerView.adapter = mAdapter


        //click floating_add
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivityForResult(intent, ADD_WORK_REQUEST)
        }
        //loadwork
        loadWork()
    }
    private fun loadWork(){
        //xác định nơi bạn muốn truy vấn
        val uri = MyContentProvider.CONTENT_URI

        //định nghĩa các cột muốn truy vấn: 3 cột ID, NAME, TIME
        val projection = arrayOf(
            WorkSQLOpenHelper.COLUMN_ID,
            WorkSQLOpenHelper.COLUMN_NAME,
            WorkSQLOpenHelper.COLUMN_TIME,
        )

        //thực hiện truy vấn vào ContentProvider để lấy dữ liệu
        val cursor:Cursor? = contentResolver.query(uri,projection,null,null,null)

        //xóa du liệu hiện tại để nạp dữ liệu cho danh sách mới
        mList.clear()

        //lặp qua tất cả dòng dữ liệu trong cursor
        cursor?.use {
            while (it.moveToNext()){
                //lấy giá trị của cột COLUMN_ID gàn vào biến "id"
                val id = it.getInt(it.getColumnIndexOrThrow(WorkSQLOpenHelper.COLUMN_ID))

                //lấy giá trị của cột COLUMN_Name gàn vào biến "workname"
                val workname = it.getString(it.getColumnIndexOrThrow(WorkSQLOpenHelper.COLUMN_NAME))

                //lấy giá trị của cột COLUMN_TIME gàn vào biến "worktime"
                val worktime = it.getString(it.getColumnIndexOrThrow(WorkSQLOpenHelper.COLUMN_TIME))


                mList.add(Work(id, workname, worktime))
                Toast.makeText(this,"Add Successfully",Toast.LENGTH_SHORT).show()
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==ADD_WORK_REQUEST && resultCode == RESULT_OK){
            loadWork()
        }
    }
    companion object {
        private const val ADD_WORK_REQUEST = 1
    }

}