package com.surelabs.auto.uploadfile

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import org.jetbrains.anko.toast
import java.io.File


//open class itu supaya dia bisa di extends sama kelas lain
open class BaseActivity : AppCompatActivity() {

    private val FOLDERNAME = "Upload File"
    var fileUri: Uri? = null
    var realPath: String? = null
    val FILEPICKERREQ = 1025
    var flagUpload = false
    val CAMERAREQ = 1024
    val MEDIATYPEIMAGE = 1000


    fun launchFilePicker() {
        //intent untuk membuka file
        val pickerIntent = Intent(Intent(Intent.ACTION_OPEN_DOCUMENT))
        pickerIntent.apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        startActivityForResult(Intent.createChooser(pickerIntent, "Pilih file..."), FILEPICKERREQ)
    }

    fun launchCamera() {
        //buat intent untuk mencari aplikasi yang dapat mengambil gambar
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //dapatkan path dari gambar yang telah diambil dari intent sebelumnya
        fileUri = getOutputMediaFileuri(MEDIATYPEIMAGE)

        //bawa path yang ada di file uri ke intent yang akan dipanggil (camera intent)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

        // start activity camera
        startActivityForResult(cameraIntent, CAMERAREQ)
    }
    fun previewImage(imageView: ImageView){
        Glide.with(this).load(realPath).into(imageView)
    }

    private fun getOutputMediaFileuri(mediatypeimage: Int): Uri? {
        return getOutputMediaFile(mediatypeimage)?.let {
            FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", it)
        }
    }

    private fun getOutputMediaFile(mediatypeimage: Int): File? {
        //path lokasi gambar
        val folder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            FOLDERNAME
        )

        //kalo foldernya nggak ada
        if (!folder.exists()) {

            //yang ini kalo buat foldernya gagal
            if (!folder.mkdirs()) {
                return null
            }
        }

        val mediaFile: File
        if (mediatypeimage == MEDIATYPEIMAGE) {
            mediaFile = File(folder.path + File.separator + "IMG_" + System.nanoTime() + ".jpg")
            realPath = mediaFile.toString()
            return mediaFile
        } else {
            return null
        }

    }

    fun getMetaData(uri: Uri, imageView: ImageView) {
        //cursor
        val cursor = contentResolver.query(uri, null, null, null, null)

        //pastikan cursor ada di baris pertama
        if (cursor?.moveToFirst() == true) {
            imageView.setImageURI(uri)
        } else {
            toast("Uri tidak dikenal/valid")
        }
    }

}