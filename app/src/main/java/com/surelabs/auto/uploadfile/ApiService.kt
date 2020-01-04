package com.surelabs.auto.uploadfile

import com.surelabs.auto.uploadfile.datamodel.ResponseUpload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("welcome/file_upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("judul") judul: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody
    ) : Call<ResponseUpload>
}
