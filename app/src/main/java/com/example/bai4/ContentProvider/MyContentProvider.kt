package com.example.bai4.ContentProvider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.bai4.SQLiteHelper.WorkSQLOpenHelper

class MyContentProvider : ContentProvider() {


    private lateinit var dbHelper: WorkSQLOpenHelper

    //khai báo URI cho Content Provider
    companion object {
        const val AUTHORITY = "com.example.bai4.ContentProvider"
        const val PATH_WORKS = "works"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$PATH_WORKS")

        private const val WORK = 1
        private const val WORK_ID = 2

        //Lớp UriMatcher được sử dụng để khơp(match) URI với các hành động cụ thể
        //UriMatcher.No_MATCH : nghĩa là ko khớp với bất kỳ URI nào theo mặc định
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, PATH_WORKS, WORK)
            uriMatcher.addURI(AUTHORITY, "$PATH_WORKS/#", WORK_ID)
        }
    }

    override fun onCreate(): Boolean {
        dbHelper = WorkSQLOpenHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase //cho phép đọc dữ liệu từ đối tượng dbhelper
        val cursor: Cursor //khai báo đôi tượng cursor: dùng để lưu trữ kết quả của truy vấn csdl
        when (uriMatcher.match(uri)) {

            //nếu mẫu URI trùng khớp với URI WORK thì thực hiện truy vấn
            WORK -> cursor = db.query(
                WorkSQLOpenHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            //nếu mẫu URI trùng khớp với URI WORK_ID
            WORK_ID -> {

                val id = uri.lastPathSegment //trich xuất phân đoạn cuối cùng trong URI.
                // Ví dụ:content://your_app/work/123 thì id sẽ được gán giá trị "123"

                //querySelection là một chuỗi lựa chọn SQL xác định điều kiện cho truy vấn.
                // Trong trường hợp này, nó sử dụng phép nội suy chuỗi để tạo chuỗi lựa chọn nhằm kiểm tra xem giá trị trong cột COLUMN_ID có khớp với một ID cụ thể hay không.
                // Ví dụ: nếu COLUMN_ID là "id" và id là "123",
                // thì querySelection sẽ là "id = ?".
                val querySelection = "${WorkSQLOpenHelper.COLUMN_ID} = ?"

                //queryArgs là một mảng các đối số tương ứng với phần giữ chỗ trong chuỗi lựa chọn.
                // Trong trường hợp này, nó chứa một phần tử duy nhất là ID được trích xuất từ URI.
                // Điều này được sử dụng để thay thế ? giữ chỗ trong chuỗi lựa chọn.
                val queryArgs = arrayOf(id)


                cursor =db.query(
                    WorkSQLOpenHelper.TABLE_NAME,
                    projection,
                    querySelection,
                    queryArgs,
                    null,
                    null,
                    sortOrder
                )

            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        //đồng bộ hóa với những thay đổi trong nguồn dữ liệu
        cursor.setNotificationUri(context!!.contentResolver,uri)
        return cursor
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val db =dbHelper.writableDatabase
        val rowID = db.insert(WorkSQLOpenHelper.TABLE_NAME,null,values)

        //kiểm tra có insert thành công ko?
        // thành công thì rowID>0
        if (rowID>0){
            val insertedUri = ContentUris.withAppendedId(CONTENT_URI,rowID) //tạo 1 content URI
            context?.contentResolver?.notifyChange(insertedUri,null) //thông báo cho cho thành phàn quan sát là dữ liệu chỉ định đã thay đổi
            return insertedUri
        }
        throw android.database.SQLException("Fail to insert row into $uri")
    }


    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
}