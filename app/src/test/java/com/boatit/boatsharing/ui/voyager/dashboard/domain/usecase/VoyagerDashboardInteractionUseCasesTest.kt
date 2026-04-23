package com.boatit.boatsharing.features.voyager.dashboard.domain.usecase

import com.boatit.boatsharing.features.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.features.chat.model.VoyagerInfo
import com.boatit.boatsharing.features.chat.model.VoyagerRelationshipObj
import com.boatit.boatsharing.features.voyager.dashboard.model.BusinessData
import com.boatit.boatsharing.features.voyager.dashboard.model.BusinessHour
import com.boatit.boatsharing.features.voyager.dashboard.model.BusinessRelationshipObj
import com.boatit.boatsharing.features.voyager.dashboard.model.BusinessRelationshipResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.DockListObj
import com.boatit.boatsharing.features.voyager.dashboard.model.FollowedVoyagerData
import com.boatit.boatsharing.features.voyager.dashboard.model.FollowedVoyagersResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.NearbyPlacesResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.PastVoyages
import com.boatit.boatsharing.features.voyager.dashboard.model.Place
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorPayments
import com.boatit.boatsharing.features.voyager.dashboard.model.SponsorVoyagerPayment
import com.boatit.boatsharing.features.voyager.dashboard.model.TravelNowObj
import com.boatit.boatsharing.features.voyager.dashboard.model.TravelNowResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageCategory
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyageCategoryDropdownResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFeedbackRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessRequest
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerFollowBusinessResponse
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerProfile
import com.boatit.boatsharing.features.voyager.dashboard.model.VoyagerVoyagesResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VoyagerDashboardInteractionUseCasesTest {
    @Test
    fun fetchVoyagerPastVoyagesUseCase_returnsGatewayData() =
        runBlocking {
            val expected =
                VoyagerVoyagesResponse(
                    Status = 200,
                    Message = "ok",
                    obj = listOf(samplePastVoyage()),
                )
            val useCase = FetchVoyagerPastVoyagesUseCase { Result.success(expected) }

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.Message)
        }

    @Test
    fun submitVoyagerFeedbackUseCase_returnsFailureFromGateway() =
        runBlocking {
            val useCase = SubmitVoyagerFeedbackUseCase { Result.failure(Exception("feedback failed")) }

            val result = useCase(VoyagerFeedbackRequest(Id = "voy-1", Rating = 5, Review = "good"))

            assertTrue(result.isFailure)
            assertEquals("feedback failed", result.exceptionOrNull()?.message)
        }

    @Test
    fun followAndUnfollowBusinessUseCases_returnGatewayResults() =
        runBlocking {
            val request = VoyagerFollowBusinessRequest(BusinessDockId = 5)
            val followUseCase =
                FollowBusinessUseCase {
                    Result.success(VoyagerFollowBusinessResponse(Status = 201, Message = "followed", obj = "ok"))
                }
            val unFollowUseCase =
                UnFollowBusinessUseCase {
                    Result.success(VoyagerFollowBusinessResponse(Status = 200, Message = "unfollowed", obj = "ok"))
                }

            val followResult = followUseCase(request)
            val unFollowResult = unFollowUseCase(request)

            assertTrue(followResult.isSuccess)
            assertEquals("followed", followResult.getOrNull()?.Message)
            assertTrue(unFollowResult.isSuccess)
            assertEquals("unfollowed", unFollowResult.getOrNull()?.Message)
        }

    @Test
    fun fetchTravelNowVoyagesUseCase_returnsGatewayData() =
        runBlocking {
            val expected = TravelNowResponse(Status = 200, Message = "ok", obj = TravelNowObj(Id = "voy-1"))
            val useCase = FetchTravelNowVoyagesUseCase { Result.success(expected) }

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals("voy-1", result.getOrNull()?.obj?.Id)
        }

    @Test
    fun fetchBusinessRelationshipsUseCase_returnsGatewayData() =
        runBlocking {
            val sampleBusiness =
                BusinessData(
                    Id = 1,
                    Name = "Biz",
                    LogoPath = "logo",
                    BusinessType = "dock",
                    YearOfEstablishment = 2020,
                    Description = "desc",
                    ImagesPath = emptyList(),
                    Location = "loc",
                    BusinessHours = listOf(BusinessHour(Day = "Mon", StartTime = "09:00", EndTimeTime = "18:00")),
                )
            val expected =
                BusinessRelationshipResponse(
                    Status = 200,
                    Message = "ok",
                    obj = BusinessRelationshipObj(Followed = listOf(sampleBusiness), UnFollowed = emptyList()),
                )
            val useCase = FetchBusinessRelationshipsUseCase { Result.success(expected) }

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.Message)
        }

    @Test
    fun fetchFollowedVoyagersUseCase_returnsGatewayData() =
        runBlocking {
            val voyager =
                VoyagerProfile(
                    PhoneNumber = "000",
                    FirstName = "Ali",
                    LastName = "Khan",
                    Address = "A",
                    DateOfBirth = "2000-01-01",
                    StripeEmail = "a@a.com",
                    UserId = "u-1",
                    ChangedOn = "2026-04-07",
                    ChangedBy = "u-1",
                )
            val expected =
                FollowedVoyagersResponse(
                    Status = 200,
                    Message = "ok",
                    obj = FollowedVoyagerData(MySelf = voyager, Followed = listOf(voyager)),
                )
            val useCase = FetchFollowedVoyagersUseCase { Result.success(expected) }

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.Message)
        }

    @Test
    fun fetchSponsorPaymentsUseCase_returnsGatewayData() =
        runBlocking {
            val expected =
                SponsorPayments(
                    Status = 200,
                    Message = "ok",
                    obj =
                        listOf(
                            SponsorVoyagerPayment(
                                Id = "voy-1",
                                Name = "Trip",
                                VoyagerName = "Voyager",
                                VoyagerPhoneNumber = "000",
                                PickupDock = "A",
                                PickupDockLatitude = 0.0,
                                PickupDockLongitude = 0.0,
                                DropOffDock = "B",
                                DropOffDockLatitude = 0.0,
                                DropOffDockLongitude = 0.0,
                                AmountToPay = 10.0,
                                NoOfVoyagers = 2,
                                WaterStay = "No",
                                Duration = "1h",
                                BookingDateTime = "2026-04-07",
                                VoyageStatus = "Pending",
                            ),
                        ),
                )
            val useCase = FetchSponsorPaymentsUseCase { Result.success(expected) }

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.message)
        }

    @Test
    fun fetchActiveVoyagersUseCase_returnsGatewayData() =
        runBlocking {
            val voyager =
                VoyagerInfo(
                    UserId = "u-1",
                    FirstName = "Ali",
                    LastName = "Khan",
                    PhoneNumber = "000",
                    Address = "A",
                    DateOfBirth = "2000-01-01",
                )
            val expected =
                ActiveVoyagersResponse(
                    Status = 200,
                    Message = "ok",
                    obj =
                        VoyagerRelationshipObj(
                            MySelf = voyager,
                            Followed = listOf(voyager),
                            UnFollowed = emptyList(),
                        ),
                )
            val useCase = FetchActiveVoyagersUseCase { Result.success(expected) }

            val result = useCase()

            assertTrue(result.isSuccess)
            assertEquals("ok", result.getOrNull()?.Message)
        }

    @Test
    fun fetchNearbyPlacesAndCategoriesUseCases_returnGatewayData() =
        runBlocking {
            val placesUseCase =
                FetchNearbyPlacesUseCase {
                    Result.success(
                        NearbyPlacesResponse(
                            Status = 200,
                            Message = "ok",
                            obj =
                                DockListObj(
                                    All =
                                        listOf(
                                            Place(
                                                Name = "Dock A",
                                                Zone = "Z",
                                                State = "S",
                                                City = "C",
                                                ZipCode = "000",
                                                ShoreLine = "shore",
                                                Address = "addr",
                                                Latitude = 0.0,
                                                Longitude = 0.0,
                                                DockTypeId = 1,
                                                DockType = "Dock",
                                            ),
                                        ),
                                    Business = emptyList(),
                                ),
                        ),
                    )
                }
            val categoriesUseCase =
                FetchVoyageCategoriesUseCase {
                    Result.success(
                        VoyageCategoryDropdownResponse(
                            Status = 200,
                            Message = "ok",
                            obj = listOf(VoyageCategory(ParentId = 0, Id = 1, Name = "Cat")),
                        ),
                    )
                }

            val placesResult = placesUseCase()
            val categoriesResult = categoriesUseCase()

            assertTrue(placesResult.isSuccess)
            assertEquals("ok", placesResult.getOrNull()?.Message)
            assertTrue(categoriesResult.isSuccess)
            assertEquals("ok", categoriesResult.getOrNull()?.Message)
        }

    private fun samplePastVoyage(): PastVoyages {
        return PastVoyages(
            Id = "voy-1",
            Name = "Trip",
            CaptainUserId = "cap-1",
            CaptainName = "Captain",
            PickupDock = "A",
            PickupDockLatitude = 0.0,
            PickupDockLongitude = 0.0,
            DropOffDock = "B",
            DropOffDockLatitude = 0.0,
            DropOffDockLongitude = 0.0,
            BoatName = "Boat",
            BoatModel = "Model",
            OTP = 1234,
            Rating = 5.0,
            NoOfVoyagers = 2,
            AmountToPay = 10.0,
            WaterStay = "No",
            Duration = "1h",
            BookingDateTime = "2026-04-05",
            sponsors = emptyList(),
        )
    }
}
