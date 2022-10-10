package com.example.masterdev_networking.viewModel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.masterdev_networking.model.model_image_storage.ImageStorage
import com.example.masterdev_networking.storage.StorageLogin
import kotlinx.coroutines.*
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class ViewModelStoreImage(val applicationContext: Context) : ViewModel(), CoroutineScope {

    //change fragment
    var changFragment = MutableLiveData<String>()
    fun resetPass() {
        StorageLogin.remove(StorageLogin.UserName)
        StorageLogin.remove(StorageLogin.PASSWORD)
        StorageLogin.remove(StorageLogin.ACCESS)
        changFragment.postValue("Login")
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var imagesLiveData: MutableLiveData<MutableList<ImageStorage>> = MutableLiveData()

    fun getImageList(): MutableLiveData<MutableList<ImageStorage>> {
        return imagesLiveData
    }

    @SuppressLint("Recycle")
    private fun loadImagesfromSDCard(): ArrayList<ImageStorage> {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val listOfAllImages = ArrayList<ImageStorage>()
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.DISPLAY_NAME
        )

        cursor = applicationContext.contentResolver.query(
            uri,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )

        val columnIndexData = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val columnTimeData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
        val columnNameData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)


        while (cursor.moveToNext()) {
            val indexData = cursor.getString(columnIndexData)
            val timeData = cursor.getLong(columnTimeData)
            val nameImage = cursor.getString(columnNameData)

            val absolutePathOfImage = ImageStorage(indexData, convertToDate(timeData,"MM/yyyy"),nameImage)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }

    fun getAllImages() {
        launch(Dispatchers.Main) {
            imagesLiveData.value = withContext(Dispatchers.IO) {
                loadImagesfromSDCard()
            }!!
        }
    }

    fun profile(){
        changFragment.postValue("profile")
    }
    fun pickPhoto(){
        changFragment.postValue("Image")
    }

    // lưu ảnh vào storage
    fun saveMediaToStorage(bitmap: Bitmap, context:Context) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis().toString())
        values.put(MediaStore.Images.Media.BUCKET_ID, "Capture_From_Camera")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Be taken at ${System.currentTimeMillis()}")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri: Uri? =
            context.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val outStream: OutputStream?
        try {
            outStream = context.contentResolver?.openOutputStream(uri!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        getAllImages()
    }

    // chuyển đổi định dạng ngày tháng năm sau khi lấy dữ liệu từ storage
    private fun convertToDate(milliSeconds: Long, dateFormat: String): String {
        val formatter = SimpleDateFormat(dateFormat)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds * 1000
        return formatter.format(calendar.time)
    }


}