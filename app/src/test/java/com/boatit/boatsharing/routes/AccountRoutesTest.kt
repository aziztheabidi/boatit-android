package com.boatit.boatsharing.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class AccountRoutesTest {
    @Test
    fun settings_buildsEncodedRoute() {
        assertEquals("settingsScreen/voyagerRole", AccountRoutes.settings("voyagerRole"))
    }

    @Test
    fun userAccountInfo_buildsEncodedRoute() {
        assertEquals("userAccountInfoScreen/voyagerRole", AccountRoutes.userAccountInfo("voyagerRole"))
    }

    @Test
    fun createAccount_routes_encodeReservedCharacters() {
        assertEquals(
            "createAccountStepTwoScreen/test%40mail.com",
            AccountRoutes.createAccountStepTwo("test@mail.com"),
        )
        assertEquals(
            "createAccountStepThreeScreen/tok%2Fvalue",
            AccountRoutes.createAccountStepThree("tok/value"),
        )
    }
}
