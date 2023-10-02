package com.example.bai4.SQLiteHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class WorkSQLOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
         const val DATABASE_NAME = "work.db"

        //tên bảng công việc
         const val TABLE_NAME = "works"

        //Table column
         const val COLUMN_ID = "id"
         const val COLUMN_NAME = "NAME"
         const val COLUMN_TIME = "TIME"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        //tạo bảng công việc
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_NAME TEXT, $COLUMN_TIME TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        //xóa bảng cũ nếu tồn tại và tạo bảng mới
        val dropTableQuer = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuer)
        onCreate(db)
    }
}