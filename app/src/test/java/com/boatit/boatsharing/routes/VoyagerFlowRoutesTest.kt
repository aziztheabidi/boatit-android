package com.boatit.boatsharing.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class VoyagerFlowRoutesTest {
    @Test
    fun dashboard_buildsNullValueRoute() {
        assertEquals("dashboardScreen/null", VoyagerFlowRoutes.dashboard(null))
    }

    @Test
    fun dashboard_buildsFlaggedRoute() {
        assertEquals("dashboardScreen/True", VoyagerFlowRoutes.dashboard("True"))
    }

    @Test
    fun createVoyageSponsor_buildsTypedBooleanRoute() {
        assertEquals("CreateVoyageSponsorScreen/true", VoyagerFlowRoutes.createVoyageSponsor(true))
        assertEquals("CreateVoyageSponsorScreen/false", VoyagerFlowRoutes.createVoyageSponsor(false))
    }
}
