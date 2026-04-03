package com.boatit.boatsharing.ui.signup.business.repository

import com.boatit.boatsharing.data.remote.api.BusinessProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessLogoResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class BusinessLogoRepository(private val api: BusinessProfileApi) {

    suspend fun saveBusinessLogo(userId: String, logoFile: File): Result<SaveBusinessLogoResponse> {
        return try {
            val userIdBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(
                "Logo",
                logoFile.name,
                logoFile.asRequestBody("image/*".toMediaTypeOrNull()),
            )
            RemoteMapper.toResult(api.saveBusinessLogo(userIdBody, part))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
