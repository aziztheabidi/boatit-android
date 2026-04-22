package com.boatit.boatsharing.features.business.domain.usecase

import com.boatit.boatsharing.features.business.model.BusinessData
import com.boatit.boatsharing.features.business.model.BusinessHour
import com.boatit.boatsharing.features.business.model.BusinessRequest
import com.boatit.boatsharing.features.business.model.DeleteRequest
import com.boatit.boatsharing.features.business.model.DockDropdownItem
import com.boatit.boatsharing.features.business.model.DockDropdownObj
import com.boatit.boatsharing.features.business.model.DocksDropdownResponse
import com.boatit.boatsharing.features.business.model.GetBusinessResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessInfoResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessDashboardUseCasesTest {
    @Test
    fun fetchBusinessAndDocksUseCases_returnGatewayResults() =
        runBlocking {
            val fetchBusinessUseCase =
                FetchBusinessDashboardProfileUseCase {
                    Result.success(
                        GetBusinessResponse(
                            Status = 200,
                            Message = "ok",
                            obj =
                                BusinessData(
                                    LogoPath = "logo",
                                    BusinessType = "dock",
                                    YearOfEstablishment = 2020,
                                    Description = "desc",
                                    ImagesPath = emptyList(),
                                    Location = "loc",
                                    BusinessHours = listOf(BusinessHour("Mon", "09:00", "18:00")),
                                    IsDock = true,
                                    Name = "Biz",
                                    ShoreId = 1,
                                    ShoreName = "Shore",
                                    ZoneId = 2,
                                    ZoneName = "Zone",
                                    IslandId = 3,
                                    IslandName = "Island",
                                    State = "State",
                                    City = "City",
                                    ZipCode = "000",
                                    Address = "Addr",
                                    Latitude = 0.0,
                                    Longitude = 0.0,
                                    UserId = "u-1",
                                    ChangedOn = "2026-04-07",
                                    ChangedBy = "u-1",
                                ),
                        ),
                    )
                }
            val fetchDocksUseCase =
                FetchBusinessDocksUseCase {
                    Result.success(
                        DocksDropdownResponse(
                            Status = 200,
                            Message = "ok",
                            obj =
                                DockDropdownObj(
                                    Shore = listOf(DockDropdownItem(0, 1, "A")),
                                    Zone = listOf(DockDropdownItem(1, 2, "B")),
                                    Island = listOf(DockDropdownItem(2, 3, "C")),
                                ),
                        ),
                    )
                }

            val businessResult = fetchBusinessUseCase()
            val docksResult = fetchDocksUseCase()

            assertTrue(businessResult.isSuccess)
            assertEquals("ok", businessResult.getOrNull()?.Message)
            assertTrue(docksResult.isSuccess)
            assertEquals("ok", docksResult.getOrNull()?.Message)
        }

    @Test
    fun saveAndDeleteUseCases_returnGatewayResults() =
        runBlocking {
            val saveUseCase =
                SaveBusinessDashboardProfileUseCase {
                    Result.success(SaveBusinessInfoResponse(Status = 200, Message = "saved", obj = "ok"))
                }
            val deleteUseCase =
                DeleteBusinessDashboardImageUseCase {
                    Result.success(SaveBusinessInfoResponse(Status = 200, Message = "deleted", obj = "ok"))
                }

            val saveResult =
                saveUseCase(
                    BusinessRequest(
                        Location = "loc",
                        BusinessHours = listOf(BusinessHour("Mon", "09:00", "18:00")),
                        IsDock = true,
                        ShoreId = 1,
                        Name = "Biz",
                        ZoneId = 2,
                        IslandId = 3,
                        State = "State",
                        City = "City",
                        ZipCode = "000",
                        ShoreLine = "shore",
                        Address = "Addr",
                        Latitude = 0.0,
                        Longitude = 0.0,
                        Description = "desc",
                    ),
                )
            val deleteResult = deleteUseCase(DeleteRequest(userId = "u-1", path = "img"))

            assertTrue(saveResult.isSuccess)
            assertEquals("saved", saveResult.getOrNull()?.Message)
            assertTrue(deleteResult.isSuccess)
            assertEquals("deleted", deleteResult.getOrNull()?.Message)
        }
}
