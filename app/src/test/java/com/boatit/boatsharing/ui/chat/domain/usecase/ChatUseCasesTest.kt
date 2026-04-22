package com.boatit.boatsharing.features.chat.domain.usecase

import com.boatit.boatsharing.features.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.features.chat.model.ComplainRequest
import com.boatit.boatsharing.features.chat.model.FollowRequest
import com.boatit.boatsharing.features.chat.model.FollowResponse
import com.boatit.boatsharing.features.chat.model.VoyagerInfo
import com.boatit.boatsharing.features.chat.model.VoyagerRelationshipObj
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChatUseCasesTest {
    @Test
    fun fetchVoyagersUseCase_returnsGatewayResult() =
        runBlocking {
            val sampleVoyager =
                VoyagerInfo(
                    UserId = "u-1",
                    FirstName = "Ali",
                    LastName = "Khan",
                    PhoneNumber = "000000000",
                    Address = "address",
                    DateOfBirth = "2000-01-01",
                )
            val expected =
                ActiveVoyagersResponse(
                    Status = 200,
                    Message = "ok",
                    obj =
                        VoyagerRelationshipObj(
                            MySelf = sampleVoyager,
                            Followed = listOf(sampleVoyager),
                            UnFollowed = emptyList(),
                        ),
                )
            val useCase = FetchVoyagersUseCase { Result.success(expected) }

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.Message)
        }

    @Test
    fun followVoyagerUseCase_returnsFailureFromGateway() =
        runBlocking {
            val useCase = FollowVoyagerUseCase { Result.failure(Exception("follow failed")) }

            val result = useCase(FollowRequest(VoyagerUserId = "u-1"))

            assertTrue(result.isFailure)
            assertEquals("follow failed", result.exceptionOrNull()?.message)
        }

    @Test
    fun complainVoyagerUseCase_returnsSuccessFromGateway() =
        runBlocking {
            val expected = FollowResponse(Status = 201, Message = "sent", obj = "ok")
            val useCase = ComplainVoyagerUseCase { Result.success(expected) }

            val result = useCase(ComplainRequest(VoyageId = "voy-1", Description = "issue"))

            assertTrue(result.isSuccess)
            assertEquals("sent", result.getOrNull()?.Message)
        }
}
