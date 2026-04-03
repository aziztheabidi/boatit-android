package com.boatit.boatsharing.ui.signup.captain.repository

import com.boatit.boatsharing.data.remote.api.UserProfileApi
import com.boatit.boatsharing.data.remote.RemoteMapper
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentResponse

class CaptainDocsRepository(private val api: UserProfileApi) {
    suspend fun CaptainDocs(profile: SaveCaptainDocumentRequest): Result<SaveCaptainDocumentResponse> {
        return try {
            RemoteMapper.toResult(api.saveCaptainDocument(profile))
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}
