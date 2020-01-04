package com.surelabs.auto.uploadfile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import com.surelabs.auto.uploadfile.datamodel.ResponseUpload
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filePicker.onClick {
            launchFilePicker()
        }

        cameraPicker.onClick {
            launchCamera()
        }

        uploadFile.onClick {
            if (flagUpload)
                doUpload()
            else
                toast("Anda Belum Memilih File")
        }

    }

    private fun doUpload() {
        //buat inputan untuk file dengan mime type yang diterima adalah file image
        val files = RequestBody.create(MediaType.parse("image/*"), imageViewToByte())

        //membuat body untuk input type file
        val imageInput = MultipartBody.Part.createFormData("files", "${System.nanoTime()}.jpg", files)

        //membuat input type text dengan value dari judul.text.toString()
        val judul = RequestBody.create(MediaType.parse("text/plain"), judul.text.toString())

        //membuat input type text dengan value dari deskripsi.text.toString()
        val deskrip = RequestBody.create(MediaType.parse("text/plain"), deskiripsi.text.toString())

        NetworkModules.getService().uploadFile(
            imageInput, judul, deskrip
        ).enqueue(object : Callback<ResponseUpload>{
            override fun onFailure(call: Call<ResponseUpload>, t: Throwable) {
                toast(t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseUpload>,
                response: Response<ResponseUpload>
            ) {
                if(response.isSuccessful){
                    val code = response.body()?.code
                    if(code == 200){
                        toast(response.body()?.message.toString())
                    }else{
                        toast(response.body()?.message.toString())
                    }
                }else{
                    toast(response.errorBody().toString())
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILEPICKERREQ && resultCode == RESULT_OK) {
            data?.data?.let { getMetaData(it, imagePreview) }
            flagUpload = true
        } else if (requestCode == CAMERAREQ && resultCode == Activity.RESULT_OK) {
            previewImage(imagePreview)
        }
    }

    private fun imageViewToByte(): ByteArray {
        //ambil gambar dari imageview as bitmap
        val bmp = (imagePreview.drawable as BitmapDrawable).bitmap

        //buat object dari bytearratoutstream
        //variable ini yang dipakai untuk ubah bitmap ke bytearray
        val bs = ByteArrayOutputStream()

        //ini proses compress. Dalam hal ini kompres ke JPG dengan quality 100
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bs)

        //variable untuk menampung bytearray dari bitmap
        val imageInByte = bs.toByteArray()

        return imageInByte
    }


}
